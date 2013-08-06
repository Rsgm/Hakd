package hakd.internet;

import hakd.networks.Network;
import hakd.networks.Network.NetworkType;
import hakd.networks.ServiceProvider;
import hakd.networks.devices.Dns;
import hakd.other.names.Isp;
import hakd.other.names.Owner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Internet {
    private List<Network> PublicNetworks = new ArrayList<Network>();
    private List<Dns> publicDns = new ArrayList<Dns>();
    private List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
    private List<Owner> owners = new ArrayList<Owner>();

    public Internet() {
	owners.addAll(Arrays.asList(Owner.values()));
	// brands and models don't need this because you can have two of the
	// same brand

	generateIsps();
	generateNetworks();
    }

    private void generateIsps() {
	int amount = (int) (Math.random() * 6 + 6);
	List<Isp> names = new ArrayList<Isp>(
		Arrays.asList(Isp.values().clone()));

	for (int i = 0; i < amount; i++) {
	    int random = (int) (Math.random() * names.size());

	    @SuppressWarnings("deprecation")
	    ServiceProvider s = new ServiceProvider(names.get(random), this);

	    s.setIp(s.register(s, 10));
	    names.remove(random);
	    publicDns.add(s.getMasterDns());
	    serviceProviders.add(s);
	    PublicNetworks.add(s); // this may not be the best but lets see what
				   // happens
	}
    }

    // creates the initial game networks
    private void generateNetworks() {
	int amount = (int) (Math.random() * 6 + 40);

	for (int i = 0; i < amount; i++) {
	    int test = (int) (Math.random() * 10);
	    if (test < 7) {
		addPublicNetwork(NetworkType.NPC);
	    } else if (test < 9) {
		addPublicNetwork(NetworkType.COMPANY);
	    } else if (test == 9) {
		addPublicNetwork(NetworkType.TEST);
	    }
	}
    }

    // returns the network at the given address
    public Network getNetwork(String address) {
	for (Dns d : publicDns) {
	    Network n = d.findNetwork(address);
	    if (n != null) {
		return n;
	    }
	}
	return null;
    }

    // returns the networks of the given type
    public List<Network> getNetworkByType(NetworkType type) {
	List<Network> array = new ArrayList<Network>();
	for (Network n : PublicNetworks) {
	    if (n.getType() == type) {
		array.add(n);
	    }
	}
	return array;
    }

    // public dns ip request, gets the ip of a server at that address
    public String getIp(String address) {
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
    public Network addPublicNetwork(NetworkType type) {
	Network n = addNetwork(type);
	n.getIsp().register(n, 5);
	return n;
    }

    // removes references to a network from all public DNSs(there may not be
    // any) and the network list
    public void removePublicNetwork(Network network) {
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
    public Network addNetwork(Network.NetworkType type) {
	Network n = new Network(type, this); // this is the only place to use
					     // this
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
    public void removeNetwork(Network network) {
	PublicNetworks.remove(network);
    }

    public List<ServiceProvider> getServiceProviders() {
	return serviceProviders;
    }

    public void setServiceProviders(List<ServiceProvider> serviceProviders) {
	this.serviceProviders = serviceProviders;
    }

    public List<Dns> getPublicDns() {
	return publicDns;
    }

    public void setPublicDns(List<Dns> publicDns) {
	this.publicDns = publicDns;
    }

    public List<Owner> getOwners() {
	return owners;
    }

    public void setOwners(List<Owner> owners) {
	this.owners = owners;
    }

    public List<Network> getPublicNetworks() {
	return PublicNetworks;
    }

    public void setPublicNetworks(List<Network> publicNetworks) {
	PublicNetworks = publicNetworks;
    }
}
