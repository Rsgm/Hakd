package hakd.networks.devices;

import hakd.networks.Network;
import hakd.other.Port;
import hakd.other.enumerations.Protocol;

public class Router extends Device { // this holds a port list that tells programs where to look for a server/port

	public Router(Network network) {
		super(network);
	}

	@Override
	public boolean Connect(Device client, String program, int port, Protocol protocol) {
		if (Port.checkPortAnd(getPorts(), program, port, protocol)) {
			return Port.getPort(getPorts(), null, port).getDevice().Connect(client, program, port, protocol);
		}
		return false;
	}
}
