package hakd.networks.devices.parts;

import hakd.networks.Network;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;

public class Part {

    // stats
    Network network;
    Device device;
    PartType type;

    int level;
    int speed; // either MHz or MB/s(megabyte/s, not megabit/s)
	       // depending on the part // cpu also has core modifier
	       // speed = speed
    // (1.8*cores)

    String brand;
    String model;

    public Part(int level, Device device) {
	this.level = level;
	this.network = device.getNetwork();
	this.device = device;
    }

    // finds all of the parts in the list of that type
    public static List<Part> findParts(List<Part> parts, PartType type) {
	List<Part> returnParts = new ArrayList<Part>();
	for (Part p : parts) {
	    if (p.getType() == type) {
		returnParts.add(p);
	    }
	}
	return returnParts;
    }

    public enum PartType {
	PART(), CPU(), GPU(), MEMORY(), STORAGE; // more to come

	private PartType() {
	}
    }

    public enum Brand {
	INTELLIGENCE("Intelligence");
	public String brand;

	Brand(String brand) {
	    this.brand = brand;
	}
    }

    public enum Model {

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
