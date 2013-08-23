package hakd.networks.devices;

import hakd.networks.Network;
import hakd.networks.ProviderNetwork;
import hakd.other.RouterNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Routers handle connections to other networks, namely ISPs and
 * backboneProviderNetworks, but can extend to large companies that span several
 * networks. This holds a portNumber list that tells programs where to look for
 * a server/portNumber. Note: This is really a modem and router combined into
 * one. Though it could be a router under a boarder definition.
 * */
public final class Router extends Device {
    private int speed; // in MB/s (megabytes per second)

    private ProviderNetwork parentNetwork; // parent network
    private Device parent; // parent device(dns usually)
    private List<Device> children = new ArrayList<Device>();

    private RouterNode node = new RouterNode(this);

    /** Creates a router with randomized values and parts. */
    public Router(Network network, int level) {
	super(network, 1/* level */, DeviceType.ROUTER);

	network.getInternet().getRouterNodes().add(node);
	parentRouter = this;

	children.addAll(network.getDnss());
	children.addAll(network.getServers());
	children.addAll(network.getOtherDevices());
	children.add(this);
    }

    /** Creates a router with the specified values. */
    public Router(Network network, int level, DeviceType type, int cpuSockets,
	    int gpuSlots, int memorySlots, int storageSlots) {
	super(network, level, type, cpuSockets, gpuSlots, memorySlots,
		storageSlots);

	network.getInternet().getRouterNodes().add(node);

	children.addAll(network.getDnss());
	children.addAll(network.getServers());
	children.addAll(network.getOtherDevices());
	children.add(this);
    }

    /** Registers a device on the netowrk as a child of this router. */
    public short[] register(Device d) {
	short ip[] = assignIp();

	if (ip == null) {
	    return null;
	}

	d.setIp(ip);
	d.setNetwork(network);
	d.setParentRouter(this);
	children.add(d);
	return ip;
    }

    /**
     * Removes a device from the router, as well as disposes it, removing any
     * connections.
     */
    public void unregister(Device d) {
	children.remove(d);
	network.getInternet().getRouterNodes().remove(node);
	d.dispose();
    }

    /**
     * Assigns an ip to an object that requests one, also checks it and adds it
     * to the dns list. Note: This will return null if there are 25
     */
    private short[] assignIp() {
	short[] ip = null;

	if (children.size() > 255) {
	    return null;
	}

	for (short i = 1; i < 256; i++) {
	    ip = new short[] { this.ip[0], this.ip[1], this.ip[2], i };
	    if (findDevice(ip) == null) {
		break;
	    }
	}
	return ip;
    }

    /** Finds the device with the given ip connected to the dns. */
    public Device findDevice(short[] ip) {
	for (Device d : children) {
	    if (Arrays.equals(ip, d.getIp())) {
		return d;
	    }
	}
	return null;
    }

    @Override
    public void dispose() {
	super.dispose();

	parentNetwork = null;
	parent = null;
	children = null;
    }

    public int getSpeed() {
	return speed;
    }

    public void setSpeed(int speed) {
	this.speed = speed;
    }

    public ProviderNetwork getParentNetwork() {
	return parentNetwork;
    }

    public void setParentNetwork(ProviderNetwork parentNetwork) {
	this.parentNetwork = parentNetwork;
    }

    public Device getParent() {
	return parent;
    }

    public void setParent(Device parent) {
	this.parent = parent;
    }

    public List<Device> getChildren() {
	return children;
    }

    public void setChildren(List<Device> children) {
	this.children = children;
    }

    public RouterNode getNode() {
	return node;
    }

    public void setNode(RouterNode node) {
	this.node = node;
    }
}
