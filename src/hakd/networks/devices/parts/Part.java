package hakd.networks.devices.parts;

import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;

public class Part {
	PartType type;
	Device device;
	int level;

	String brand;
	String model;

	public Part() {
		type = PartType.PART;
	}

	// finds all of the parts in the list of that type
	public static List<Part> findParts(List<Part> parts, PartType type) {
		List<Part> returnParts = new ArrayList<Part>();
		for(Part p : parts) {
			if(p.getType() == type) {
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
		public final String brand;

		Brand(String brand) {
			this.brand = brand;
		}
	}

	public enum Model {

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
}
