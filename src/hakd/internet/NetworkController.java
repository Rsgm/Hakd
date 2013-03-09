package hakd.internet;

import hakd.networks.Network;
import hakd.networks.ServiceProvider;
import hakd.networks.devices.Dns;
import hakd.other.enumerations.NetworkType;
import hakd.other.enumerations.names.Owner;

import java.util.ArrayList;
import java.util.Arrays;

public class NetworkController {
	private static ArrayList<Network>			networks			= new ArrayList<Network>();
	private static ArrayList<Dns>				publicDns			= new ArrayList<Dns>();
	private static ArrayList<ServiceProvider>	serviceProviders	= new ArrayList<ServiceProvider>();
	private static ArrayList<Owner>				owners				= (ArrayList<Owner>) Arrays.asList(Owner.values());

	// returns the network at the given address
	public static Network getNetwork(String address) {
		for (Dns d : publicDns) {
			Network n = d.findNetwork(address);
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

	// add the network to all public DNSs
	public static void addPublicNetwork(NetworkType type) {
		Network n = addNetwork(type);

		for (Dns d : publicDns) {
			d.getHosts().add(n);
		}
	}

	// removes references to a network from all public DNSs(there may not be any) and the network list
	public static void removePublicNetwork(Network network) {
		for (Dns d : publicDns) {
			d.getHosts().remove(network);
		}
		networks.remove(network);
		// TODO remove graphical data, or put that in a better spot to be more modular
	}

	// creates a network, needed because you can't add(this) in a constructor, strange
	@SuppressWarnings("deprecation")
	public static Network addNetwork(NetworkType type) {
		Network n = new Network(type); // this is the only place to use this constructor
		networks.add(n);
		return n;
	}

	// removes a network
	public static void removeNetwork(Network network) {
		networks.remove(network);
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
