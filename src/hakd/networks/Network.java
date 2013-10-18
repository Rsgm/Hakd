package hakd.networks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import hakd.game.Internet;
import hakd.game.gameplay.Player;
import hakd.networks.devices.Device;
import hakd.networks.devices.Device.DeviceType;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A network represents a collection of devices.
 */
public class Network {
    short[] ip = new short[4];
    int level; // 0-7, 0 for player because you start with almost nothing
    String owner; // owner, company, player
    Player player;
    Stance stance; // TODO move this to npc class
    NetworkType type;

    // connection info
    int speed; // in MB/s (megabytes per second)
    Network parent; // parent network

    // children device
    final List<Device> devices = new ArrayList<Device>();
    int deviceLimit; // The maximum allowable devices on the network, also the amount to generate is based on this value. This must be less than 255

    // gui stuff
    Model sphere;
    ModelInstance sphereInstance;
    Vector3 spherePosition;
    Model parentConnectionLine;
    ModelInstance parentConnectionInstance;
    Vector3 parentConnectionPosition;

    public static final float worldSize = 700;
    public static final float BackboneRegionSize = 150;
    public static final float ispRegionSize = 80;
    public static final float networkRegionSize = 100;
    IpRegion ipRegion; // where the network is in the world, it helps find an ip


    Internet internet;

