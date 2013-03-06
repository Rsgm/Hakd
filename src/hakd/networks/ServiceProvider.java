package hakd.networks;

import hakd.internet.NetworkController;
import hakd.networks.devices.Dns;
import other.enumerations.Types;

public class ServiceProvider extends Network {
	private Dns	dns;

	public ServiceProvider() {
		super(Types.ISP);
		Dns dns = new Dns(true, null);
		NetworkController.getPublicDns().add(dns);
		getDevices().add(dns); // give each isp a dns server
	}

	public static String register(Network network, int speed) {
		network.setSpeed(speed);

		return "";
	}

	public Dns getDns() {
		return dns;
	}

	public void setDns(Dns dns) {
		this.dns = dns;
	}
}