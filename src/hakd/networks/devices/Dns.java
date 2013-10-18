package hakd.networks.devices;

import hakd.connection.Port;
import hakd.game.Internet.Protocol;
import hakd.networks.Network;

public final class Dns extends Device {
    public Dns(Network network, int level) {
        super(network, level, DeviceType.DNS);

        openPort(new Port("Dns", Protocol.DNS.portNumber, Protocol.DNS));
    }

    public Dns(Network network, int level, DeviceType type, int cpuSockets, int gpuSlots, int memorySlots, int storageSlots) {
        super(network, level, type, cpuSockets, gpuSlots, memorySlots, storageSlots);

        openPort(new Port("Dns", Protocol.DNS.portNumber, Protocol.DNS));
    }

    int getRandomNumber() {
        return 4; // chosen by fair dice roll
        // guaranteed to be random
    }
}
