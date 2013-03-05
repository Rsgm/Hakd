package hakd.networks.devices.parts;

import hakd.networks.Network;
import hakd.networks.devices.Device;

public class Cpu extends Part {
	private int	cores;	// core modifier speed = speed (1.8*cores) // in MHz, 3.5GHz -> 3500MHz

	public Cpu(int level, Network network, Device device) {
		super(level, network, device);

		switch (level) {
			case 0:
				setSpeed(((int) (Math.random() * 5 + 1)) * 105 + 100);
				cores = 1;
				break;
			default:
				setSpeed((level + 1) * 625 + (((int) (Math.random() * 400 + 1)) * 5 - 1000));
				if (level >= 4) {
					cores = (int) Math.pow(2, (level - 3) - ((int) (Math.random() * 2)));
				} else {
					cores = 1;
				}
				break;
		}
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}
}