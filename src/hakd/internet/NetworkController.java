package hakd.internet;

import hakd.networks.Network;
import hakd.networks.Network.NetworkType;
import hakd.networks.ServiceProvider;
import hakd.networks.devices.Dns;
import hakd.other.names.Owner;

import java.util.ArrayList;
import java.util.List;

public class NetworkController {
    private static List<Network> PublicNetworks = new ArrayList<Network>();
    private static List<Dns> publicDns = new ArrayList<Dns>();
    private static List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
    private static List<Owner> owners = new ArrayList<Owner>();

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
    public static List<Network> getNetworkByType(NetworkType type) {
	List<Network> array = new ArrayList<Network>();
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
	    if (s != null) { // why is this needed? removing it is probably not
			     // worth what ever it causes
		return s; // oh, this is needed so it doesn't return if the
			  // first dns does not have it
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

    // removes references to a network from all public DNSs(there may not be
    // any) and the network list
    public static void removePublicNetwork(Network network) {
	for (Dns d : publicDns) {
	    d.getHosts().remove(network);
	}
	PublicNetworks.remove(network);
	// TODO remove graphical data, or put that in a better spot to be more
	// modular
    }

    // creates a network, needed because you can't add(this) in a constructor,
    // strange
    @SuppressWarnings("deprecation")
    public static Network addNetwork(Network.NetworkType type) {
	Network n = new Network(type); // this is the only place to use this
				       // constructor
	PublicNetworks.add(n);
	return n;
    }

    public enum Protocol { // protocol(port)
	FTP(21), SSH(22), SMTP(25), WHOIS(43), DNS(53), HTTP(80), HTTPS(443), STEAM(
		1725), XBOX(3074), MYSQL(3306), RDP(3389), WOW(3724), UPUP(5000), IRC(
		6667), TORRENT(6881), LAMBDA(27015), COD(28960), LEET(31337);

	public int port; // these are only default ports

	private Protocol(int port) {
	    this.port = port;
	}

	public static Protocol getProtocol(int port) {
	    for (Protocol p : Protocol.values()) {
		if (p.port == port) {
		    return p;
		}
	    }
	    return HTTP;
	}
    }

    // removes a network
    public static void removeNetwork(Network network) {
	PublicNetworks.remove(network);
    }

    public static List<ServiceProvider> getServiceProviders() {
	return serviceProviders;
    }

    public static void setServiceProviders(
	    List<ServiceProvider> serviceProviders) {
	NetworkController.serviceProviders = serviceProviders;
    }

    public static List<Dns> getPublicDns() {
	return publicDns;
    }

    public static void setPublicDns(List<Dns> publicDns) {
	NetworkController.publicDns = publicDns;
    }

    public static List<Owner> getOwners() {
	return owners;
    }

    public static void setOwners(List<Owner> owners) {
	NetworkController.owners = owners;
    }

    public static List<Network> getPublicNetworks() {
	return PublicNetworks;
    }

    public static void setPublicNetworks(List<Network> publicNetworks) {
	PublicNetworks = publicNetworks;
    }
}
