package hakd.networks.devices;

import hakd.gui.windows.ServerWindow;
import hakd.internet.Connectable;
import hakd.internet.Connection;
import hakd.internet.NetworkController;
import hakd.internet.NetworkController.Protocol;
import hakd.internet.Port;
import hakd.networks.Network;
import hakd.networks.Network.NetworkType;
import hakd.networks.devices.parts.Cpu;
import hakd.networks.devices.parts.Gpu;
import hakd.networks.devices.parts.Memory;
import hakd.networks.devices.parts.Part;
import hakd.networks.devices.parts.Part.PartType;
import hakd.networks.devices.parts.Storage;
import hakd.other.File;
import hakd.other.File.FileType;
import hakd.other.names.Brand;
import hakd.other.names.Model;

import java.util.ArrayList;
import java.util.List;

public class Device implements Connectable {

    // stats
    private Network network;
    private int level;
    // private int webserver = 0; // 0 = 404, if port 80 is open
    private List<Port> ports = new ArrayList<Port>(); // port, program /
						      // if its
						      // closed just
						      // delete it
    private List<Connection> connections = new ArrayList<Connection>();
    private File logs; // TODO make this a file instead connecting from and the
		       // action after
    // that

    private Brand brand; // for example bell, or HQ
    private Model model;

    private int totalMemory; // in MB
    private int totalStorage; // in ???

    // objects
    private List<Part> parts = new ArrayList<Part>();
    private int cpuSockets; // easier than using a for loop to count the amount,
			    // just remember to
    // change this port
    private int memorySlots; // maybe have a maximum part number, so you can
			     // specialize a server
    private int storageSlots;
    private int gpuSlots;
    private Storage masterStorage; // TODO where the os resides
    private DeviceType type;

    // server gui
    private ServerWindow window;

    // --------constructor--------
    public Device(Network network, int level, DeviceType type) { // idea: have
								 // random
								 // smartphone
								 // connections
								 // and
								 // disconnections

	this.network = network; // idea: smartphones are like insects on a
				// network,
				// many types, random behavior, and there are
				// lots of them

	this.level = level;
	this.type = type;

	switch (level) {
	case -1:
	    cpuSockets = 0;
	    gpuSlots = 0;
	    memorySlots = 0;
	    storageSlots = 0;
	    break;
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
	    Cpu cpu = new Cpu(level, this);
	    parts.add(cpu);
	}
	for (int i = 0; i < gpuSlots; i++) {
	    Gpu gpu = new Gpu(level, this);
	    parts.add(gpu);
	}
	for (int i = 0; i < memorySlots; i++) {
	    Memory memory = new Memory(level, this);
	    parts.add(memory);
	    totalMemory += memory.getCapacity();
	}
	for (int i = 0; i < storageSlots; i++) {
	    Storage storage = new Storage(level, this);
	    parts.add(storage);
	    totalStorage += storage.getCapacity();
	}

	if (storageSlots > 0) {
	    masterStorage = (Storage) Part.findParts(parts, PartType.STORAGE)
		    .get(0);
	}

	if (network.getType() == NetworkType.PLAYER) {
	    window = new ServerWindow(this);
	}
    }

    public Device(Network network, int level, int cpuSockets, int gpuSlots,
	    int memorySlots, int storageSlots) {
	this.level = level;
	this.cpuSockets = cpuSockets;
	this.gpuSlots = gpuSlots;
	this.memorySlots = memorySlots;
	this.storageSlots = storageSlots;

	for (int i = 0; i < cpuSockets; i++) {
	    Cpu cpu = new Cpu(level, this);
	    parts.add(cpu);
	}
	for (int i = 0; i < gpuSlots; i++) {
	    Gpu gpu = new Gpu(level, this);
	    parts.add(gpu);
	}
	for (int i = 0; i < memorySlots; i++) {
	    Memory memory = new Memory(level, this);
	    parts.add(memory);
	    totalMemory += memory.getCapacity();
	}
	for (int i = 0; i < storageSlots; i++) {
	    Storage storage = new Storage(level, this);
	    parts.add(storage);
	    totalStorage += storage.getCapacity();
	}

	if (storageSlots > 0) {
	    masterStorage = (Storage) Part.findParts(parts, PartType.STORAGE)
		    .get(0);
	}

	if (network.getType() == NetworkType.PLAYER) {
	    window = new ServerWindow(this);
	}
    }

    // --------methods--------

    @Override
    public boolean Connect(Device client, String program, int port,
	    NetworkController.Protocol protocol) { // TODO this
	Connection c = new Connection(this, client, Protocol.getProtocol(port));
	connections.add(c);

	if (Port.checkPortAnd(ports, program, port, protocol)) {
	    // Desktop d = Desktop.getDesktop();

	    switch (port) {
	    default: // 80, 443, others but just get directed to a 404 if not a
		     // website
		     // d.browse(URI.create("http://localhost:80/network/" +
		     // network.getIp())); // I am not doing the html store any
		     // more
	    case 31337:
		// grant complete(root?) access
		// open ssh
		break;
	    }
	    return true;
	}
	return false;
    }

