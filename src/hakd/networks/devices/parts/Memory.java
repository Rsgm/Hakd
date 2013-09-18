package hakd.networks.devices.parts;

import hakd.networks.devices.Device;

public final class Memory extends Part {
	private int capacity;

	public Memory(int level, Device device) {
		super(level, device);
		type = PartType.MEMORY;

		switch(level) {
			case 0:
				capacity = 1 + (int) (Math.random() * 3 - 3);
				speed = (int) ((Math.random() * 2 + 1) * 350 + (Math.random() * 5 - 2) * 33.33); // an
				// average
				// of
				// 100MHz
				// memory
				// and
				// 2800MHz
				break;
			default:
				capacity = (int) Math.pow(2, (level + 1) / 2 + ((int) (Math.random() * 3 + 1) - 2));
				speed = (int) ((Math.random() * 3) * 350 * (level + 1) + (Math.random() * 5 - 2) * 33.33);
				if(speed == 0) {
					speed = (level - 1) * 400;
				}
				break;
		}
	}

	public Memory(Device device, int level, int speed, int capacity) {
		super(level, device);

		this.speed = speed;
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
