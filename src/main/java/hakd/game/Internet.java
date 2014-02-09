package hakd.game;

import hakd.game.gameplay.City;
import hakd.networks.BackboneProviderNetwork;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;
import hakd.networks.Network.NetworkType;
import hakd.networks.NetworkFactory;
import hakd.networks.devices.Device;

import java.util.*;

public final class Internet {
    /**
     * Contains all ISPs. Note: ISPs can only be public.
     */
    private final Map<String, InternetProviderNetwork> internetProviderNetworksMap;

    /**
     * Contains all Backbone networks. Note: Backbone networks can only be
     * public.
     */
    private final Map<String, BackboneProviderNetwork> backboneProviderNetworksMap;

    /**
     * Contains all networks with an IP.
     */
    private final Map<String, Network> networkMap;

    /**
     * Contains all IPs and their registered addresses, if they have one.
     */
    private final Map<String, short[]> addressMap;

    /**
     * Used when generating IPs to find an unused, random ip.
     */
    public static List<Short> ipNumbers = new ArrayList<Short>(255);

    /**
     * This is only created at the start of the game.
     *
     * @param cities
     */
    public Internet(HashMap<String, City> cities) {
        int backbones = cities.size();
        int isps = (int) (backbones * (Math.random() * 2 + 1));
        int networks = (int) (isps * (Math.random() * 4 + 2));

        backboneProviderNetworksMap = new HashMap<String, BackboneProviderNetwork>(isps);
        internetProviderNetworksMap = new HashMap<String, InternetProviderNetwork>(backbones);
        networkMap = new HashMap<String, Network>(networks + isps + backbones);
        addressMap = new HashMap<String, short[]>(networks + isps + backbones);

        for (short i = 1; i <= 256; i++) {
            ipNumbers.add(i);
        }

        generateBackbones(cities);
        generateIsps(isps, cities);
        generateNetworks(networks, cities);
    }

    private void generateBackbones(HashMap<String, City> cities) {
        // each ipRegion gets at least one backbone, possibly several
        for (City city : cities.values()) {
            BackboneProviderNetwork backbone = NetworkFactory.createBackbone(this, city);

            short[] ip = {0, 1, 1, 1};
            do {
                ip[0] = generateIpByte(backbone.getIpRegion());
            } while (networkMap.containsKey(ipToString(ip)) && networkMap.size() < 256);
            backbone.setIp(ipToString(ip));

            backboneProviderNetworksMap.put(backbone.getIp(), backbone);
            networkMap.put(backbone.getIp(), backbone);

            city.addNetwork(backbone);
        }


        // create backbone lines
//        List<Vector2> positions = new ArrayList<Vector2>(backboneProviderNetworksMap.size());
//        for (BackboneProviderNetwork b : backboneProviderNetworksMap) {
//            positions.add(b.getPos());
//
//            TODO I guess use Delaunay Triangulation https://github.com/irstv/jdelaunay/wiki
//        }
//        if (!isIntersected) {
//            Vector2 v1 = new Vector2(b1.getPos());
//            Vector2 v2 = new Vector2(b2.getPos());
//            Sprite line = Assets.nearestTextures.createSprite("dashedLine");
//            line.setOrigin(0, 0);
//            line.setSize(v1.dst(v2), 3);
//            line.setPosition(v1.x, v1.y);
//            line.setRotation(v1.sub(v2).scl(-1).angle());
//            b1.getBackboneConnectionLines().add(line);
//            b2.getBackboneConnectionLines().add(line);
// }
    }

    private void generateIsps(int amount, HashMap<String, City> cities) {
        List<City> citiesShuffled = new ArrayList<City>(cities.values());

        for (int i = 0; i < amount; i++) {
            float random = (float) (Math.random() * 2 - 1);
            Collections.shuffle(citiesShuffled);

            for (City c : citiesShuffled) {
                if (c.getDensity() >= random) {
                    List<BackboneProviderNetwork> cityBackbones = new ArrayList<BackboneProviderNetwork>();
                    int isps = 0;
                    for (Network n : c.getNetworks().values()) {
                        if (n instanceof BackboneProviderNetwork) {
                            cityBackbones.add((BackboneProviderNetwork) n);
                        } else if (n instanceof InternetProviderNetwork) {
                            isps++;
                        }
                    }

                    if (cityBackbones.isEmpty() || isps > 5) {
                        continue;
                    }

                    BackboneProviderNetwork b = cityBackbones.get((int) (Math.random() * cityBackbones.size()));

                    if (b.getIpChildNetworkHashMap().size() < 256) {
                        InternetProviderNetwork isp = NetworkFactory.createISP(this, c);
                        b.registerNewIsp(isp, 1);
                        internetProviderNetworksMap.put(isp.getIp(), isp);
                        networkMap.put(isp.getIp(), isp);
                        c.addNetwork(isp);

                        break;
                    }
                }
            }
        }

        // check for cities without isps
        for (City c : cities.values()) {
            BackboneProviderNetwork b = null;
            boolean hasIsp = false;

            for (Network n : c.getNetworks().values()) {
                if (n instanceof BackboneProviderNetwork) {
                    b = (BackboneProviderNetwork) n;
                } else if (n instanceof InternetProviderNetwork) {
                    hasIsp = true;
                    break;
                }
            }

            if (b == null || hasIsp) {
                continue;
            }

            if (b.getIpChildNetworkHashMap().size() < 256) {
                InternetProviderNetwork isp = NetworkFactory.createISP(this, c);
                b.registerNewIsp(isp, 1);
                internetProviderNetworksMap.put(isp.getIp(), isp);
                networkMap.put(isp.getIp(), isp);
                c.addNetwork(isp);
            }
        }
    }


