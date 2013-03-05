package hakd.networks.devices.parts;

import hakd.networks.Network;
import hakd.networks.devices.Device;

public class Gpu extends Part {

	public Gpu(int level, Network network, Device device) {
		super(level, network, device);

		switch (level) {
			case 0:
				setSpeed((int) (Math.random() * 200 + 100));
				break;
			default:
				setSpeed((level + 1) * 150 + (int) (Math.random() * 400 - 200));
				break;
		}
	}
}
