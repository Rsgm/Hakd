package hakd.networks;

import hakd.networks.devices.Device;
import hakd.networks.devices.Dns;
import hakd.other.enumerations.DeviceType;
import hakd.other.enumerations.NetworkType;
import hakd.other.enumerations.names.Isp;

public class ServiceProvider extends Network {
	private Dns	masterDns;

	@SuppressWarnings("deprecation")
	public ServiceProvider(Isp owner) { // these can be used like normal, but see network
		super(NetworkType.ISP); // this is ok

		setOwner(owner.company);
		masterDns = (Dns) Device.findDevices(getDevices(), DeviceType.DNS).get(0);
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