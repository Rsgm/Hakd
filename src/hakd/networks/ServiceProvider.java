package hakd.networks;

import hakd.networking.NetworkController;
import hakd.networking.networks.devices.Dns;

public class ServiceProvider extends Network {
	private final Dns	dns;

	public ServiceProvider() {
		super(Network.Types.ISP);
		dns = new Dns(true, null);
		NetworkController.getPublicDns().add(dns);
		getDevices().add(dns); // give each isp a dns server
	}

	public static String register(Network network, int speed) {
		network.setSpeed(speed);
		
		return "";
	}
	
	public 
}