package hakd.networks.devices;

import hakd.gui.windows.Window;
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
    private List<Port> ports = new ArrayList<Port>(); // port, program / if its
						      // closed just delete it
    private List<Connection> connections = new ArrayList<Connection>();
    private File logs; // TODO make this a file instead connecting from and the
		       // action after
    // that

    private Brand brand; // for example bell, or HQ
    private Model model;

    private int totalMemory; // in MB
    private int totalStorage; // in ???
    private int totalCores;

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
    private Window window;

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
	    Cpu cpu = new Cpu(level, network, this);
	    parts.add(cpu);
	    totalCores += cpu.getCores();
	}
	for (int i = 0; i < gpuSlots; i++) {
	    Gpu gpu = new Gpu(level, network, this);
	    parts.add(gpu);
	}
	for (int i = 0; i < memorySlots; i++) {
	    Memory memory = new Memory(level, network, this);
	    parts.add(memory);
	    totalMemory = +memory.getCapacity();
	}
	for (int i = 0; i < storageSlots; i++) {
	    Storage storage = new Storage(level, network, this);
	    parts.add(storage);
	    totalStorage = storage.getCapacity();
	}

	masterStorage = (Storage) Part.findParts(parts, PartType.STORAGE)
		.get(0);

	if (network.getType() == NetworkType.PLAYER) {
	    window = new Window(this);
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

    public Window getWindow() {
	return window;
    }

    public void setWindow(Window window) {
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

    public int getTotalCores() {
	return totalCores;
    }

    public void setTotalCores(int totalCores) {
	this.totalCores = totalCores;
    }
}
