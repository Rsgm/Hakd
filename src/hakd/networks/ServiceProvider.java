package hakd.networks;

import hakd.internet.NetworkController;
import hakd.networks.devices.Dns;
import hakd.other.enumerations.NetworkType;
import hakd.other.enumerations.names.Isp;

public class ServiceProvider extends Network {
	private Dns	dns;

	@SuppressWarnings("deprecation")
	public ServiceProvider(Isp owner) { // these can be used like normal, but see network
		super(NetworkType.ISP); // this is ok

		setOwner(owner.name());
		Dns dns = new Dns(true, null);
		NetworkController.getPublicDns().add(dns);
		getDevices().add(dns); // give each isp a dns server
	}

	public String register(Network network, int speed) { // TODO
		network.setSpeed(speed);
		dns.generateIp(network.getRegion());
		return "";
	}

	public Dns getDns() {
		return dns;
	}

	public void setDns(Dns dns) {
		this.dns = dns;
	}
}