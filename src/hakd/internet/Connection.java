package hakd.internet;

import hakd.internet.NetworkController.Protocol;
import hakd.networks.devices.Device;

public final class Connection { // connections are one way, and one connection to one
			  // port // for two way connections just use two
			  // connections

    private Device host;
    private Device client;
    private String hostIp;
    private String clientIp;

    // other info
    private int speed;

    // private// --------constructor--------
    public Connection(Device host, Device client, Protocol protocol) {
	this.host = host;
	this.client = client;

	if (host.getNetwork().getSpeed() >= client.getNetwork().getSpeed()) { // slowest
									      // speed
									      // goes
	    speed = client.getNetwork().getSpeed();
	} else {
	    speed = host.getNetwork().getSpeed();
	}

    }

    // --------methods-------- // TODO make methods for sending data and secure
    // data from host to client // does it need a buffer?
    public void close() {
	host.getConnections().remove(this);
	client.getConnections().remove(this);
    }

    // --------getters/setters--------
    public Device getHost() {
	return host;
    }

    public void setHost(Device host) {
	this.host = host;
    }

    public Device getClient() {
	return client;
    }

    public void setClient(Device client) {
	this.client = client;
    }

    public String getHostIp() {
	return hostIp;
    }

    public void setHostIp(String hostIp) {
	this.hostIp = hostIp;
    }

    public String getClientIp() {
	return clientIp;
    }

    public void setClientIp(String clientIp) {
	this.clientIp = clientIp;
    }

    public int getSpeed() {
	return speed;
    }

    public void setSpeed(int speed) {
	this.speed = speed;
    }
}
