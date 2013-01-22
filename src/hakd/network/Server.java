package hakd.network;

import hakd.network.serverparts.Cpu;
import hakd.network.serverparts.Gpu;
import hakd.network.serverparts.Memory;
import hakd.network.serverparts.Storage;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;

public class Server {

	// stats
	private Network network;
	private int						networkId;								// which network a server belongs to
	private int serverId; // server id starts at 0 in the gui as well
	private int						level;
	// private final int webserver = 0; // 0 = 404
	private final ArrayList<String>	ports	= new ArrayList<String>();	// port, program / if its closed just delete it
	private final ArrayList<String>	logs	= new ArrayList<String>();	// TODO make this a file instead // connecting from and the action after that

	private String					brand;									// for example bell
	private String					model;

	// objects
	private int						cpuSockets;							// easier than using a for loop to count the amount, just remember to
	// change this value
	private int						memorySlots;
	private int						storageSlots;
	private int						gpuSlots;

	// objects
	private final ArrayList<Cpu>		cpu		= new ArrayList<Cpu>();
	private final ArrayList<Gpu>		gpu		= new ArrayList<Gpu>();
	private final ArrayList<Memory>	memory	= new ArrayList<Memory>();
	private final ArrayList<Storage>	storage	= new ArrayList<Storage>();

	// total stats? added, multiplied?

	// --------constructor--------
	public Server(Network n) {
		serverId = Network.getNetworks().get(networkId).getServers().size();
		network = n;
		networkId = n.getNetworkId();
		level = network.getLevel();

		switch (level) {
			case 0:
				cpuSockets = (int) (Math.random() * 2 + 1);
				gpuSlots = 1; // TODO server part generation code
				memorySlots = 1;
				storageSlots = 1;
				
				break;
			case 7:
				cpuSockets = (int) (Math.random() * 2 + 7);
				gpuSlots = 1;
				memorySlots = 1;
				storageSlots = 1;
				break;
			default:
				cpuSockets = (int) (Math.random() * 3 + level);
				gpuSlots = 1;
				memorySlots = 1;
				storageSlots = 1;
				break;
		}
	}

	// --------methods--------
	public void populate(int id) {
		for (int i = 0; i < cpuSockets; i++) {
			cpu.add(new Cpu(level, networkId, serverId));
		}
		for (int i = 0; i < gpuSlots; i++) {
			gpu.add(new Gpu(level, networkId, serverId));
		}
		for (int i = 0; i < memorySlots; i++) {
			memory.add(new Memory(level, networkId, serverId));
		}
		for (int i = 0; i < storageSlots; i++) {
			storage.add(new Storage(level, networkId, serverId));
		}
		return;
	}

	boolean connect(String address, String program, String port) {
		logs.add(address);
		logs.add("connected " + program + " " + port);
		if (ports.get(ports.indexOf(port) + 1).equals(program)) {
			try {
				switch (port) {
					default: // 80, 443, others but just get directed to a 404 if not a website
						Desktop.getDesktop().browse(java.net.URI.create("http://localhost:80/network/" + address));
					case "31337":
						// grant complete(root?) access
						// open ssh
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;

	}

	public boolean addPorts(int port, String program) {
		if (ports.contains(ports) == false) {

			ports.add(port + "");
			ports.add(program);
			return true;
		}
		return false;
	}

	public boolean removePort(int port) {
		int index = ports.indexOf(port);
		if (ports.contains(port)) {
			ports.remove(index + 1);
			ports.remove(index);
			return true;
		}
		return false;
	}

	// --------getters/setters--------
	public int getNetwork() {
		return networkId;
	}

	public void setNetwork(int network) {
		this.networkId = network;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<String> getLogs() {
		return logs;
	}

	public int getStorageSlots() {
		return storageSlots;
	}

	public void setStorageSlots(int storageSlots) {
		this.storageSlots = storageSlots;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}