    @Override
    public boolean Disconnect(Device device, String program, int port) {
	// TODO this
	return false;
    }

    @Override
    public boolean addPorts(Device device, String program, int port,
	    Protocol protocol) {
	if (Port.checkPortOr(ports, null, null, port, null) == false) {
	    ports.add(new Port(null, program, port, protocol));
	    return true;
	}
	return false;
    }

    @Override
    public boolean removePort(int port) { // this may be a memory leak where
					  // devices can't remove the ports they
					  // bind when they are removed
	return ports.remove(Port.getPort(ports, null, port));
    }

    @Override
    public void log(Device client, String program, int port, Protocol protocol) {
	masterStorage.addFile(new File(0, "Log - "
		+ client.getNetwork().getIp() + ".log", "Connecting with "
		+ program + " through port" + port + " using " + protocol
		+ "\n" + program + ":" + port + ">" + protocol, FileType.LOG));
	/*
	 * ---Example--- Log - 243.15.66.24 Connecting with half life 3 through
	 * port 28190 using LAMBDA half life 3:28190>LAMBDA
	 */
    }

    // finds all of the devices in the list of that type
    public static List<Device> findDevices(List<Device> devices, DeviceType type) {
	List<Device> returnDevices = new ArrayList<Device>();
	for (Device d : devices) {
	    if (d.getType() == type) {
		returnDevices.add(d);
	    }
	}
	return returnDevices;
    }

    public void addPart(PartType partType, int level, int a, int b, boolean c) {
	switch (partType) {
	case CPU:
	    if (cpuSockets <= Part.findParts(parts, PartType.CPU).size()) {
		break;
	    }

	    Cpu cpu = new Cpu(this, level, a, b);
	    parts.add(cpu);
	    break;
	case GPU:
	    if (cpuSockets <= Part.findParts(parts, PartType.GPU).size()) {
		break;
	    }

	    Gpu gpu = new Gpu(this, level, a, b);
	    parts.add(gpu);
	    break;
	case MEMORY:
	    if (cpuSockets <= Part.findParts(parts, PartType.MEMORY).size()) {
		break;
	    }

	    Memory memory = new Memory(this, level, a, b);
	    parts.add(memory);
	    totalMemory += memory.getCapacity();
	    break;
	default: // storage
	    if (cpuSockets <= Part.findParts(parts, PartType.STORAGE).size()) {
		break;
	    }

	    Storage storage = new Storage(this, level, a, b, c);
	    parts.add(storage);
	    totalStorage += storage.getCapacity();
	    break;
	}
    }

    public void addPart(PartType partType, Part p) {
	switch (partType) {
	case CPU:
	    if (cpuSockets <= Part.findParts(parts, PartType.CPU).size()) {
		break;
	    }

	    Cpu cpu = (Cpu) p;
	    parts.add(cpu);
	    break;
	case GPU:
	    if (cpuSockets <= Part.findParts(parts, PartType.GPU).size()) {
		break;
	    }

	    Gpu gpu = (Gpu) p;
	    parts.add(gpu);
	    break;
	case MEMORY:
	    if (cpuSockets <= Part.findParts(parts, PartType.MEMORY).size()) {
		break;
	    }

	    Memory memory = (Memory) p;
	    parts.add(memory);
	    totalMemory += memory.getCapacity();
	    break;
	default: // storage
	    if (cpuSockets <= Part.findParts(parts, PartType.STORAGE).size()) {
		break;
	    }

	    Storage storage = (Storage) p;
	    parts.add(storage);
	    totalStorage += storage.getCapacity();
	    break;
	}
    }

    public enum DeviceType {
	DEVICE(), DNS(), ROUTER(), SERVER(); // more to come

	private DeviceType() {
	}

    }

    // --------getters/setters--------
    public File getLogs() {
	return logs;
    }

    public void setLogs(File logs) {
	this.logs = logs;
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

    public List<Port> getPorts() {
	return ports;
    }

    public List<Part> getParts() {
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

    public void setPorts(List<Port> ports) {
	this.ports = ports;
    }

    public void setParts(List<Part> parts) {
	this.parts = parts;
    }

    public List<Connection> getConnections() {
	return connections;
    }

    public void setConnections(List<Connection> connections) {
	this.connections = connections;
    }

    public DeviceType getType() {
	return type;
    }

    public void setType(DeviceType type) {
	this.type = type;
    }

    public Brand getBrand() {
	return brand;
    }

    public void setBrand(Brand brand) {
	this.brand = brand;
    }

    public Model getModel() {
	return model;
    }

    public void setModel(Model model) {
	this.model = model;
    }

    public ServerWindow getWindow() {
	return window;
    }

    public void setWindow(ServerWindow window) {
	this.window = window;
    }

    public int getTotalMemory() {
	return totalMemory;
    }

    public void setTotalMemory(int totalMemory) {
	this.totalMemory = totalMemory;
    }

    public int getTotalStorage() {
	return totalStorage;
    }

    public void setTotalStorage(int totalStorage) {
	this.totalStorage = totalStorage;
    }
}
