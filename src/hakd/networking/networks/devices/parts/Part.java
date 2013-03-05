package hakd.networking.networks.devices.parts;

import hakd.networking.networks.Network;
import hakd.networking.networks.devices.Device;

public class Part {

	// stats
	private Network	network;
	private Device	device;

	private int		level;
	private int		speed;		// either MHz or Mbps(megabyte, not bit) depending on the part // cpu also has core modifier speed = speed
// (1.8*cores)

	private String	brand;
	private String	model;

	public Part(int level, Network network, Device device) {
		this.level = level;
		this.network = network;
		this.device = device;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
