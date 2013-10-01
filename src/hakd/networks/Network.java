package hakd.networks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import hakd.connection.Port;
import hakd.game.Internet;
import hakd.game.Internet.Protocol;
import hakd.game.gameplay.Player;
import hakd.networks.devices.Device;
import hakd.networks.devices.Device.DeviceType;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * A network represents a collection of devices.
 */
public class Network {
    // stats
    int level; // 0-7, 0 for player because you start with almost
    // nothing
    String owner; // owner, company, player
    Player player;

    int serverLimit; // amount of server objects to begin with and the
    // limit
    int dnsLimit; // same as serverLimit but for DNSs
    int routerLimit;

    Stance stance;
    NetworkType type;

    // children
    final List<Device> otherDevices = new ArrayList<Device>();
    final List<Server> servers = new ArrayList<Server>();
    final List<Dns> dnss = new ArrayList<Dns>();
    final List<Router> routers = new ArrayList<Router>();

    // gui
    Model sphere;
    ModelInstance instance;
    Vector3 position;
    public static final float worldSize = 700;
    public static final float BackboneRegionSize = 150;
    public static final float ispRegionSize = 80;
    public static final float networkRegionSize = 100;
    IpRegion ipRegion; // where the network is in the world, it helps find
    // an ip

    Internet internet;

