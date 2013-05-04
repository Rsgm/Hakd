package hakd.networks.devices;

import hakd.networks.Network;
import hakd.other.Port;
import hakd.other.enumerations.DeviceType;
import hakd.other.enumerations.Protocol;

public class Router extends Device { // this holds a port list that tells programs where to look for a server/port

	public Router(Network network, int level) {
		super(network, level, DeviceType.ROUTER);
	}

	@Override
	public boolean Connect(Device client, String program, int port, Protocol protocol) {
		if (Port.checkPortAnd(getPorts(), program, port, protocol)) {
			return Port.getPort(getPorts(), null, port).getDevice().Connect(client, program, port, protocol);
		}
		return false;
	}
}
// I don't want to change the name, but this is really a modem. Though it could be a router under a boarder definition.