    /**
     * Creates the initial game networks.
     */
    private void generateNetworks(int amount, HashMap<String, City> cities) {
        List<City> citiesShuffled = new ArrayList<City>(cities.values());

        for (int i = 0; i < amount; i++) {
            float random = (float) (Math.random() * 2 - 1);
            Collections.shuffle(citiesShuffled);

            for (City c : citiesShuffled) {
                if (c.getDensity() >= random) {
                    List<InternetProviderNetwork> cityISPs = new ArrayList<InternetProviderNetwork>();
                    for (Network n : c.getNetworks().values()) {
                        if (n instanceof InternetProviderNetwork) {
                            cityISPs.add((InternetProviderNetwork) n);
                        }
                    }

                    if (cityISPs.isEmpty()) {
                        continue;
                    }

                    InternetProviderNetwork isp = cityISPs.get((int) (Math.random() * cityISPs.size()));

                    if (isp.getIpChildNetworkHashMap().size() < 256) {
                        Network network = NetworkFactory.createNetwork(NetworkType.NPC, c, this);
                        addNetworkToInternet(network, isp);
                        c.addNetwork(network);

                        break;
                    }
                }
            }
        }


    }

    /**
     * Adds a network to the internet(network map) and the specified ISP. Not for provider networks.
     */
    public void addNetworkToInternet(Network network, InternetProviderNetwork isp) {
        isp.registerNewNetwork(network, 1);
        networkMap.put(network.getIp(), network);
        network.setInternet(this);
    }

    /**
     * Returns a list of networks of the given type.
     */
    public Map<String, Network> getNetworksByType(NetworkType type) {
        Map<String, Network> map = new HashMap<String, Network>();
        for (Network n : networkMap.values()) {
            if (n.getType() == type) {
                map.put(n.getIp(), n);
            }
        }
        return map;
    }

    /**
     * Used to create a (somewhat) realistic, random ip loosely based on the
     * ipv4 internet map. This is used mostly with the assign ip method, but can
     * be useful for other things.
     */
    public static short generateIpByte(Network.IpRegion ipRegion) {
        return (short) (Math.random() * (ipRegion.max - ipRegion.min) + ipRegion.min);
    }

    /**
     * Returns an ip to an object that calls this, also checks it and adds it
     * to the dns map.
     */
    public String assignIp(Network network) {
        short[] ip = null;
        short[] parentIp = ipFromString(network.getParent().getIp());

        Collections.shuffle(ipNumbers);

        for (short i = 0; i < 255; i++) {
            // just randomly generate numbers for the isp, there are not enough to slow it down
            switch (network.getType()) {
                case ISP:
                    ip = new short[]{parentIp[0], ipNumbers.get(i), generateIpByte(Network.IpRegion.none), 1};
                    // networks always end in 1
                    break;
                default:
                    ip = new short[]{parentIp[0], parentIp[1], ipNumbers.get(i), 1};
                    break;
            }

            if (!addressMap.containsValue(ip) && !networkMap.containsKey(ipToString(ip))) {
                break;
            }
        }
        return ipToString(ip);
    }

    /**
     * Registers a url to an ip so that not everything is an IP, a player can buy an address.
     */
    public boolean addUrl(String ip, String address) {
        if (address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$") && !addressMap.containsValue(address)) { // address regex
            getDevice(ip).setAddress(address);
            return true; // "you have successfully registered the url " + url +
            // " for the ip " + ip;
        }
        return false; // "Sorry, either that URL is already registered, or a bug)."
    }

    /**
     * Searches for the device with the given ip on the public internet.
     */
    public Device getDevice(String ip) {
        short[] a = ipFromString(ip); // used to search through the hashmap because it only lists networks, which always end in 1s
        a[3] = 1;
        System.out.println(networkMap.containsKey(ipToString(a)));
        return networkMap.get(ipToString(a)).getDevice(ip);
    }

    /**
     * Public dns ip request, gets the ip of a server at that address.
     */
    public short[] getIp(String address) {
        if (!addressMap.containsKey(address)) {
            return null;
        }
        return addressMap.get(address);
    }

    /**
     * Removes references to a network from all public DNSs(there may not be
     * any) and the network map. You can either leave it up to the GC to
     * remove, or use the network privately.
     */
    public void removePublicNetwork(Network network) {
        networkMap.remove(network.getIp());
        // TODO remove graphical data, or put that in a better spot to be more modular
    }

    public static String ipToString(short[] ip) {
        return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3];
    }

    public static short[] ipFromString(String ip) {
        short[] array = new short[4];

        if (ip.matches("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")) {
            Scanner scanner = new Scanner(ip);
            scanner.useDelimiter("\\.");
            for (int i = 0; i < 4; i++) {
                array[i] = scanner.nextShort();
            }
            scanner.close();
        }
        return array;
    }

    public Map<String, InternetProviderNetwork> getInternetProviderNetworksMap() {
        return internetProviderNetworksMap;
    }

    public Map<String, BackboneProviderNetwork> getBackboneProviderNetworksMap() {
        return backboneProviderNetworksMap;
    }

    public Map<String, Network> getNetworkMap() {
        return networkMap;
    }

    public Map<String, short[]> getAddressMap() {
        return addressMap;
    }

    public static List<Short> getIpNumbers() {
        return ipNumbers;
    }
}
