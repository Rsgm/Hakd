package hakd.networks;

import hakd.networks.devices.Dns;
import hakd.other.names.Isp;

public final class ServiceProvider extends Network {
    private Dns masterDns;

    @Deprecated
    public ServiceProvider(Isp owner) { // these can be used like normal, but
					// see network
	super(NetworkType.ISP); // this is ok

	this.owner = owner.company;
	masterDns = dnss.get(0);
    }

    public String register(Network network, int speed) { // TODO
	network.setSpeed(speed);
	masterDns.getHosts().add(network);
	return masterDns.assignIp(network.getRegion());
    }

    public Dns getDns() {
	return masterDns;
    }

    public void setDns(Dns dns) {
	this.masterDns = dns;
    }
}