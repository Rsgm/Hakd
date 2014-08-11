package networks;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import game.Internet;
import game.Noise;
import game.gameplay.Character;
import game.gameplay.City;
import game.gameplay.Player;
import gui.HakdSprite;
import networks.devices.Device;
import other.Line;

import java.util.HashMap;
import java.util.Map;

/**
 * A network represents a collection of devices.
 */
public class Network {
    String ip = "";
    int level; // 0-7, 0 for player because you start with almost nothing
    Character owner; // owner, company, player
    Stance stance; // TODO move this to npc class
    NetworkType type;
    City city;

    // provider connection info
    int speed; // in MB/s (megabytes per second)
    Network parent; // parent network

    // children device
    final Map<String, Device> devices = new HashMap<String, Device>();
    int deviceLimit; // The maximum allowable devices on the network, also the amount to generate is based on this value. This must be less than 255

    // gui stuff
    private HakdSprite mapIcon;
    Line mapParentLine;
    Vector2 pos; // holds the position of the center of the sprite

    public static final int backboneRegionSize = 2000; // diameter of the circle
    public static final int ispRegionSize = 200; // diameter of the circle
    public static final int networkRegionSize = 100;
    IpRegion ipRegion; // where the network is in the world, it helps find an ip

    Internet internet;

    public Network() {
    }

    /**
     * Removes a device from the network, as well as disposes it, removing any
     * connections.
     */
    public final void removeDevice(Device device) {
        devices.remove(device.getIp());
        device.dispose();

        if (owner instanceof Player) {
            device.getTile().setObject(null);
            ((Player) owner).getRoom().removeDevice(device);
        }
    }

    /**
     * Registers a device on the network.
     */
    public final boolean addDevice(Device device) {
        String ip = assignIp();

        if (devices.size() >= deviceLimit || ip == null) {
            return false;
        }

        devices.put(ip, device);

        device.setIp(ip);
        device.setNetwork(this);

        if (owner instanceof Player) {
            ((Player) owner).getRoom().addDevice(device);
        }
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
    public final Device getDevice(String ip) {
        return devices.get(ip);
    }

    void placeNetwork(float regionSize) {
        float iconWidth = mapIcon.getWidth() / 2;
        float iconHeight = mapIcon.getHeight() / 2;
        Circle c = new Circle(city.getPosition().x, city.getPosition().y, 1);
        Vector2 v = new Vector2();

        l1:
        for (int i = (int) ((city.getDensity() + 1) / 2 * 2500); i < 5000; i += 250) {
            c.setRadius(i / 2);

            for (int k = 0; k < 50; k++) {
                float random = (float) (Math.random() * 2 - 1);
                v.x = (float) ((Math.random() * i) - i / 2) + iconWidth;
                v.y = (float) ((Math.random() * i) - i / 2) + iconHeight;
                v.add(city.getPosition());

                if (c.contains(v)) {
                    double land = Noise.getLand().getValue(v.x, 0, v.y);
                    double density = Noise.DENSITY.getValue(v.x, 0, v.y);
                    if (land > 0 && density >= random) {
                        break;
                    }
                }

                if (k >= 50) {
                    continue l1;
                }
            }
            pos = v;
            mapIcon.setPosition(v.x - iconWidth, v.y - iconHeight);

            if (v.dst2(city.getPosition()) <= City.height * City.height) {
                continue;
            }

            if (internet.getNetworkMap() == null || internet.getNetworkMap().isEmpty()) {
                break;
            }

            int j = 0;
            for (Network n : internet.getNetworkMap().values()) {
                j++;
                if (v.dst2(n.getMapIcon().getX(), n.getMapIcon().getY()) <= regionSize * regionSize && n != this) {
                    continue l1;
                } else if (j >= internet.getNetworkMap().size()) {
                    break l1;
                }
            }
        }
    }

    /**
     * These define how to generate a network.
     */
    public enum NetworkType {
        PLAYER(0, IpRegion.PRIVATE), BUSINESS(0, IpRegion.BUSINESS), TEST(0, IpRegion.none),
        ISP(0, IpRegion.BUSINESS), NPC(0, IpRegion.PRIVATE), BACKBONE(0, IpRegion.BUSINESS),
        EDUCATION(0, IpRegion.EDUCATION), BANK(0, IpRegion.BUSINESS), MILITARY(0, IpRegion.MILITARY),
        GOVERNMENT(0, IpRegion.GOVERNMENT), RESEARCH(0, IpRegion.GOVERNMENT);

        public final float noiseLevel;
        public final IpRegion ipRegion;

        NetworkType(float noiseLevel, IpRegion ipRegion) {
            this.noiseLevel = noiseLevel;
            this.ipRegion = ipRegion;
        }
    }

    public enum Stance {
        FRIENDLY, NEUTRAL, ENEMY // TODO do I want to give the network or the npc a stance?
    }

    public enum IpRegion {
        BUSINESS(1, 56), PRIVATE(57, 126), EDUCATION(173, 182), GOVERNMENT(214, 220), MILITARY(220, 255),
        none(1, 255);

        public final int min; // min backbone ip range
        public final int max; // max ip range

        IpRegion(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public game.gameplay.Character getOwner() {
        return owner;
    }

    public void setOwner(Character owner) {
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

    public Map<String, Device> getDevices() {
        return devices;
    }

    public int getDeviceLimit() {
        return deviceLimit;
    }

    public void setDeviceLimit(int deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    public HakdSprite getMapIcon() {
        return mapIcon;
    }

    public void setMapIcon(HakdSprite mapIcon) {
        this.mapIcon = mapIcon;
        mapIcon.setObject(this);
    }

    public Line getMapParentLine() {
        return mapParentLine;
    }

    public void setMapParentLine(Line mapParentLine) {
        this.mapParentLine = mapParentLine;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
