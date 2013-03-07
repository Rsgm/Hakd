package hakd.internet;

import hakd.networks.Network;
import hakd.networks.ServiceProvider;
import hakd.networks.devices.Dns;

import java.util.ArrayList;

public class NetworkController {
	private static ArrayList<Network>			networks			= new ArrayList<Network>();
	private static ArrayList<Dns>				publicDns			= new ArrayList<Dns>();
	private static ArrayList<ServiceProvider>	serviceProviders	= new ArrayList<ServiceProvider>();

	// returns the network at the given address
	public static Network getNetwork(String address) {
		for (Dns d : publicDns) {
			Network n = d.getNetwork(address);
			if (n != null) {
				return n;
			}
		}
		return null;
	}

	// public dns ip request, gets the ip of a server at that address
	public static String getIp(String address) {
		for (Dns d : publicDns) {
			String s = d.getIp(address);
			if (s != null) {
				return s;
			}
		}
		return null;
	}

	public static void addPublicNetwork(Network network) {
		for (Dns d : publicDns) {
			d.getHosts().add(network);
		}
	}

	// removes references to a network from all public DNSs(there may not be any) and the network list
	public static void removePublicNetwork(Network network) {
		for (Dns d : publicDns) {
			d.getHosts().add(network);
		}
		networks.remove(network);
		// TODO remove graphical data, or put that in a better spot to be more modular
	}

	public static ArrayList<Network> getNetworks() {
		return networks;
	}

	public static void setNetworks(ArrayList<Network> networks) {
		NetworkController.networks = networks;
	}

	public static ArrayList<ServiceProvider> getServiceProviders() {
		return serviceProviders;
	}

	public static void setServiceProviders(ArrayList<ServiceProvider> serviceProviders) {
		NetworkController.serviceProviders = serviceProviders;
	}

	public static ArrayList<Dns> getPublicDns() {
		return publicDns;
	}

	public static void setPublicDns(ArrayList<Dns> publicDns) {
		NetworkController.publicDns = publicDns;
	}
}
