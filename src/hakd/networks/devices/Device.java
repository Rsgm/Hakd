package hakd.networks.devices;

import com.badlogic.gdx.graphics.g2d.Sprite;
import hakd.connection.Connectable;
import hakd.connection.Connection;
import hakd.connection.Connection.ConnectionStatus;
import hakd.connection.Packet;
import hakd.connection.Port;
import hakd.game.Internet;
import hakd.game.Internet.Protocol;
import hakd.gui.windows.server.ServerWindowStage;
import hakd.networks.Network;
import hakd.networks.Network.NetworkType;
import hakd.networks.devices.parts.*;
import hakd.networks.devices.parts.Part.Brand;
import hakd.networks.devices.parts.Part.Model;
import hakd.networks.devices.parts.Part.PartType;
import hakd.other.File;
import hakd.other.File.FileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Device implements Connectable {

    // stats
    Network network;
    int level;
    short[] ip = new short[4]; // all network variables will be in IP
    // format
    String address;
    Router parentRouter; // the router this device belongs to
    // int webserver = 0; // 0 = 404, if portNumber 80 is open
    final List<Port> ports = new ArrayList<Port>(); // portNumber, program /
    // if its
    // closed just
    // delete it
    File logs; // TODO make this a file instead connecting from and the
    // action after that

    Brand brand; // for example bell, or HQ
    Model model;

    int totalMemory; // in MB
    int totalStorage; // in ???

    final List<Connection> connections = new ArrayList<Connection>();
    final Queue<Packet> ConnectionDataBuffer = new ConcurrentLinkedQueue<Packet>();

    // objects
    final List<Part> parts = new ArrayList<Part>();
    int cpuSockets; // easier than using a for loop to count the amount,
    // just remember to
    // change this portNumber
    int memorySlots; // maybe have a maximum part number, so you can
    // specialize a server
    int storageSlots;
    int gpuSlots;
    Storage masterStorage; // TODO where the os resides
    DeviceType type;

    // server gui
    ServerWindowStage window;
    Sprite tile;

    /**
     * @param network - The network This device belongs to.
     * @param level   - The level of the network, used to generate parts.
     * @param type    - The device type.
     */
    public Device(Network network, int level, DeviceType type) { // idea:
        // have
        // random
        // smartphone
        // connections
        // and
        // disconnections

        this.network = network; // idea: smartphones are like
        // insects on a
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
            masterStorage = (Storage) Part.findParts(parts, PartType.STORAGE).get(0);
        }

        switch (type) {
            case DNS:

                if (network.getType() == NetworkType.PLAYER) {
                    window = new ServerWindowStage(this);
                }
                break;
            case ROUTER:

                if (network.getType() == NetworkType.PLAYER) {
                }
                break;
            default:

                if (network.getType() == NetworkType.PLAYER) {
                    window = new ServerWindowStage(this);
                }
                break;
        }

    }

    /**
     * @param network      - The parent network.
     * @param level        - The level of the network, used to generate parts.
     * @param type         - The device type.
     * @param cpuSockets   - The amount of CPU parts to make.
     * @param gpuSlots     - The amount of GPU parts to make.
     * @param memorySlots  - The amount of memory parts to make.
     * @param storageSlots - The amount of storage parts to make.
     */
    public Device(Network network, int level, DeviceType type, int cpuSockets, int gpuSlots, int memorySlots, int storageSlots) {
        this.network = network;
        this.level = level;
        this.cpuSockets = cpuSockets;
        this.gpuSlots = gpuSlots;
        this.memorySlots = memorySlots;
        this.storageSlots = storageSlots;

        this.type = type;

        if (network.getType() == NetworkType.PLAYER) {
            if (type == DeviceType.DNS || type == DeviceType.SERVER) {
                window = new ServerWindowStage(this);
            }
        }
    }

    @Override
    public ConnectionStatus Connect(Device client, Port port) {
        ConnectionStatus permission = hasPermission(port);

        Connection c = new Connection(this, client, port);
        if (permission == ConnectionStatus.OK) {
            connections.add(c);
        } else {
            return permission;
        }

        permission = client.Connect(this, port, true);
        if (permission == ConnectionStatus.OK) {
            List<Connection> connections = client.getConnections();
            c.setSiblingConnection(connections.get(connections.size() - 1));
            connections.get(connections.size() - 1).setSiblingConnection(c);
        } else {
            connections.remove(c);
        }

        // if player, connect to server program()

        return permission;
    }

    private ConnectionStatus Connect(Device client, Port port, boolean twoWay) {
        ConnectionStatus permission = hasPermission(port);

        Connection c = new Connection(this, client, port);
        if (permission == ConnectionStatus.OK) {
            connections.add(c);
        } else {
            return permission;
        }

        // if player, connect to server program()

        return permission;
    }

    /**
     * if it has permission to connect
     *
     * @return True if it is allowed.
     */
    ConnectionStatus hasPermission(Port port) {

        // TODO check server firewall settings, inbound/outbound
        return ConnectionStatus.OK;
    }

    @Override
    public boolean Disconnect(Connection c) {
        return c.close();
    }

    @Override
    public boolean openPort(String program, int portNumber, Protocol protocol) {
        if (Port.checkPort(ports, portNumber) == Port.PortStatus.CLOSED) {
            ports.add(new Port(program, portNumber, protocol));
            return true;
        }
        return false;
    }

    @Override
    public boolean openPort(Port port) {
        if (Port.checkPort(ports, port.getPortNumber()) == Port.PortStatus.CLOSED) {
            ports.add(port);
            return true;
        }
        return false;
    }

    @Override
    public boolean closePort(Port port) {
        for (Connection c : connections) {
            if (c.getClientPort() == port) {
                c.close();// you may have to close these from a for loop with a
                // temporary array
            }
        }

        return ports.remove(port);
    }

    @Override
    public void log(Device client, String program, int port, Protocol protocol) {
        masterStorage.addFile(new File(0, "Log - " + Internet.ipToString(client.ip) + ".log", "Connecting with " + program + " through portNumber" + port + " using " + protocol + "\n" + program + ":" + port + ">" + protocol, FileType.LOG));
    /*
	 * ---Example--- Log - 243.15.66.24 Connecting with half life 3 through
	 * portNumber 28190 using LAMBDA half life 3:28190>LAMBDA
	 */
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
            case PART:
                if (cpuSockets <= Part.findParts(parts, PartType.STORAGE).size()) {
                    break;
                }

                Part part = p;
                parts.add(part);
                break;
            case STORAGE:
                if (cpuSockets <= Part.findParts(parts, PartType.STORAGE).size()) {
                    break;
                }

                Storage storage = (Storage) p;
                parts.add(storage);
                totalStorage += storage.getCapacity();
                break;
        }
    }

    public void dispose() {
        for (Connection c : connections) {
            Disconnect(c);
        }
        for (Port p : ports) {
            closePort(p);
        }

        ip = null;
        address = null;
        network = null;
        parentRouter = null;
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

    public ServerWindowStage getWindow() {
        return window;
    }

    public void setWindow(ServerWindowStage window) {
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

    public Sprite getTile() {
        return tile;
    }

    public void setTile(Sprite tile) {
        this.tile = tile;
    }

    public short[] getIp() {
        return ip;
    }

    public void setIp(short[] ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Router getParentRouter() {
        return parentRouter;
    }

    public void setParentRouter(Router parentRouter) {
        this.parentRouter = parentRouter;
    }
}
