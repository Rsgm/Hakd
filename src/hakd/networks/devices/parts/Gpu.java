package hakd.networks.devices.parts;

import hakd.networks.devices.Device;

public class Gpu extends Part {

    public Gpu(int level, Device device) {
	super(level, device);
	type = PartType.GPU;

	switch (level) {
	case 0:
	    speed = (int) (Math.random() * 200 + 100);
	    break;
	default:
	    speed = (level + 1) * 150 + (int) (Math.random() * 400 - 200);
	    break;
	}
    }

    public Gpu(Device device, int level, int speed, int b) {
	super(level, device);

	this.speed = speed;
    }
}
