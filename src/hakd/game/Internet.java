package hakd.game;

import com.badlogic.gdx.Gdx;
import hakd.networks.BackboneProviderNetwork;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;
import hakd.networks.Network.NetworkType;
import hakd.networks.NetworkFactory;
import hakd.networks.devices.Device;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
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
	 * Handles all of the device connections.
	 */
	private static ServerSocket serverSocket;
	private static Thread thread;

	/**
	 * This is only created at the start of the game.
	 */
	public Internet() {
		int backbones = 4;// (int) (Math.random() * 3 + IpRegion.values().length);
		int isps = 20; //(int) (Math.random() * 8 + 8);
		int networks = 100;//(int) (Math.random() * 15 + 60);

		backboneProviderNetworks = new ArrayList<BackboneProviderNetwork>(isps);
		internetProviderNetworks = new ArrayList<InternetProviderNetwork>(backbones);
		ipNetworkHashMap = new HashMap<String, Network>(networks + isps + backbones);
		addressIpHashMap = new HashMap<String, short[]>(networks + isps + backbones);

		startServer();

		for(short i = 1; i <= 256; i++) {
			ipNumbers.add(i);
		}

		generateBackbones(backbones);
		generateIsps(isps);
		generateNetworks(networks);
	}

	/**
	 * Called on internet creation.
	 */
	private static void startServer() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress((int) (Math.random() * 10000 + 40000))); // this could cause (hilarious) problems if you run more than one instance of the two games share a server

			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(Gdx.app.getApplicationListener() != null) { // better than while(true){}
						try {
							serverSocket.accept();
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread.start();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void generateBackbones(int amount) {
		// each ipRegion gets at least one backbone, possibly several
		for(int i = 0; i < amount; i++) {
			BackboneProviderNetwork backbone = NetworkFactory.createBackbone(this);

			short[] ip = {generateIpByte(backbone.getIpRegion()), 1, 1, 1};
			backbone.setIp(ipToString(ip));

			backboneProviderNetworks.add(backbone);
			ipNetworkHashMap.put(backbone.getIp(), backbone);
		}
	}

	private void generateIsps(int amount) {
		for(int i = 0; i < amount; i++) {
			System.out.println("ISP - " + i);
			int a = internetProviderNetworks.size() % backboneProviderNetworks.size();
			if(backboneProviderNetworks.get(a).getIpChildNetworkHashMap().size() >= 256) {
				System.out.println("ISP - No free backbone child spots");
			} else {
				InternetProviderNetwork isp = NetworkFactory.createISP(this);
				backboneProviderNetworks.get(a).registerAnIsp(isp, 1);
				internetProviderNetworks.add(isp);
				ipNetworkHashMap.put(isp.getIp(), isp);
			}
		}
	}

	/**
	 * Creates the initial game networks.
	 */
	private void generateNetworks(int amount) {
		for(int i = 0; i < amount; i++) {
			System.out.println("NETWORK - " + i);
			Network network;

			int a;
			for(int j = 0; j < internetProviderNetworks.size() * 2; j++) {
				a = (int) (Math.random() * internetProviderNetworks.size());
				if(internetProviderNetworks.get(a).getIpChildNetworkHashMap().size() >= 256) {
					System.out.println("ISP - No free backbone child spots");
				} else {
					int random = (int) (Math.random() * 10);
					if(random < 7) { // chances of generating a certain network type
						network = NetworkFactory.createNetwork(NetworkType.NPC);
					} else if(random <= 8) {
						network = NetworkFactory.createNetwork(NetworkType.BUSINESS);
					} else {
						network = NetworkFactory.createNetwork(NetworkType.GOVERNMENT);
					}

					addNetworkToInternet(network, internetProviderNetworks.get(a));
					break;
				}
			}
		}
	}

	/**
	 * Adds a network to the internet(network list) and the specified ISP. Not for provider networks.
	 */
	public void addNetworkToInternet(Network network, InternetProviderNetwork isp) {
		isp.registerANetwork(network, 1);
		ipNetworkHashMap.put(network.getIp(), network);
		network.setInternet(this);
	}

	/**
	 * Returns a list of networks of the given type.
	 */
	public List<Network> getNetworkByType(NetworkType type) {
		List<Network> array = new ArrayList<Network>();
		for(Network n : ipNetworkHashMap.values()) {
			if(n.getType() == type) {
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

		for(short i = 0; i < 255; i++) {
			// just randomly generate numbers for the isp, there are not enough to slow it down
			switch(network.getType()) {
				case ISP:
					ip = new short[]{parentIp[0], ipNumbers.get(i), generateIpByte(Network.IpRegion.none), 1};
					// networks always end in 1
					break;
				default:
					ip = new short[]{parentIp[0], parentIp[1], ipNumbers.get(i), 1};
					break;
			}

			if(!addressIpHashMap.containsValue(ip) && !ipNetworkHashMap.containsKey(ip)) {
				break;
			}
		}
		return ipToString(ip);
	}

	/**
	 * Registers a url to an ip so that not everything is an IP, a player can buy an address.
	 */
	public boolean addUrl(String ip, String address) {
		if(address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$") && !addressIpHashMap.containsValue(address)) { // address regex
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
		a[4] = 1;
		return ipNetworkHashMap.get(ipFromString(ip)).getDevice(ipToString(a));
	}

	/**
	 * Public dns ip request, gets the ip of a server at that address.
	 */
	public short[] getIp(String address) {
		if(!addressIpHashMap.containsValue(address)) {
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
		ipNetworkHashMap.remove(network);
		// TODO remove graphical data, or put that in a better spot to be more modular
	}

	public static String ipToString(short[] ip) {
		return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3];
	}

	public static short[] ipFromString(String ip) {
		short[] array = new short[4];

		if(ip.matches("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")) {
			Scanner scanner = new Scanner(ip);
			scanner.useDelimiter("\\.");
			for(int i = 0; i < 4; i++) {
				array[i] = scanner.nextShort();
			}
			scanner.close();
		}
		return array;
	}

	public enum Protocol {
		FTP(21), SSH(22), SMTP(25), WHOIS(43), DNS(53), HTTP(80), HTTPS(443), STEAM(1725), XBOX(3074), MYSQL(3306),
		RDP(3389), WOW(3724), UPUP(5000), IRC(6667), TORRENT(6881), LAMBDA(27015), COD(28960), LEET(31337);

		public final int portNumber; // these are only default ports

		private Protocol(int portNumber) {
			this.portNumber = portNumber;
		}

		public static Protocol getProtocol(int port) {
			for(Protocol p : Protocol.values()) {
				if(p.portNumber == port) {
					return p;
				}
			}
			return HTTP;
		}
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

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static Thread getThread() {
		return thread;
	}
}
