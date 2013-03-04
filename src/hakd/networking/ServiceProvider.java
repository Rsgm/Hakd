package hakd.networking;

import hakd.networking.devices.Dns;

public class ServiceProvider extends Network {

	public ServiceProvider() {
		super(Network.Types.ISP);

		getDevices().add(new Dns(true)); // give each isp a dns server
	}
}