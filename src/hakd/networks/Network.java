package hakd.networks;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import hakd.game.Internet;
import hakd.game.gameplay.Player;
import hakd.gui.EmptyDeviceTile;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A network represents a collection of devices.
 */
public class Network {
	short[] ip = new short[4];
	int level; // 0-7, 0 for player because you start with almost nothing
	String owner; // owner, company, player
	Player player;
	Stance stance; // TODO move this to npc class
	NetworkType type;

	// provider connection info
	int speed; // in MB/s (megabytes per second)
	Network parent; // parent network

	// children device
	final List<Device> devices = new ArrayList<Device>();
	int deviceLimit; // The maximum allowable devices on the network, also the amount to generate is based on this value. This must be less than 255
	List<EmptyDeviceTile> EmptyDeviceTiles;

	// gui stuff
	Model sphere;
	ModelInstance sphereInstance;
	Vector3 spherePosition;
	Model parentConnectionLine;
	ModelInstance parentConnectionInstance;

	public static final float worldSize = 500;
	public static final float BackboneRegionSize = 150;
	public static final float ispRegionSize = 80;
	public static final float networkRegionSize = 100;
	IpRegion ipRegion; // where the network is in the world, it helps find an ip

	Internet internet;

	public Network() {
	}

	/**
	 * Removes a device from the network, as well as disposes it, removing any
	 * connections.
	 */
	public void removeDevice(Device d) {
		devices.remove(d);
		d.dispose();
	}

	/**
	 * Registers a device on the network.
	 */
	public boolean addDevice(Device device) {
		short ip[] = assignIp();

		if(devices.size() >= deviceLimit || ip == null) {
			return false;
		}

		devices.add(device);

		device.setIp(ip);
		device.setNetwork(this);
		return true;
	}

	/**
	 * Assigns an ip to an object that requests one, also checks it and adds it
	 * to the dns list. Note: This will return null if there are 25
	 */
	private short[] assignIp() {
		short[] ip = null;

		if(devices.size() > 255) {
			return null;
		}

		for(short i = 1; i < 256; i++) {
			ip = new short[]{this.ip[0], this.ip[1], this.ip[2], i};
			if(getDevice(ip) == null) {
				break;
			}
		}
		return ip;
	}

	/**
	 * Finds the device with the given ip connected to the dns.
	 */
	public Device getDevice(short[] ip) {
		for(Device d : devices) {
			if(Arrays.equals(ip, d.getIp())) {
				return d;
			}
		}
		return null;
	}

	/**
	 * These define how to generate a network.
	 */
	public enum NetworkType { // TODO choose a random number between 0 and the probabilities added up, then go through a loop to check which to use
		PLAYER(0, 0), BUSINESS(1, 10), TEST(1, 1), ISP(1, 1), NPC(20, 100), BACKBONE(1, 1), EDUCATION(1, 1), BANK(1, 1),
		MILITARY(1, 1), GOVERNMENT(1, 1),
		RESEARCH(1, 1); // set to 0,0 to never be used in random generation

		public final int probabilityMin; // 0 to 1000
		public final int probabilityMax;

		NetworkType(int probabilityMin, int probabilityMax) {
			this.probabilityMin = probabilityMin;
			this.probabilityMax = probabilityMax;
		}
	}

	public enum Stance {
		FRIENDLY(), NEUTRAL(), ENEMY(); // TODO do I want to give the network or the npc a stance?

		Stance() {
		}
	}

	public enum IpRegion {
		BUSINESS(1, 56), SA(57, 62), NA(63, 76), EUROPE(77, 91), ASIA(92, 114), AFRICA(115, 126), PRIVATE(128, 172),
		EDUCATION(173, 182), GOVERNMENT(214, 220), MILITARY(220, 255), none(1, 255);

		public final int min; // min backbone ip range
		public final int max; // max ip range

		IpRegion(int min, int max) {
			this.min = min;
			this.max = max;
		}
	}

	public enum Owner {
		COMPANY("Company"), TEST("Test"); // these will be replaced with better
		// ones

		public final String company;

		Owner(String company) {
			this.company = company;
		}
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public Stance getStance() {
		return stance;
	}


	public void setStance(Stance stance) {
		this.stance = stance;
	}


	public IpRegion getRegion() {
		return ipRegion;
	}


	public void setRegion(IpRegion ipRegion) {
		this.ipRegion = ipRegion;
	}

	public NetworkType getType() {
		return type;
	}


	public void setType(NetworkType type) {
		this.type = type;
	}


	public Player getPlayer() {
		return player;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}


	public Internet getInternet() {
		return internet;
	}


	public void setInternet(Internet internet) {
		this.internet = internet;
	}


	public Model getModel() {
		return sphere;
	}


	public void setModel(Model model) {
		this.sphere = model;
	}


	public Vector3 getSpherePosition() {
		return spherePosition;
	}


	public void setSpherePosition(Vector3 spherePosition) {
		this.spherePosition = spherePosition;
	}


	public Model getSphere() {
		return sphere;
	}


	public void setSphere(Model sphere) {
		this.sphere = sphere;
	}


	public ModelInstance getSphereInstance() {
		return sphereInstance;
	}


	public void setSphereInstance(ModelInstance sphereInstance) {
		this.sphereInstance = sphereInstance;
	}

	public IpRegion getIpRegion() {
		return ipRegion;
	}

	public void setIpRegion(IpRegion ipRegion) {
		this.ipRegion = ipRegion;
	}

	public short[] getIp() {
		return ip;
	}

	public void setIp(short[] ip) {
		this.ip = ip;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Network getParent() {
		return parent;
	}

	public void setParent(Network parent) {
		this.parent = parent;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public int getDeviceLimit() {
		return deviceLimit;
	}

	public void setDeviceLimit(int deviceLimit) {
		this.deviceLimit = deviceLimit;
	}

	public Model getParentConnectionLine() {
		return parentConnectionLine;
	}

	public void setParentConnectionLine(Model parentConnectionLine) {
		this.parentConnectionLine = parentConnectionLine;
	}

	public ModelInstance getParentConnectionInstance() {
		return parentConnectionInstance;
	}

	public void setParentConnectionInstance(ModelInstance parentConnectionInstance) {
		this.parentConnectionInstance = parentConnectionInstance;
	}

	public static float getWorldSize() {
		return worldSize;
	}

	public static float getBackboneRegionSize() {
		return BackboneRegionSize;
	}

	public static float getIspRegionSize() {
		return ispRegionSize;
	}

	public static float getNetworkRegionSize() {
		return networkRegionSize;
	}

	public List<EmptyDeviceTile> getEmptyDeviceTiles() {
		return EmptyDeviceTiles;
	}

	public void setEmptyDeviceTiles(List<EmptyDeviceTile> emptyDeviceTiles) {
		EmptyDeviceTiles = emptyDeviceTiles;
	}
}
