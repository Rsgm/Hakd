package hakd.networks.devices.parts;

import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.other.enumerations.PartType;

import java.util.ArrayList;

public class Part {

	// stats
	private Network		network;
	private Device		device;
	private PartType	type;

	private int			level;
	private int			speed;		// either MHz or MB/s(megabyte/s, not megabit/s) depending on the part // cpu also has core modifier speed = speed
// (1.8*cores)

	private String		brand;
	private String		model;

	public Part(int level, Network network, Device device) {
		this.level = level;
		this.network = network;
		this.device = device;
	}

	public static ArrayList<Storage> findParts(ArrayList<Part> parts, PartType type) {
		ArrayList<Storage> returnParts = new ArrayList<Storage>();
		for (Part p : parts) {
			if (p.getType() == type) {
				returnParts.add((Storage) p);
			}
		}
		return returnParts;
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

	public PartType getType() {
		return type;
	}

	public void setType(PartType type) {
		this.type = type;
	}
}
