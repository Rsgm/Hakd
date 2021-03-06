package game;

import game.gameplay.City;
import networks.BackboneProviderNetwork;
import networks.InternetProviderNetwork;
import networks.Network;
import networks.Network.NetworkType;
import networks.NetworkFactory;
import networks.devices.Device;

import java.util.*;

public class Internet {
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
    private final Map<String, String> addressMap;

    /**
     * Used when generating IPs to find an unused, random ip.
     */
    public static List<Short> ipNumbers = new ArrayList<Short>(255);

    public static final int BACKBONE_MULTIPLIER = 1;
    public static final int ISP_MULTIPLIER = 2;
    public static final int NETWORK_MULTIPLIER = 3;
    public final int INITIAL_BACKBONE_SIZE;
    public final int INITIAL_ISP_SIZE;

    /**
     * This is only created at the start of the game.
     *
     * @param cities list of cities in the world.
     */
    public Internet(HashMap<String, City> cities) {
        INITIAL_BACKBONE_SIZE = BACKBONE_MULTIPLIER * cities.size();
        INITIAL_ISP_SIZE = (int) (INITIAL_BACKBONE_SIZE * (Math.random() * ISP_MULTIPLIER + 1));
        int network_size = (int) (INITIAL_ISP_SIZE * (Math.random() * NETWORK_MULTIPLIER + 2)); // not very accurate, but it helps

        backboneProviderNetworksMap = new HashMap<String, BackboneProviderNetwork>(INITIAL_BACKBONE_SIZE);
        internetProviderNetworksMap = new HashMap<String, InternetProviderNetwork>(INITIAL_ISP_SIZE);
        networkMap = new HashMap<String, Network>(network_size + INITIAL_ISP_SIZE + INITIAL_BACKBONE_SIZE);
        addressMap = new HashMap<String, String>(network_size + INITIAL_ISP_SIZE + INITIAL_BACKBONE_SIZE);

        for (short i = 1; i <= 256; i++) {
            ipNumbers.add(i);
        }

        generateBackbones(cities);
        generateIsps(INITIAL_ISP_SIZE, cities);
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
     *
     * @return True if it was registered correctly, otherwise false.
     */
    public boolean addAddress(String ip, String address) {
        if (address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$") && !addressMap.containsValue(address)) { // address regex
            addressMap.put(address, ip);
            return true; // "you have successfully registered the url " + url +
            // " for the ip " + ip;
        }
        return false; // "Sorry, either that URL is already registered)."
    }

    /**
     * Remove an address from the addressMap.
     *
     * @return True if the address map does not contain the address, otherwise false. This will return true even if it never contained the address.
     */
    public boolean removeAddress(String address) {
        addressMap.remove(address);
        return !addressMap.containsKey(address);
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
    public String getIp(String address) {
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
        return Collections.unmodifiableMap(internetProviderNetworksMap);
    }

    public Map<String, BackboneProviderNetwork> getBackboneProviderNetworksMap() {
        return Collections.unmodifiableMap(backboneProviderNetworksMap);
    }

    public Map<String, Network> getNetworkMap() {
        return Collections.unmodifiableMap(networkMap);
    }

    public Map<String, String> getAddressMap() {
        return Collections.unmodifiableMap(addressMap);
    }

    public static List<Short> getIpNumbers() {
        return Collections.unmodifiableList(ipNumbers);
    }
}
