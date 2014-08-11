package networks.devices;

import networks.Network;
import networks.devices.parts.PartFactory;

public class DeviceFactory {

    /**
     * Create a device and give it parts.
     *
     * @param level - The level of the network, used to generate parts.
     * @param type  - The device type.
     */
    public static Device createDevice(int level, Device.DeviceType type) {
        Device d = createDevice(type);

        d.setLevel(level);
        d.setType(type);

        switch (level) {
            case 0:
                d.setPartLimit(4); // TODO server part generation code
                break;
            case 7:
                d.setPartLimit((int) (Math.random() * 2 + 7));
                break;
            default:
                d.setPartLimit((int) (Math.random() * 3 + level));
                break;
        }

        for (int i = 0; i < d.getPartLimit(); i += 4) {
            d.addPart(PartFactory.createCpu(level));
            d.addPart(PartFactory.createGpu(level));
            d.addPart(PartFactory.createMemory(level));

            d.addPart(PartFactory.createStorage(level, false));
        }

        return d;
    }

    /**
     * Create a device and give it parts, and add it to a network.
     *
     * @param network - The network to add the device to.
     * @param level   - The level of the network, used to generate parts.
     * @param type    - The device type.
     */
    public static Device createDevice(Network network, int level, Device.DeviceType type) {
        Device d = createDevice(level, type);
        network.addDevice(d);
        return d;
    }

    /**
     * Create a device.
     *
     * @param network   - The parent network.
     * @param level     - The level of the network, used to generate parts.
     * @param type      - The device type.
     * @param partLimit - The amount to set the part limit to.
     */
    public Device createDevice(Network network, int level, Device.DeviceType type, int partLimit) {
        Device d = createDevice(type);

        network.addDevice(d);
        d.setLevel(level);
        d.setPartLimit(partLimit);
        d.setType(type);
        return d;
    }

    /**
     * Most basic constructor.
     *
     * @param type - The type of device to create.
     */
    public static Device createDevice(Device.DeviceType type) {
        switch (type) {
            default:
                return new Device();
            case DNS:
                return new Dns();
            case SERVER:
                final Server server = new Server();

//                try {
//                    for (int i = 0; i < 3; i++) {
//                        server.getHome().addFile(Util.getFileData("bash", (int) (Math.random() * 9) + ""));
//                    }
//
//                    for (String f : Util.PROGRAMS.keySet()) {
//                        server.getBin().addFile(new File(f, Util.PROGRAMS.get(f)));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                return server;
        }
    }
}