    /**
     * This should only be used at the beginning of the game. If you need to add
     * networks use {@link Internet}.NewNetwork().
     */
    @Deprecated
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
                serverLimit = 1;
                routerLimit = 1;
                stance = Stance.FRIENDLY;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.BLUE)), Usage.Position | Usage.Normal);
                break;
            case NPC:
                ipRegion = IpRegion.NA;
                owner = "PyTest";
                serverLimit = 4;
                dnsLimit = 1;
                routerLimit = 1;
                stance = Stance.NEUTRAL;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), Usage.Position | Usage.Normal);
                break;
            case TEST:
                ipRegion = IpRegion.ASIA;
                owner = "PyTest";
                serverLimit = 32;
                dnsLimit = 2;
                routerLimit = 1; // for now just one
                level = 7;
                stance = Stance.NEUTRAL;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
                break;
            case BUSINESS: // company // random company
                ipRegion = IpRegion.BUSINESS;
                serverLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
                dnsLimit = (int) (level / 3.5);
                routerLimit = 1;
                owner = "company";
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.ORANGE)), Usage.Position | Usage.Normal);
                break;
            case ISP:
                ipRegion = IpRegion.values()[internet.getInternetProviderNetworks().size() % IpRegion.values().length];
                serverLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
                level = (int) (Math.random() * 8);
                dnsLimit = 1;
                routerLimit = 1;
                sphere = modelBuilder.createSphere(15f, 15f, 15f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.CYAN)), Usage.Position | Usage.Normal);
                break;
            case BACKBONE:
                ipRegion = IpRegion.values()[internet.getBackboneProviderNetworks().size() % IpRegion.values().length];
                serverLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
                level = (8);
                dnsLimit = 1;
                routerLimit = 1;
                sphere = modelBuilder.createSphere(25f, 25f, 25f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.Normal);
                break;
            default: // copied from the npc case
                ipRegion = IpRegion.NA;
                owner = "PyTest";
                serverLimit = 4;
                dnsLimit = 1;
                routerLimit = 1;
                stance = Stance.NEUTRAL;
                sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), Usage.Position | Usage.Normal);
                break;
        }

        // used to add randomness to the amount of servers to make given
        // serverLimit
        int s = (int) Math.round(serverLimit * (Math.random() * 0.35 + 0.65));

        for (int i = 0; i < routerLimit; i++) { // create servers on the
            // network
            Router r = new Router(this, level);
            routers.add(r);
        }

        Router router = routers.get(0);
        registerPrimaryRouter(router);

        for (int i = 0; i < s; i++) { // create servers on the network
            Server server = new Server(this, level);
            router.register(server);
            servers.add(server);
        }

        for (int i = 0; i < dnsLimit; i++) { // create DNSs on the
            // network
            Dns d = new Dns(this, level);
            router.register(d);
            dnss.add(d);
        }

        instance = new ModelInstance(sphere);
        instance.transform.setToTranslation(position);
    }

    private void registerPrimaryRouter(Router router) {
        switch (type) {
            case BACKBONE:
                router.setParentNetwork(null);
                router.setParent(null);
                router.setIp(new short[]{Dns.generateIpByte(ipRegion), Dns.generateIpByte(IpRegion.none), Dns.generateIpByte(IpRegion.none), 1});

                position = new Vector3((float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2));

                for (BackboneProviderNetwork b : internet.getBackboneProviderNetworks()) {
                    b.getRouters().get(0).Connect(router, new Port(Protocol.DNS.name(), Protocol.DNS.portNumber, Protocol.DNS));
                }
                break;
            case ISP:
                router.setParentNetwork(internet.getBackboneProviderNetworks().get(internet.getInternetProviderNetworks().size() % internet.getBackboneProviderNetworks().size()));
                router.setParent(router.getParentNetwork().getMasterDns());

                position = new Vector3(router.getParentNetwork().getPosition().x + (float) ((Math.random() * BackboneRegionSize) - BackboneRegionSize / 2), router.getParentNetwork().getPosition().y + (float) ((Math.random() * BackboneRegionSize) - BackboneRegionSize / 2), (router.getParentNetwork()).getPosition().z + (float) ((Math.random() * BackboneRegionSize) - BackboneRegionSize / 2));

                router.setIp(router.getParentNetwork().register(router, 1));
                break;
            default:
                router.setParentNetwork(internet.getInternetProviderNetworks().get((int) (Math.random() * internet.getInternetProviderNetworks().size())));
                router.setParent(router.getParentNetwork().getMasterDns());

                position = new Vector3(router.getParentNetwork().getPosition().x + (float) ((Math.random() * ispRegionSize) - ispRegionSize / 2), router.getParentNetwork().getPosition().y + (float) ((Math.random() * ispRegionSize) - ispRegionSize / 2), router.getParentNetwork().getPosition().z + (float) ((Math.random() * ispRegionSize) - ispRegionSize / 2));

                router.setIp(router.getParentNetwork().register(router, 1));
                break;
        }
    }


    public void removeDevice(Router r, Device d) {
        servers.remove(d);
        r.unregister(d);
        // TODO add a call to remove d from the room, or just use the lists here when redrawing
    }


    public boolean addDevice(Router r, Device device) {
        DeviceType dType = device.getType();

        int total = 0;
        for (Device d : servers) {
            if (d.getType() == dType) {
                total++;
            }
        }

        boolean b = false;

        if (total < dnsLimit && dType == DeviceType.DNS) {
            Dns dns = (Dns) device;
            dnss.add(dns);
            b = true;
        } else if (total < routerLimit && dType == DeviceType.ROUTER) {
            Router router = (Router) device;
            routers.add(router);
            b = true;
        } else if (total < serverLimit && dType == DeviceType.SERVER) {
            Server server = (Server) device;
            servers.add(server);
            b = true;
        }

        device.setNetwork(this);
        r.register(device);

        return b;
    }

    /**
     * these define how to generate a network
     */
    public enum NetworkType {
        PLAYER(), BUSINESS(), TEST(), ISP(), NPC(), BACKBONE(), EDUCATION(), BANK(), MILITARY(), GOVERNMENT(),
        RESEARCH();

        NetworkType() {
        }
    }

    public enum Stance {
        FRIENDLY(), NEUTRAL(), ENEMY(); // TODO do I want to give the network or the npc a stance

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


    public int getServerLimit() {
        return serverLimit;
    }


    public void setServerLimit(int serverLimit) {
        this.serverLimit = serverLimit;
    }


    public int getDnsLimit() {
        return dnsLimit;
    }


    public void setDnsLimit(int dnsLimit) {
        this.dnsLimit = dnsLimit;
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


    public int getRouterLimit() {
        return routerLimit;
    }


    public void setRouterLimit(int routerLimit) {
        this.routerLimit = routerLimit;
    }


    public NetworkType getType() {
        return type;
    }


    public void setType(NetworkType type) {
        this.type = type;
    }


    public List<Device> getOtherDevices() {
        return otherDevices;
    }


    public List<Server> getServers() {
        return servers;
    }


    public List<Dns> getDnss() {
        return dnss;
    }


    public List<Router> getRouters() {
        return routers;
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


    public Vector3 getPosition() {
        return position;
    }


    public void setPosition(Vector3 position) {
        this.position = position;
    }


    public Model getSphere() {
        return sphere;
    }


    public void setSphere(Model sphere) {
        this.sphere = sphere;
    }


    public ModelInstance getInstance() {
        return instance;
    }


    public void setInstance(ModelInstance sphereInstance) {
        this.instance = sphereInstance;
    }

    public IpRegion getIpRegion() {
        return ipRegion;
    }

    public void setIpRegion(IpRegion ipRegion) {
        this.ipRegion = ipRegion;
    }
}
