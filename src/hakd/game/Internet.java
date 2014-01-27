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
    private final List<InternetProviderNetwork> internetProviderNetworks;

    /**
     * Contains all Backbone networks. Note: Backbone networks can only be
     * public.
     */
    private final List<BackboneProviderNetwork> backboneProviderNetworks;

    /**
     * Contains all networks with an IP.
     */
    private final Map<String, Network> ipNetworkHashMap;

    /**
     * Contains all IPs and their registered addresses, if they have one.
     */
    private final Map<String, short[]> addressIpHashMap;

    /**
     * Used when generating IPs to find an unused, random ip.
     */
    public static List<Short> ipNumbers = new ArrayList<Short>(255);

    /**
     * This is only created at the start of the game.
     */
    public Internet(List<City> cities) {
        int backbones = cities.size(); // (int) (Math.random() * 3 + Network.IpRegion.values().length);
        int isps = (int) (backbones * (Math.random() * 2 + 1));
        int networks = (int) (isps * (Math.random() * 4 + 2));

        backboneProviderNetworks = new ArrayList<BackboneProviderNetwork>(isps);
        internetProviderNetworks = new ArrayList<InternetProviderNetwork>(backbones);
        ipNetworkHashMap = new HashMap<String, Network>(networks + isps + backbones);
        addressIpHashMap = new HashMap<String, short[]>(networks + isps + backbones);

        for (short i = 1; i <= 256; i++) {
            ipNumbers.add(i);
        }

        generateBackbones(backbones, cities);
        generateIsps(isps, cities);
        generateNetworks(networks, cities);
    }

    private void generateBackbones(int amount, List<City> cities) {
        // each ipRegion gets at least one backbone, possibly several
        for (int i = 0; i < amount; i++) {
            City city = cities.get(i % cities.size());

            BackboneProviderNetwork backbone = NetworkFactory.createBackbone(this, city);

            short[] ip = {0, 1, 1, 1};
            do {
                ip[0] = generateIpByte(backbone.getIpRegion());
            } while (ipNetworkHashMap.containsKey(ipToString(ip)) && ipNetworkHashMap.size() < 256);
            backbone.setIp(ipToString(ip));

            backboneProviderNetworks.add(backbone);
            ipNetworkHashMap.put(backbone.getIp(), backbone);

            city.addNetwork(backbone);
        }


        // create backbone lines
//        List<Vector2> positions = new ArrayList<Vector2>(backboneProviderNetworks.size());
//        for (BackboneProviderNetwork b : backboneProviderNetworks) {
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

    private void generateIsps(int amount, List<City> cities) {
        List<City> citiesShuffled = new ArrayList<City>(cities.size());
        citiesShuffled.addAll(cities);

        for (int i = 0; i < amount; i++) {
            float random = (float) (Math.random() * 2 - 1);
            Collections.shuffle(citiesShuffled);

            for (City c : citiesShuffled) {
                if (c.getDensity() >= random) {
                    List<BackboneProviderNetwork> cityBackbones = new ArrayList<BackboneProviderNetwork>();
                    int isps = 0;
                    for (Network n : c.getNetworks()) {
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
                        internetProviderNetworks.add(isp);
                        ipNetworkHashMap.put(isp.getIp(), isp);
                        c.addNetwork(isp);

                        break;
                    }
                }
            }
        }

        // check for cities without isps
        for (City c : cities) {
            BackboneProviderNetwork b = null;
            boolean hasIsp = false;

            for (Network n : c.getNetworks()) {
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
                internetProviderNetworks.add(isp);
                ipNetworkHashMap.put(isp.getIp(), isp);
                c.addNetwork(isp);
            }
        }
    }


    /**
     * Creates the initial game networks.
     */
    private void generateNetworks(int amount, List<City> cities) {
        List<City> citiesShuffled = new ArrayList<City>(cities.size());
        citiesShuffled.addAll(cities);

        for (int i = 0; i < amount; i++) {
            float random = (float) (Math.random() * 2 - 1);
            Collections.shuffle(citiesShuffled);

            for (City c : citiesShuffled) {
                if (c.getDensity() >= random) {
                    List<InternetProviderNetwork> cityISPs = new ArrayList<InternetProviderNetwork>();
                    for (Network n : c.getNetworks()) {
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
     * Adds a network to the internet(network list) and the specified ISP. Not for provider networks.
     */
    public void addNetworkToInternet(Network network, InternetProviderNetwork isp) {
        isp.registerNewNetwork(network, 1);
        ipNetworkHashMap.put(network.getIp(), network);
        network.setInternet(this);
    }

    /**
     * Returns a list of networks of the given type.
     */
    public List<Network> getNetworkByType(NetworkType type) {
        List<Network> array = new ArrayList<Network>();
        for (Network n : ipNetworkHashMap.values()) {
            if (n.getType() == type) {
                array.add(n);
            }
        }
        return array;
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
     * to the dns list.
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

            if (!addressIpHashMap.containsValue(ip) && !ipNetworkHashMap.containsKey(ipToString(ip))) {
                break;
            }
        }
        return ipToString(ip);
    }

    /**
     * Registers a url to an ip so that not everything is an IP, a player can buy an address.
     */
    public boolean addUrl(String ip, String address) {
        if (address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$") && !addressIpHashMap.containsValue(address)) { // address regex
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
        System.out.println(ipNetworkHashMap.containsKey(ipToString(a)));
        return ipNetworkHashMap.get(ipToString(a)).getDevice(ip);
    }

    /**
     * Public dns ip request, gets the ip of a server at that address.
     */
    public short[] getIp(String address) {
        if (!addressIpHashMap.containsKey(address)) {
            return null;
        }
        return addressIpHashMap.get(address);
    }

    /**
     * Removes references to a network from all public DNSs(there may not be
     * any) and the network list. You can either leave it up to the GC to
     * remove, or use the network privately.
     */
    public void removePublicNetwork(Network network) {
        ipNetworkHashMap.remove(network.getIp());
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

    public List<InternetProviderNetwork> getInternetProviderNetworks() {
        return internetProviderNetworks;
    }

    public List<BackboneProviderNetwork> getBackboneProviderNetworks() {
        return backboneProviderNetworks;
    }

    public Map<String, Network> getIpNetworkHashMap() {
        return ipNetworkHashMap;
    }

    public Map<String, short[]> getAddressIpHashMap() {
        return addressIpHashMap;
    }

    public static List<Short> getIpNumbers() {
        return ipNumbers;
    }
}
