package hakd.internet;

import hakd.networks.Network;
import hakd.networks.ServiceProvider;
import hakd.networks.devices.Dns;
import hakd.other.enumerations.NetworkType;
import hakd.other.enumerations.names.Owner;

import java.util.ArrayList;

public class NetworkController {
	private static ArrayList<Network>			PublicNetworks		= new ArrayList<Network>();
	private static ArrayList<Dns>				publicDns			= new ArrayList<Dns>();
	private static ArrayList<ServiceProvider>	serviceProviders	= new ArrayList<ServiceProvider>();
	private static ArrayList<Owner>				owners				= new ArrayList<Owner>();

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

	// returns the networks of the given type
	public static ArrayList<Network> getNetworkByType(NetworkType type) {
		ArrayList<Network> array = new ArrayList<Network>();
		for (Network n : PublicNetworks) {
			if (n.getType() == type) {
				array.add(n);
			}
		}
		return array;
	}

	// public dns ip request, gets the ip of a server at that address
	public static String getIp(String address) {
		for (Dns d : publicDns) {
			String s = d.getIp(address);
			if (s != null) { // why is this needed? removing it is probably not worth what ever it causes
				return s; // oh, this is needed so it doesn't return if the first dns does not have it
			}
		}
		return null;
	}

	// add the network to all public DNSs
	public static Network addPublicNetwork(NetworkType type) {
		Network n = addNetwork(type);
		n.getIsp().register(n, 5);
		return n;
	}

	// removes references to a network from all public DNSs(there may not be any) and the network list
	public static void removePublicNetwork(Network network) {
		for (Dns d : publicDns) {
			d.getHosts().remove(network);
		}
		PublicNetworks.remove(network);
		// TODO remove graphical data, or put that in a better spot to be more modular
	}

	// creates a network, needed because you can't add(this) in a constructor, strange
	@SuppressWarnings("deprecation")
	public static Network addNetwork(NetworkType type) {
		Network n = new Network(type); // this is the only place to use this constructor
		PublicNetworks.add(n);
		return n;
	}

	// removes a network
	public static void removeNetwork(Network network) {
		PublicNetworks.remove(network);
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

	public static ArrayList<Owner> getOwners() {
		return owners;
	}

	public static void setOwners(ArrayList<Owner> owners) {
		NetworkController.owners = owners;
	}

	public static ArrayList<Network> getPublicNetworks() {
		return PublicNetworks;
	}

	public static void setPublicNetworks(ArrayList<Network> publicNetworks) {
		PublicNetworks = publicNetworks;
	}
}