    /**
     * This should only be used at the beginning of the game. If you need to add
     * networks use {@link hakd.game.Internet}.NewNetwork().
     */
    public Network(NetworkType type, Internet internet) {
        level = (int) (Math.random() * 8);
        stance = Stance.NEUTRAL;
        this.type = type;
        this.setInternet(internet);

        ModelBuilder modelBuilder = new ModelBuilder();

        switch (type) { // I should really clean these up, meh, later
            case PLAYER:// new player // only happens at the start of the game
                ipRegion = IpRegion.NA;
                level = -1;
                deviceLimit = 1;
                stance = Stance.FRIENDLY;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.BLUE)), Usage.Position | Usage.Normal);
                break;
            case NPC:
                ipRegion = IpRegion.NA;
                owner = "NPC";
                deviceLimit = 4;
                stance = Stance.NEUTRAL;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), Usage.Position | Usage.Normal);
                break;
            case TEST:
                ipRegion = IpRegion.ASIA;
                owner = "Test";
                deviceLimit = 32;
                level = 7;
                stance = Stance.NEUTRAL;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
                break;
            case BUSINESS: // company // random company
                ipRegion = IpRegion.BUSINESS;
                deviceLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
                owner = "company";
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.ORANGE)), Usage.Position | Usage.Normal);
                break;
            case ISP:
                ipRegion = IpRegion.values()[internet.getInternetProviderNetworks().size() % IpRegion.values().length];
                deviceLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
                level = (int) (Math.random() * 8);
                sphere = modelBuilder.createSphere(15f, 15f, 15f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.CYAN)), Usage.Position | Usage.Normal);
                break;
            case BACKBONE:
                ipRegion = IpRegion.values()[internet.getBackboneProviderNetworks().size() % IpRegion.values().length];
                deviceLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
                level = (8);
                sphere = modelBuilder.createSphere(25f, 25f, 25f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
                break;
            default: // copied from the npc case
                ipRegion = IpRegion.NA;
                owner = "Some Name";
                deviceLimit = 4;
                stance = Stance.NEUTRAL;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), Usage.Position | Usage.Normal);
                break;
        }

        // used to add randomness to the amount of servers to make given serverLimit
        int d = (int) Math.round(deviceLimit * (Math.random() * 0.35 + 0.65));

        for (int i = 0; i < d; i++) { // create servers on the network
            Server server = new Server(this, level);

        }
    }

    /**
     * Removes a device from the network, as well as disposes it, removing any
     * connections.
     */
    public void removeDevice(Device d) {
        devices.remove(d);
        d.dispose();
        // TODO add a call to remove d from the room, or just use the lists here when redrawing
    }

    /**
     * Registers a device on the netowrk.
     */
    public boolean addDevice(Device device) {
        DeviceType dType = device.getType();
        short ip[] = assignIp();

        int total = 0;
        for (Device d : devices) {
            if (d.getType() == dType) {
                total++;
            }
        }

        if (total >= deviceLimit || ip == null) {
            return false;
        }
        Dns dns = (Dns) device;
        devices.add(dns);

        device.setIp(ip);
        device.setNetwork(this);
        return true;
    }

    /**
     * Assigns an ip to an object that requests one, also checks it and adds it
     * to the dns list. Note: This will return null if there are 25
     */
    private short[] assignIp() {
        short[] ip = null;

        if (devices.size() > 255) {
            return null;
        }

        for (short i = 1; i < 256; i++) {
            ip = new short[]{this.ip[0], this.ip[1], this.ip[2], i};
            if (findDevice(ip) == null) {
                break;
            }
        }
        return ip;
    }

    /**
     * Finds the device with the given ip connected to the dns.
     */
    public Device findDevice(short[] ip) {
        for (Device d : devices) {
            if (Arrays.equals(ip, d.getIp())) {
                return d;
            }
        }
        return null;
    }

    /**
     * These define how to generate a network.
     */
    public enum NetworkType { // TODO choose a random number between 0 and the probabilities added up, then go through a loop to check which to use
        PLAYER(0, 0), BUSINESS(1, 10), TEST(1, 1), ISP(1, 1), NPC(20, 100), BACKBONE(1, 1), EDUCATION(1, 1), BANK(1, 1), MILITARY(1, 1), GOVERNMENT(1, 1),
        RESEARCH(1, 1); // set to 0,0 to never be used in random generation

        public final int probabilityMin; // 0 to 1000
        public final int probabilityMax;

        NetworkType(int probabilityMin, int probabilityMax) {
            this.probabilityMin = probabilityMin;
            this.probabilityMax = probabilityMax;
        }
    }

    public enum Stance {
        FRIENDLY(), NEUTRAL(), ENEMY(); // TODO do I want to give the network or the npc a stance?

        Stance() {
        }
    }

    public enum IpRegion {
        BUSINESS(1, 56), SA(57, 62), NA(63, 76), EUROPE(77, 91), ASIA(92, 114), AFRICA(115, 126), PRIVATE(128, 172),
        EDUCATION(173, 182), GOVERNMENT(214, 220), MILITARY(220, 255), none(1, 255);

        public final int min; // min backbone ip range
        public final int max; // max ip range

        IpRegion(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    public enum Owner {
        COMPANY("Company"), TEST("Test"); // these will be replaced with better
        // ones

        public final String company;

        Owner(String company) {
            this.company = company;
        }
    }


    public int getLevel() {
        return level;
    }


    public void setLevel(int level) {
        this.level = level;
    }


    public String getOwner() {
        return owner;
    }


    public void setOwner(String owner) {
        this.owner = owner;
    }


    public Stance getStance() {
        return stance;
    }


    public void setStance(Stance stance) {
        this.stance = stance;
    }


    public IpRegion getRegion() {
        return ipRegion;
    }


    public void setRegion(IpRegion ipRegion) {
        this.ipRegion = ipRegion;
    }

    public NetworkType getType() {
        return type;
    }


    public void setType(NetworkType type) {
        this.type = type;
    }


    public Player getPlayer() {
        return player;
    }


    public void setPlayer(Player player) {
        this.player = player;
    }


    public Internet getInternet() {
        return internet;
    }


    public void setInternet(Internet internet) {
        this.internet = internet;
    }


    public Model getModel() {
        return sphere;
    }


    public void setModel(Model model) {
        this.sphere = model;
    }


    public Vector3 getSpherePosition() {
        return spherePosition;
    }


    public void setSpherePosition(Vector3 spherePosition) {
        this.spherePosition = spherePosition;
    }


    public Model getSphere() {
        return sphere;
    }


    public void setSphere(Model sphere) {
        this.sphere = sphere;
    }


    public ModelInstance getSphereInstance() {
        return sphereInstance;
    }


    public void setSphereInstance(ModelInstance sphereInstance) {
        this.sphereInstance = sphereInstance;
    }

    public IpRegion getIpRegion() {
        return ipRegion;
    }

    public void setIpRegion(IpRegion ipRegion) {
        this.ipRegion = ipRegion;
    }

    public short[] getIp() {
        return ip;
    }

    public void setIp(short[] ip) {
        this.ip = ip;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Network getParent() {
        return parent;
    }

    public void setParent(Network parent) {
        this.parent = parent;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public int getDeviceLimit() {
        return deviceLimit;
    }

    public void setDeviceLimit(int deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    public Model getParentConnectionLine() {
        return parentConnectionLine;
    }

    public void setParentConnectionLine(Model parentConnectionLine) {
        this.parentConnectionLine = parentConnectionLine;
    }

    public ModelInstance getParentConnectionInstance() {
        return parentConnectionInstance;
    }

    public void setParentConnectionInstance(ModelInstance parentConnectionInstance) {
        this.parentConnectionInstance = parentConnectionInstance;
    }

    public Vector3 getParentConnectionPosition() {
        return parentConnectionPosition;
    }

    public void setParentConnectionPosition(Vector3 parentConnectionPosition) {
        this.parentConnectionPosition = parentConnectionPosition;
    }

    public static float getWorldSize() {
        return worldSize;
    }

    public static float getBackboneRegionSize() {
        return BackboneRegionSize;
    }

    public static float getIspRegionSize() {
        return ispRegionSize;
    }

    public static float getNetworkRegionSize() {
        return networkRegionSize;
    }
}
