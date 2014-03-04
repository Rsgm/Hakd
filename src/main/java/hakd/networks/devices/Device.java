package hakd.networks.devices;

import com.badlogic.gdx.math.Vector2;
import hakd.connection.Connectable;
import hakd.connection.Connection;
import hakd.connection.Port;
import hakd.gui.HakdSprite;
import hakd.gui.windows.device.DeviceScene;
import hakd.networks.Network;
import hakd.networks.devices.parts.*;
import hakd.networks.devices.parts.Part.Brand;
import hakd.networks.devices.parts.Part.Model;
import hakd.networks.devices.parts.Part.PartType;
import hakd.other.File;
import hakd.other.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Device implements Connectable {
    Network network;
    int level;
    String ip = ""; // all network variables will be in IP format

    final Map<String, Connection> connections = new HashMap<String, Connection>();
    final Map<Integer, Port> ports = new HashMap<Integer, Port>(); // portNumber, program / if its closed just delete it
    File logs; // TODO make this a file instead connecting from and the action after that

    Brand brand; // for example bell, or HQ
    Model model;

    int memoryCapacity; // in MB, additive
    int storageCapacity; // in ???, additive
    int cpuSpeed; // in MHz, additive
    int gpuSpeed; // in MHz, additive

    // objects
    final Set<Part> parts = new HashSet<Part>();
    int partLimit;
    DeviceType type;

    // gui
    Vector2 isoPos;
    DeviceScene deviceScene;
    HakdSprite tile;

    // default files
    private final File root = new File("root", null); // root directory of the storage filesystem, rm -rf /
    private File sys = new File("sys", null); // operating system files, !FUN!
    private File home = new File("home", null); // random files people save
    private File bin = new File("bin", null); // (python)programs able to run
    private File log = new File("log", null); // these log arrays have infinite storage, thanks to a new leap in quantum physics

    public Device() { // TODO: have random smartphone connections and disconnections. smartphones are like insects on a network, many types, random behavior, and there are lots of them
        root.setDevice(this);
        root.addFile(sys);
        root.addFile(home);
        root.addFile(bin);
        root.addFile(log);

        try {
            for (int i = 0; i < 3; i++) {
                home.addFile(Util.getFileData("bash", (int) (Math.random() * 9) + ""));
            }

            for (String f : Util.PROGRAMS.keySet()) {
                bin.addFile(new File(f, Util.PROGRAMS.get(f)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public final boolean connect(Device client, Port clientPort, int port) throws IOException {
        if (client == this && clientPort.getPortNumber() == port) {
            throw new IOException("Can't connect a port to its self.");
        }

        ConnectionStatus permission = hasPermission(clientPort, port);

        if (permission == ConnectionStatus.OK) {
            int i;
            do {
                i = (int) (Math.random() * 25536 + 40000); // 2^16 = 65536, 65536 - 40000 = 25536
            } while (ports.containsKey(i));

            Port defaultServerPort = ports.get(port);
            Port hostPort = new Port(defaultServerPort.getProgram(), i, defaultServerPort.getProtocol());

            if (hostPort == null) {
                throw new IOException(ConnectionStatus.Internal_Server_Error.toString());
            }

            try {
                hostPort.connect(clientPort);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException(ConnectionStatus.Service_Unavailable.toString());
            }

            Connection c = new Connection(this, client, clientPort, hostPort);

            connections.put(client.getIp(), c);
            client.getConnections().put(ip, c);

            return true;
        } else {
            log("Connection falure", "Connection from: " + client.getIp() + ", to port: " + port);
            throw new IOException(permission.toString());
        }
    }

    /**
     * Checks if it has permission to make the connection.
     *
     * @return True if it is allowed.
     */
    ConnectionStatus hasPermission(Port port, int hostPort) {
        if (!ports.containsKey(hostPort)) {
            return ConnectionStatus.Internal_Server_Error;
        }

        // TODO check server firewall settings, inbound/outbound
        return ConnectionStatus.OK;
    }

    @Override
    public final void disconnect(Connection c) {
        c.close();
    }

    @Override
    public final void openPort(Port port) throws IOException {
        if (!ports.containsKey(port.getPortNumber())) {
            ports.put(port.getPortNumber(), port);
            return;
        }

        throw new IOException("Port already exists.");
    }

    @Override
    public final void closePort(int port) throws IOException {
        if (!ports.containsKey(port)) {
            throw new IOException("Port does not exist.");
        }

        for (Connection c : connections.values()) { // close connections on the port to be closed.
            if (c.getClient() == this && c.getClientPort() == ports.get(port)) {
                c.getClientPort().disconnect();
                c.getClientPort().disconnect();
                c.close();
            } else if (c.getHost() == this && c.getHostPort() == ports.get(port)) {
                c.getClientPort().disconnect();
                c.getClientPort().disconnect();
                c.close();
            }
        }

        ports.remove(port);
    }

    @Override
    public final void log(String name, String data) {
        try {
            log.addFile(new File(name + ".log", data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void addPart(Part p) {
        if (partLimit <= parts.size()) {
            return;
        }
        parts.add(p);
        p.setDevice(this);

        switch (p.getType()) {
            case CPU:
                Cpu cpu = (Cpu) p;
                if (cpu.getCores() > 0) {
                    cpuSpeed += cpu.getSpeed() * cpu.getCores() * 0.75; // CPU cores are .75 as efficient than CPUs
                } else {
                    cpuSpeed += cpu.getSpeed();
                }
                break;
            case GPU:
                Gpu gpu = (Gpu) p;
                gpuSpeed += gpu.getSpeed();
                break;
            case MEMORY:
                Memory memory = (Memory) p;
                this.memoryCapacity += memory.getCapacity();
                break;
            case PART:
                Part part = p;
                break;
            case STORAGE:
                Storage storage = (Storage) p;
                this.storageCapacity += storage.getCapacity();
                break;
        }
    }

    public final void removePart(PartType partType, Part p) {
        if (!parts.remove(p)) {
            return;
        }

        switch (partType) {
            case CPU:
                Cpu cpu = (Cpu) p;
                if (cpu.getCores() > 0) {
                    cpuSpeed -= cpu.getSpeed() * cpu.getCores() * 0.75; // CPU cores are .75 as efficient than CPUs
                } else {
                    cpuSpeed -= cpu.getSpeed();
                }
                break;
            case GPU:
                Gpu gpu = (Gpu) p;
                gpuSpeed -= gpu.getSpeed();
                break;
            case MEMORY:
                Memory memory = (Memory) p;
                this.memoryCapacity -= memory.getCapacity();
                break;
            case PART:
                Part part = p;
                break;
            case STORAGE:
                Storage storage = (Storage) p;
                this.storageCapacity -= storage.getCapacity();
                break;
        }
    }

    public File getFile(String path) throws FileNotFoundException {
        if (path.isEmpty() || !path.matches("^/.*?$")) {
            throw new FileNotFoundException();
        }

        File file = root;
        String[] splitPath = path.split("/");

        for (String s : splitPath) {
            File f = file.getFile(s);
            if (f != null) {
                file = f;
            } else if (!s.isEmpty()) {
                throw new FileNotFoundException();
            }
        }

        return file;
    }

    public int getUsedSpace() {
        int space = 0;
        for (File f : root.getRecursiveFileList(root)) {
            space += f.getSize();
        }
        return space;
    }

    public int getTotalSpace() {
        int space = 0;
        for (Part p : parts) {
            if (p instanceof Storage) {
                space += ((Storage) p).getCapacity();
            }
        }
        return space;
    }

    public final void dispose() {
        // TODO stop terminal command threads

        for (Connection c : connections.values()) {
            disconnect(c);
        }

        for (Port p : ports.values()) {
            try {
                closePort(p.getPortNumber());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        network.removeDevice(this);
        ip = null;
        network = null;

    }

    public enum DeviceType {
        DEVICE(), DNS(), SERVER(); // more to come

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

    public Network getNetwork() {
        return network;
    }

    public int getLevel() {
        return level;
    }

    public Map<Integer, Port> getPorts() {
        return ports;
    }

    public Set<Part> getParts() {
        return parts;
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

    public DeviceScene getDeviceScene() {
        return deviceScene;
    }

    public void setDeviceScene(DeviceScene deviceScene) {
        this.deviceScene = deviceScene;
    }

    public int getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(int memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public HakdSprite getTile() {
        return tile;
    }

    public void setTile(HakdSprite tile) {
        this.tile = tile;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, Connection> getConnections() {
        return connections;
    }

    public int getCpuSpeed() {
        return cpuSpeed;
    }

    public int getGpuSpeed() {
        return gpuSpeed;
    }

    public int getPartLimit() {
        return partLimit;
    }

    public void setPartLimit(int partLimit) {
        this.partLimit = partLimit;
    }

    public File getRoot() {
        return root;
    }

    public File getSys() {
        return sys;
    }

    public File getHome() {
        return home;
    }

    public File getBin() {
        return bin;
    }

    public File getLog() {
        return log;
    }

    public Vector2 getIsoPos() {
        return isoPos;
    }

    public void setIsoPos(Vector2 isoPos) {
        this.isoPos = isoPos;
    }
}
