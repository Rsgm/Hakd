package hakd.networks.devices;

import hakd.networks.Network;

import java.util.ArrayList;
import java.util.List;

public final class Dns extends Device { // TODO let DNSs communicate a bit.
    private List<Network> hosts = new ArrayList<Network>();

    public Dns(Boolean publicDns, Network network, int level) {
	super(network, level, DeviceType.DNS);
    }

    public Dns(Network network, int level, DeviceType type, int cpuSockets,
	    int gpuSlots, int memorySlots, int storageSlots) {
	super(network, level, type, cpuSockets, gpuSlots, memorySlots,
		storageSlots);
    }

    // --------methods--------
    public String assignIp(Network.Region region) { // assigns an ip to an
						    // object that
	// requests one, also checks it and
	// adds it to the dns list
	String ip;
	boolean taken;

	do {
	    ip = generateIp(region);
	    taken = false;

	    for (Network n : hosts) {
		if (n.getIp() != null && n.getIp().equals(ip)) {
		    taken = true;
		}
	    }
	} while (taken == true); // better practice to do this rather than
				 // while(true); and break;
	return ip;
    }

    // used to create a realistic, random ip based on registered ipv4 IRL(or AFK
    // if your from sweeden). used mostly with the assign ip method, but can
    // be useful for other things
    public static String generateIp(Network.Region region) {
	switch (region) {
	default:
	    return (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256);
	case COMPANIES:
	    return (int) (Math.random() * 50 + 6) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256);
	case NA:
	    return (int) (Math.random() * 13 + 63) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256);
	case EUROPE:
	    return (int) (Math.random() * 15 + 77) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256) + "."
		    + (int) (Math.random() * 256);
	}
    }

    // registers a url to an ip just so not everything is an ip // ip can only
    // be a player's ip if they buy it
    public boolean addUrl(String ip, String address) {
	if (address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$")
		&& network.getInternet().getIp(address) == null) { // address
								   // regex
	    findNetwork(ip).setAddress(address);
	    return true; // "you have successfully registered the url " + url +
			 // " for the ip " + ip;
	}
	return false; // "Sorry, either that URL is already registered, or a bug)."
    }

    // returns the network with the given address or ip
    public Network findNetwork(String address) {
	if (address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$")) {
	    address = network.getInternet().getIp(address);
	}

	for (Network n : hosts) {
	    if (n.getIp().equals(address)) {
		return n;
	    }
	}
	return null;
    }

    // gets the ip of the given address
    public String getIp(String address) {
	for (Network n : hosts) {
	    if (n.getAddress().equals(address)) {
		return n.getIp();
	    }
	}
	return null;
    }

    int getRandomNumber() {
	return 4; // chosen by fair dice roll
		  // guaranteed to be random
    }

    // --------getters/setters--------
    public List<Network> getHosts() {
	return hosts;
    }

    public void setHosts(List<Network> hosts) {
	this.hosts = hosts;
    }
}
