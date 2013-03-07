package hakd.networks.devices;

import hakd.internet.Connectable;
import hakd.internet.Connection;
import hakd.networks.Network;
import hakd.networks.devices.parts.Cpu;
import hakd.networks.devices.parts.Gpu;
import hakd.networks.devices.parts.Memory;
import hakd.networks.devices.parts.Part;
import hakd.networks.devices.parts.Storage;
import hakd.other.File;
import hakd.other.Port;
import hakd.other.enumerations.DeviceType;
import hakd.other.enumerations.Protocol;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;

public class Device implements Connectable {

	// stats
	private Network					network;
	private int						level;
	// private int webserver = 0; // 0 = 404, if port 80 is open
	private ArrayList<Port>			ports		= new ArrayList<Port>();		// port, program / if its closed just delete it
	private ArrayList<Connection>	connections	= new ArrayList<Connection>();
	private File					logs;										// TODO make this a file instead connecting from and the action after
// that

	private String					brand;										// for example bell, or HQ
	private String					model;

	// objects
	private int						cpuSockets;								// easier than using a for loop to count the amount, just remember to
// change this port
	private int						memorySlots;								// maybe have a maximum part number, so you can specialize a server
	private int						storageSlots;
	private int						gpuSlots;
	private Storage					masterStorage;								// TODO where the os resides
	private DeviceType				type;

	// objects
	private ArrayList<Part>			parts		= new ArrayList<Part>();

	// --------constructor--------
	public Device(Network network) { // have random smartphone connections and disconnections
		this.network = network; // smartphones are like insects on a network, many types, random behavior, and there are lots of them
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
	public boolean Connect(Device client, String program, int port, Protocol protocol) { // TODO work on connections and these
		Connection c = new Connection(this, client, Protocol.getProtocol(port));
		connections.add(c);

		if (Port.checkPortAnd(ports, program, port, protocol)) {
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
	public boolean addPorts(Device device, String program, int port, Protocol protocol) {
		if (Port.checkPortOr(ports, null, null, port, null) == false) {
			ports.add(new Port(null, program, port, protocol));
			return true;
		}
		return false;
	}

	@Override
	public boolean removePort(int port) { // this may be a memory leak where devices can't remove the ports they bind when they are removed
		return ports.remove(Port.getPort(ports, null, port));
	}

	@Override
	public void log(Device client, String program, int port, Protocol protocol) {
		File log =
				new File(0, "Log - " + client.getNetwork().getIp(), "Connecting with " + program + " through port" + port + " using " + protocol
						+ "\n" + program + ":" + port + ">" + protocol, ".log");
		parts.indexOf(null); // TODO figure out how to search the parts array, try collections
		/* ---Example---
		 * Log - 243.15.66.24
		 * Connecting with half life 3 through port 28190 using LAMBDA
		 * half life 3:28190>LAMBDA
		 * */
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

	public ArrayList<Port> getPorts() {
		return ports;
	}

	public ArrayList<Part> getParts() {
		return parts;
	}

	public Storage getMasterStorage() {
		return masterStorage;
	}

	public void setMasterStorage(Storage masterStorage) {
		this.masterStorage = masterStorage;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setPorts(ArrayList<Port> ports) {
		this.ports = ports;
	}

	public void setParts(ArrayList<Part> parts) {
		this.parts = parts;
	}

	public ArrayList<Connection> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}
}
