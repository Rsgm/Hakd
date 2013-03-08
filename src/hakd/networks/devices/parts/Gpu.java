package hakd.networks.devices.parts;

import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.other.enumerations.PartType;

public class Gpu extends Part {

	public Gpu(int level, Network network, Device device) {
		super(level, network, device);
		setType(PartType.GPU);

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
