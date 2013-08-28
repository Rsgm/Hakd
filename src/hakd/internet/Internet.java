package hakd.internet;

import hakd.networks.BackboneProviderNetwork;
import hakd.networks.DefaultNetwork;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;
import hakd.networks.Network.NetworkType;
import hakd.networks.devices.Device;
import hakd.networks.devices.Device.DeviceType;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;

import java.util.ArrayList;
import java.util.List;

import ai.pathfinder.Node;

public final class Internet {
    /** Contains all networks with an ISP as a parent. */
    private final List<Network> PublicNetworks;

    /** Contains all ISPs. Note: ISPs can only be public. */
    private final List<InternetProviderNetwork> internetProviderNetworks;

    /**
     * Contains all Backbone networks. Note: Backbone networks can only be
     * public.
     */
    private final List<BackboneProviderNetwork> backboneProviderNetworks;

    /** Contains all Dns servers belonging to ISPs and Backbone networks. */
    private final List<Dns> publicDns;

    /**
     * Contains all router nodes. This should be updated about once per 3-5
     * seconds. Also update the routes of connections at the same time.
     */
    private final List<Node> routerNodes;

    /** This is only created at the start of the game. */
    public Internet() {
	int backbones = 4;// (int) (Math.random() * 3 +
			  // IpRegion.values().length);
	int isps = /* 24 */(int) (Math.random() * 8 + 8);
	int networks = (int) (Math.random() * 15 + 60);

	backboneProviderNetworks = new ArrayList<BackboneProviderNetwork>(isps);
	internetProviderNetworks = new ArrayList<InternetProviderNetwork>(
		backbones);
	PublicNetworks = new ArrayList<Network>(networks);
	publicDns = new ArrayList<Dns>(isps + backbones);
	routerNodes = new ArrayList<Node>(isps + backbones + networks);

	generateBackbones(backbones);
	generateIsps(isps);
	generateNetworks(networks);
    }

    private void generateBackbones(int amount) {
	// each ipRegion gets at least one backbone, possibly several
	for (int i = 0; i < amount; i++) {
	    @SuppressWarnings("deprecation")
	    BackboneProviderNetwork b = new BackboneProviderNetwork(this);

	    publicDns.add(b.getMasterDns());
	    backboneProviderNetworks.add(b);
	    PublicNetworks.add(b); // this may not be the best but lets see what
	    // happens
	}
    }

    private void generateIsps(int amount) {
	for (int i = 0; i < amount; i++) {
	    @SuppressWarnings("deprecation")
	    InternetProviderNetwork s = new InternetProviderNetwork(this);

	    publicDns.add(s.getMasterDns());
	    internetProviderNetworks.add(s);
	    PublicNetworks.add(s); // this may not be the best but lets see what
	    // happens
	}
    }

    /** creates the initial game networks */
    private void generateNetworks(int amount) {
	for (int i = 0; i < amount; i++) {
	    int test = (int) (Math.random() * 10);
	    if (test < 7) { // chances of generating a certain network type
		NewPublicNetwork(NetworkType.NPC);
	    } else if (test >= 6) {
		NewPublicNetwork(NetworkType.BUSINESS);
		// } else if (PyTest == 9) {
		// NewPublicNetwork(NetworkType.TEST);
	    }
	}
    }

    /** Returns a list of networks of the given type. */
    public List<Network> getNetworkByType(NetworkType type) {
	List<Network> array = new ArrayList<Network>();
	for (Network n : PublicNetworks) {
	    if (n.getType() == type) {
		array.add(n);
	    }
	}
	return array;
    }

    /** Searches for the router with the given ip on the public internet. */
    public Router findRouter(short[] ip) {

	for (InternetProviderNetwork i : internetProviderNetworks) {
	    if (i.getMasterDns().getIp()[1] == ip[1]) {
		for (Connection c : i.getMasterDns().getConnections()) {
		    if (c.getClient().getIp()[2] == ip[2]
			    && c.getClient().getType() == DeviceType.ROUTER) {
			return (Router) c.getClient();
		    }
		}
	    }
	}
	return null;
    }

    /** Searches for the device with the given ip on the public internet. */
    public Device findDevice(short[] ip) {

	for (InternetProviderNetwork i : internetProviderNetworks) {
	    if (i.getMasterDns().getIp()[1] == ip[1]) {
		for (Connection c : i.getMasterDns().getConnections()) {
		    if (c.getClient().getIp()[2] == ip[2]
			    && c.getClient().getType() == DeviceType.ROUTER) {
			for (Device d : ((Router) c.getClient()).getChildren()) {
			    if (d.getIp()[3] == ip[3]) {
				return d;
			    }
			}
		    }
		}
	    }
	}
	return null;
    }

    /** Public dns ip request, gets the ip of a server at that address. */
    public short[] getIp(String address) {
	for (Dns d : publicDns) {
	    short[] ip = d.getIp(address);
	    if (ip != null) { // why is this needed? removing it is probably not
		// worth what ever it causes
		return ip; // oh, this is needed so it doesn't return if the
		// first dns does not have it
	    }
	}
	return null;
    }

    /** Creates a new network and adds it to the public network list. */
    public Network NewPublicNetwork(NetworkType type) {
	Network n = addNetwork(type);
	PublicNetworks.add(n);
	return n;
    }

    /**
     * Removes references to a network from all public DNSs(there may not be
     * any) and the network list. You can either leave it up to the GC to
     * remove, or use the network privately.
     */
    public void removePublicNetwork(Network network) {
	for (Dns d : publicDns) {
	    d.getConnections().remove(network);
	}
	PublicNetworks.remove(network);
	// TODO remove graphical data, or put that in a better spot to be more
	// modular
    }

    /**
     * Creates a network, needed because you can't add(this) in a constructor,
     * strange.
     */
    @SuppressWarnings("deprecation")
    public Network addNetwork(NetworkType type) {
	Network n;

	switch (type) {
	default:
	    n = new DefaultNetwork(type, this);
	    break;
	case BACKBONE:
	    n = new BackboneProviderNetwork(this);
	    break;
	case ISP:
	    n = new InternetProviderNetwork(this);
	}
	return n;
    }

    public static String ipToString(short[] ip) {
	return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3];
    }

    public enum Protocol {
	FTP(21), SSH(22), SMTP(25), WHOIS(43), DNS(53), HTTP(80), HTTPS(443), STEAM(
		1725), XBOX(3074), MYSQL(3306), RDP(3389), WOW(3724), UPUP(5000), IRC(
		6667), TORRENT(6881), LAMBDA(27015), COD(28960), LEET(31337);

	public int portNumber; // these are only default ports

	private Protocol(int portNumber) {
	    this.portNumber = portNumber;
	}

	public static Protocol getProtocol(int port) {
	    for (Protocol p : Protocol.values()) {
		if (p.portNumber == port) {
		    return p;
		}
	    }
	    return HTTP;
	}
    }

    public List<Network> getPublicNetworks() {
	return PublicNetworks;
    }

    public List<Dns> getPublicDns() {
	return publicDns;
    }

    public List<InternetProviderNetwork> getInternetProviderNetworks() {
	return internetProviderNetworks;
    }

    public List<BackboneProviderNetwork> getBackboneProviderNetworks() {
	return backboneProviderNetworks;
    }

    public List<Node> getRouterNodes() {
	return routerNodes;
    }
}
