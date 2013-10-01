package hakd.networks.devices;

import hakd.networks.Network;

public final class Server extends Device {

    public Server(Network n, int level) {
        super(n, level, DeviceType.SERVER);

    }

    public Server(Network network, int level, DeviceType type, int cpuSockets, int gpuSlots, int memorySlots, int storageSlots) {
        super(network, level, type, cpuSockets, gpuSlots, memorySlots, storageSlots);
    }
}