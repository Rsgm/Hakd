package hakd.networks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import hakd.game.Internet;
import hakd.game.gameplay.Player;
import hakd.gui.EmptyDeviceTile;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * A network represents a collection of devices.
 */
public class Network {
    String ip = "";
    int level; // 0-7, 0 for player because you start with almost nothing
    String owner; // owner, company, player
    Player player;
    Stance stance; // TODO move this to npc class
    NetworkType type;

    // provider connection info
    int speed; // in MB/s (megabytes per second)
    Network parent; // parent network

    // children device
    final List<Device> devices = new ArrayList<Device>();
    int deviceLimit; // The maximum allowable devices on the network, also the amount to generate is based on this value. This must be less than 255
    List<EmptyDeviceTile> EmptyDeviceTiles;

    // gui stuff
    Sprite mapIcon;
    Sprite mapParentLine;

    public static final float BackboneRegionSize = 2000;
    public static final float ispRegionSize = 800;
    public static final float networkRegionSize = 100;
    IpRegion ipRegion; // where the network is in the world, it helps find an ip

    Internet internet;

    public Network() {
    }

    /**
     * Removes a device from the network, as well as disposes it, removing any
     * connections.
     */
    public void removeDevice(Device d) {
        devices.remove(d);
        d.dispose();
    }

    /**
     * Registers a device on the network.
     */
    public boolean addDevice(Device device) {
        String ip = assignIp();

        if (devices.size() >= deviceLimit || ip == null) {
            return false;
        }

        devices.add(device);

        device.setIp(ip);
        device.setNetwork(this);
        return true;
    }

    /**
     * Assigns an ip to a device. Note: This will return null if there are 255
     */
    private String assignIp() {
        short[] deviceIp = null;
        short[] ip = Internet.ipFromString(this.ip);

        if (devices.size() > 255) {
            return null;
        }

        for (short i = 2; i < 256; i++) {
            deviceIp = new short[]{ip[0], ip[1], ip[2], i};
            if (getDevice(Internet.ipToString(deviceIp)) == null) {
                break;
            }
        }
        return Internet.ipToString(deviceIp);
    }

    /**
     * Finds the device with the given ip connected to the dns.
     */
    public Device getDevice(String ip) {
        for (Device d : devices) {
            if (ip.equals(d.getIp())) {
                return d;
            }
        }
        return null;
    }

    /**
     * These define how to generate a network.
     */
    public enum NetworkType { // TODO choose a random number between 0 and the probabilities added up, then go through a loop to check which to use
        PLAYER(0, 0), BUSINESS(1, 10), TEST(1, 1), ISP(1, 1), NPC(20, 100), BACKBONE(1, 1), EDUCATION(1, 1), BANK(1, 1),
        MILITARY(1, 1), GOVERNMENT(1, 1),
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

    public IpRegion getIpRegion() {
        return ipRegion;
    }

    public void setIpRegion(IpRegion ipRegion) {
        this.ipRegion = ipRegion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
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

    public List<EmptyDeviceTile> getEmptyDeviceTiles() {
        return EmptyDeviceTiles;
    }

    public void setEmptyDeviceTiles(List<EmptyDeviceTile> emptyDeviceTiles) {
        EmptyDeviceTiles = emptyDeviceTiles;
    }

    public Sprite getMapIcon() {
        return mapIcon;
    }

    public void setMapIcon(Sprite mapIcon) {
        this.mapIcon = mapIcon;
    }

    public Sprite getMapParentLine() {
        return mapParentLine;
    }

    public void setMapParentLine(Sprite mapParentLine) {
        this.mapParentLine = mapParentLine;
    }
}
