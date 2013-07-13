package hakd.networks.devices;

import hakd.networks.Network;

public class Server extends Device {

    public Server(Network n, int level) {
	super(n, level, DeviceType.SERVER);

    }
}