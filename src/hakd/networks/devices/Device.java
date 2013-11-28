package hakd.networks.devices;

import com.badlogic.gdx.graphics.g2d.Sprite;
import hakd.connection.Connectable;
import hakd.connection.Connection;
import hakd.connection.Port;
import hakd.gui.windows.deviceapps.ServerWindowStage;
import hakd.networks.Network;
import hakd.networks.devices.parts.*;
import hakd.networks.devices.parts.Part.Brand;
import hakd.networks.devices.parts.Part.Model;
import hakd.networks.devices.parts.Part.PartType;
import hakd.other.File;
import hakd.other.File.FileType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Device implements Connectable {
    // stats
    Network network;
    int level;
    String ip = ""; // all network variables will be in IP format
    String address;

    final HashMap<String, Connection> connections = new HashMap<String, Connection>();
    final HashMap<Integer, Port> ports = new HashMap<Integer, Port>(); // portNumber, program / if its closed just delete it
    File logs; // TODO make this a file instead connecting from and the action after that

    Brand brand; // for example bell, or HQ
    Model model;

    int memoryCapacity; // in MB, additive
    int storageCapacity; // in ???, additive
    int cpuSpeed; // in MHz, additive
    int gpuSpeed; // in MHz, additive


    // objects
    final List<Part> parts = new ArrayList<Part>();
    int partLimit;
    Storage masterStorage; // TODO where the os resides
    DeviceType type;

    // gui
    int isoX; // isoX and isoY are first set in EmptyDeviceTile on room creation, then transferred to a the new device when bought, and back to an EmptyDeviceTile when trashed
    int isoY;
    ServerWindowStage window;
    Sprite tile;


    public Device() { // TODO: have random smartphone connections and disconnections. smartphones are like insects on a network, many types, random behavior, and there are lots of them
    }

    @Override
    public boolean connect(Device client, Port clientPort, int port) throws IOException {
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
    public void disconnect(Connection c) {
        c.close();
    }

    @Override
    public void openPort(Port port) throws IOException {
        if (!ports.containsKey(port.getPortNumber())) {
            ports.put(port.getPortNumber(), port);
            return;
        }

        throw new IOException("Port already exists.");
    }

    @Override
    public void closePort(int port) throws IOException {
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
        return;
    }

    @Override
    public void log(String name, String data) {
        try {
            masterStorage.addFile(new File(name + ".log", data, FileType.LOG, this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPart(Part p) {
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

    public void removePart(PartType partType, Part p) {
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

    public void dispose() {
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
        address = null;
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

    public HashMap<Integer, Port> getPorts() {
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

    public Sprite getTile() {
        return tile;
    }

    public void setTile(Sprite tile) {
        this.tile = tile;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashMap<String, Connection> getConnections() {
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

    public int getIsoX() {
        return isoX;
    }

    public void setIsoX(int isoX) {
        this.isoX = isoX;
    }

    public int getIsoY() {
        return isoY;
    }

    public void setIsoY(int isoY) {
        this.isoY = isoY;
    }
}
