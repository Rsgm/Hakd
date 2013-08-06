package hakd.networks;

import hakd.game.gameplay.Player;
import hakd.internet.Connection;
import hakd.internet.Internet;
import hakd.internet.Internet.Protocol;
import hakd.networks.devices.Device;
import hakd.networks.devices.Device.DeviceType;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Network { // this only holds a set of devices and info, connecting
		       // to this just forwards you to the masterRouter
    // stats
    ServiceProvider isp; // the network's isp
    int level; // 0-7, 0 for player because you start with almost
	       // nothing
    int speed; // in Mb per second(1/1024*Gb), may want to change it to
	       // MBps
    String ip; // all network variables will be in IP format
    String address;
    String owner; // owner, company, player
    Player player;

    int serverLimit; // amount of server objects to begin with and the
		     // limit
    int dnsLimit; // same as serverLimit but for DNSs
    int routerLimit;

    Stance stance;
    NetworkType type;

    Router masterRouter; // main router
    Server masterServer; // main server

    // children
    final List<Device> otherDevices = new ArrayList<Device>();
    final List<Server> servers = new ArrayList<Server>();
    final List<Dns> dnss = new ArrayList<Dns>();
    final List<Router> routers = new ArrayList<Router>();

    // internet
    List<Connection> connections = new ArrayList<Connection>();

    // gui
    Model sphere;
    ModelInstance instance;
    Vector3 position;
    static final float worldSize = 100;
    Region region; // where the network is in the world, it helps find
		   // an ip

    private Internet internet;

    // --------constructor--------
    @Deprecated
    public Network(NetworkType type, Internet internet) { // this can't be used,
							  // you
	// must use the
	// add network or add public network
	// methods in network controller
	level = (int) (Math.random() * 8);
	stance = Stance.NEUTRAL;
	this.type = type;
	this.setInternet(internet);

	position = new Vector3(
		(float) ((Math.random() * worldSize) - worldSize / 2),
		(float) ((Math.random() * worldSize) - worldSize / 2),
		(float) ((Math.random() * worldSize) - worldSize / 2));
	ModelBuilder modelBuilder = new ModelBuilder();

	switch (type) { // I should really clean these up, meh, later
	case PLAYER:// new player // only happens at the start of the game
	    region = Region.NA;
	    level = -1;
	    serverLimit = 1;
	    routerLimit = 0;
	    stance = Stance.FRIENDLY;
	    sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10,
		    new Material(ColorAttribute.createDiffuse(Color.BLUE)),
		    Usage.Position | Usage.Normal);
	    break;
	default:
	    region = Region.NA;
	    owner = "test";
	    serverLimit = 4;
	    dnsLimit = 1;
	    routerLimit = 1;
	    stance = Stance.NEUTRAL;
	    sphere = modelBuilder
		    .createSphere(5f, 5f, 5f, 20, 10, new Material(
			    ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
			    Usage.Position | Usage.Normal);
	    break;
	case TEST:
	    region = Region.ASIA;
	    owner = "test";
	    serverLimit = 32;
	    dnsLimit = 2;
	    routerLimit = 1; // for now just one
	    level = 7;
	    stance = Stance.NEUTRAL;
	    sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10,
		    new Material(ColorAttribute.createDiffuse(Color.RED)),
		    Usage.Position | Usage.Normal);
	    break;
	case COMPANY: // company // random company
	    region = Region.COMPANIES;
	    serverLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
	    dnsLimit = (int) (level / 3.5);
	    routerLimit = 1;
	    owner = "company";
	    sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10,
		    new Material(ColorAttribute.createDiffuse(Color.ORANGE)),
		    Usage.Position | Usage.Normal);
	    break;
	case ISP:
	    region = Region.COMPANIES;
	    serverLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
	    level = (int) (Math.random() * 8);
	    dnsLimit = 3;
	    routerLimit = 1;
	    sphere = modelBuilder.createSphere(5f, 5f, 5f, 20, 10,
		    new Material(ColorAttribute.createDiffuse(Color.CYAN)),
		    Usage.Position | Usage.Normal);
	    break;
	}

	instance = new ModelInstance(sphere);
	instance.transform.setToTranslation(position);

	if (type != NetworkType.ISP) {
	    isp = internet.getServiceProviders().get(
		    (int) (Math.random() * internet.getServiceProviders()
			    .size()));
	    ip = isp.register(this, 1);
	    // TODO what does the isp connect to? it already gets an ip
	}

	// used to add randomness to the amount of servers to make given
	// serverLimit
	int s = (int) Math.round(serverLimit * (Math.random() * 0.35 + 0.65));

	for (int i = 0; i < s; i++) { // create servers on the network
	    Server server = new Server(this, level);
	    servers.add(server);
	}

	for (int i = 0; i < dnsLimit; i++) { // create DNSs on the network
	    if (type == NetworkType.ISP && i == 0) {
		dnss.add(new Dns(true, this, level));
	    } else {
		dnss.add(new Dns(false, this, level));
	    }
	}

	for (int i = 0; i < routerLimit; i++) { // create servers on the network
	    Router r = new Router(this, level);
	    routers.add(r);

	    if (i == 0) {
		masterRouter = r;
	    }
	}

	// TODO remove this
	if (type != NetworkType.ISP) {
	    servers.get(0).Connect(isp.getMasterDns(), "test", 1337,
		    Protocol.LEET);
	}
    }

    // --------methods--------
    public boolean Connect(Device client, String program, int port,
	    Internet.Protocol protocol) {
	for (Device d : servers) {
	    if (d.getType() == DeviceType.ROUTER) {
		return d.Connect(client, program, port, protocol);
	    }
	}
	return false;
    }

    // removes a device from the network
    public void removeDevice(Device d) {
	servers.remove(d);
	// TODO add a call to remove d from the room
    }

    // adds a device if the limit has not been reached
    public boolean addDevice(Device device) {
	DeviceType dType = device.getType();

	int total = 0;
	for (Device d : servers) {
	    if (d.getType() == dType) {
		total++;
	    }
	}

	if (total < dnsLimit && dType == DeviceType.DNS) {
	    Dns dns = (Dns) device;
	    dnss.add(dns);
	    return true;
	} else if (total < routerLimit && dType == DeviceType.ROUTER) {
	    Router router = (Router) device;
	    routers.add(router);
	    return true;
	} else if (total < serverLimit && dType == DeviceType.SERVER) {
	    Server server = (Server) device;
	    servers.add(server);
	    return true;
	}

	return false;
    }

    public Object findConnection(Device d1, Device d2, String program, int port) {
	Connection connection = null;
	// for(Connection c : connections){
	// //
	// if((c.getHost()==d1||c.getHost()==d2)&&(c.getClient()==d1||c.getClient()==d2)&&(c.)&&()){
	// // connection = c;
	// // }
	// }
	return connection;
    }

    public enum NetworkType { // these don't define networks or who owns them,
			      // their stats do, these just define how to
			      // generate a network
	PLAYER(), COMPANY(), TEST(), ISP(), NPC();// more to come
	NetworkType() {
	}
    }

    public enum Stance {
	FRIENDLY(), NEUTRAL(), ENEMY();
	Stance() {
	}
    }

    public enum Region {
	NA(), SA(), ASIA(), EUROPE, COMPANIES; // others
	Region() {
	}
    }

    // --------getters/setters--------
    public ServiceProvider getIsp() {
	return isp;
    }

    public void setIsp(ServiceProvider isp) {
	this.isp = isp;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }

    public int getSpeed() {
	return speed;
    }

    public void setSpeed(int speed) {
	this.speed = speed;
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

    public Region getRegion() {
	return region;
    }

    public void setRegion(Region region) {
	this.region = region;
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

    public Router getMasterRouter() {
	return masterRouter;
    }

    public void setMasterRouter(Router router) {
	this.masterRouter = router;
    }

    public Server getMasterServer() {
	return masterServer;
    }

    public void setMasterServer(Server masterServer) {
	this.masterServer = masterServer;
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

    public List<Connection> getConnections() {
	return connections;
    }

    public void setConnections(List<Connection> connections) {
	this.connections = connections;
    }
}
