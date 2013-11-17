package hakd.networks.devices;

import com.badlogic.gdx.graphics.g2d.Sprite;
import hakd.connection.Connectable;
import hakd.connection.Connection;
import hakd.connection.Port;
import hakd.game.Internet;
import hakd.gui.windows.deviceapps.ServerWindowStage;
import hakd.networks.Network;
import hakd.networks.devices.parts.*;
import hakd.networks.devices.parts.Part.Brand;
import hakd.networks.devices.parts.Part.Model;
import hakd.networks.devices.parts.Part.PartType;
import hakd.other.File;
import hakd.other.File.FileType;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Device implements Connectable {

    // stats
    Network network;
    int level;
    String ip = ""; // all network variables will be in IP format
    String address;
    // enum webserver = 0; // 0 = 404, if portNumber 80 is open
    final List<Port> ports = new ArrayList<Port>(); // portNumber, program / if its closed just delete it
    File logs; // TODO make this a file instead connecting from and the action after that

    Brand brand; // for example bell, or HQ
    Model model;

    int memoryCapacity; // in MB, additive
    int storageCapacity; // in ???, additive
    int cpuSpeed; // in MHz, additive
    int gpuSpeed; // in MHz, additive

    final HashMap<String, Connection> connections = new HashMap<String, Connection>();

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
    public ConnectionStatus connect(Device client, Port port) {
        ConnectionStatus permission = hasPermission(port);

        if (permission == ConnectionStatus.OK) {
            Socket sClient = new Socket();
            Socket sHost = Internet.connectSocket(sClient);

            Connection c = new Connection(this, client, port, sClient, sHost);
            connections.put(client.getIp(), c);

            client.getConnections().put(ip, c);
        } else {
            log(client, port.getProgram(), port.getPortNumber(), port.getProtocol());
            return permission;
        }

        // if player, connect to server program()


        return permission;
    }

    /**
     * Checks if it has permission to make the connection.
     *
     * @return True if it is allowed.
     */
    ConnectionStatus hasPermission(Port port) {

        // TODO check server firewall settings, inbound/outbound
        return ConnectionStatus.OK;
    }

    @Override
    public void disconnect(Connection c) {
        c.close();
    }

    @Override
    public boolean openPort(String program, int portNumber, String protocol) {
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
        for (Connection c : connections.values()) {
            if (c.getClientPort() == port) {
                c.close();// you may have to close these from a for loop with a
                // temporary array
            }
        }

        return ports.remove(port);
    }

    @Override
    public void log(Device client, String program, int port, String protocol) {
        masterStorage.addFile(new File(0, "Log - " + client.ip + ".log", "Connecting with " + program + " through portNumber" + port + " using " + protocol + "\n" + program + ":" + port + "->" + protocol, FileType.LOG));
    /*
     * ---Example---
	 * Log - 243.15.66.24 Connecting with half life 3 through portNumber 28190 using LAMBDA
	 * half life 3:28190->LAMBDA
	 */
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
        for (Port p : ports) {
            closePort(p);
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
