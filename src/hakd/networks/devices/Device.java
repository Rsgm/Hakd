package hakd.networks.devices;

import hakd.internet.Connectable;
import hakd.internet.Connection;
import hakd.internet.Connection.Protocol;
import hakd.networks.Network;
import hakd.networks.devices.parts.Cpu;
import hakd.networks.devices.parts.File;
import hakd.networks.devices.parts.Gpu;
import hakd.networks.devices.parts.Memory;
import hakd.networks.devices.parts.Part;
import hakd.networks.devices.parts.Storage;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;

public class Device implements Connectable {

	// stats
	private final Network			network;
	private final int				level;

	// private final int webserver = 0; // 0 = 404, if port 80 is open
	private final ArrayList<String>	ports	= new ArrayList<String>();	// port,
// program / if its closed just
// delete it
	private File					logs;								// TODO make this a file instead //
// connecting from and the action after that

	private String					brand;								// for
// example bell
	private String					model;

	// objects
	private int						cpuSockets;						// easier
// than using a for loop to
// count the amount, just remember to
																		// change
// this port
	private int						memorySlots;						// maybe
// have a maximum part number,
// so you can specialize a server
	private int						storageSlots;
	private int						gpuSlots;

	// objects
	private final ArrayList<Part>	parts	= new ArrayList<Part>();

	// --------constructor--------
	public Device(Network network) {
		this.network = network;
// serverId = NetworkController.getNetworks().get(networkId).getServers().size(); // are these needed?
// networkId = network.getNetworkId();
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
		for (int i = 0; i < cpuSockets; i++) {
			parts.add(new Cpu(level, network, this));
		}
		for (int i = 0; i < gpuSlots; i++) {
			parts.add(new Gpu(level, network, this));
		}
		for (int i = 0; i < memorySlots; i++) {
			parts.add(new Memory(level, network, this));
		}
		for (int i = 0; i < storageSlots; i++) {
			parts.add(new Storage(level, network, this));
		}

	}

	// --------methods--------

	@Override
	public boolean Connect(Device client, String program, int port) {
		Connection c = new Connection(this, network, Protocol.getProtocol(port));

		if (ports.get(ports.indexOf(port) + 1).equals(program)) {
			try {
				switch (port) {
					default: // 80, 443, others but just get directed to a 404 if not a website
						Desktop.getDesktop().browse(java.net.URI.create("http://localhost:80/network/" + network.getIp()));
					case 31337:
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

	@Override
	public boolean Disconnect(Device device, String program, int port) {

		return false;
	}

	@Override
	public boolean addPorts(int port, String program, Device device) {
		if (ports.contains(ports) == false) {

			ports.add(port + "");
			ports.add(program);
			return true;
		}
		return false;
	}

	@Override
	public boolean removePort(int port, String program, Device device) {
		int index = ports.indexOf(port);
		if (ports.contains(port)) {
			ports.remove(index + 1);
			ports.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public void log(Device host, Device client, String program, String port) {
		File log = new File(0, "Log - " + network.getIp(), "connecting to " + program + ":" + port, ".log");
		parts.indexOf(null); // TODO figure out how to search the parts array, try collections
	}

	// --------getters/setters--------
	public File getLogs() {
		return logs;
	}

	public void setLogs(File logs) {
		this.logs = logs;
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

	public int getCpuSockets() {
		return cpuSockets;
	}

	public void setCpuSockets(int cpuSockets) {
		this.cpuSockets = cpuSockets;
	}

	public int getMemorySlots() {
		return memorySlots;
	}

	public void setMemorySlots(int memorySlots) {
		this.memorySlots = memorySlots;
	}

	public int getStorageSlots() {
		return storageSlots;
	}

	public void setStorageSlots(int storageSlots) {
		this.storageSlots = storageSlots;
	}

	public int getGpuSlots() {
		return gpuSlots;
	}

	public void setGpuSlots(int gpuSlots) {
		this.gpuSlots = gpuSlots;
	}

	public Network getNetwork() {
		return network;
	}

	public int getLevel() {
		return level;
	}

	public ArrayList<String> getPorts() {
		return ports;
	}

	public ArrayList<Part> getParts() {
		return parts;
	}
}