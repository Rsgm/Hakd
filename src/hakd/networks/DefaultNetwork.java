package hakd.networks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import hakd.game.gameplay.Player;
import hakd.internet.Internet;
import hakd.internet.Internet.Protocol;
import hakd.internet.Port;
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
public class DefaultNetwork implements Network {
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
	public DefaultNetwork(NetworkType type, Internet internet) {
		level = (int) (Math.random() * 8);
		stance = Stance.NEUTRAL;
		this.type = type;
		this.setInternet(internet);

		ModelBuilder modelBuilder = new ModelBuilder();

		switch(type) { // I should really clean these up, meh, later
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

		for(int i = 0; i < routerLimit; i++) { // create servers on the
			// network
			Router r = new Router(this, level);
			routers.add(r);
		}

		Router router = routers.get(0);
		registerPrimaryRouter(router);

		for(int i = 0; i < s; i++) { // create servers on the network
			Server server = new Server(this, level);
			router.register(server);
			servers.add(server);
		}

		for(int i = 0; i < dnsLimit; i++) { // create DNSs on the
			// network
			Dns d = new Dns(this, level);
			router.register(d);
			dnss.add(d);
		}

		instance = new ModelInstance(sphere);
		instance.transform.setToTranslation(position);
	}

	private void registerPrimaryRouter(Router router) {
		switch(type) {
			case BACKBONE:
				router.setParentNetwork(null);
				router.setParent(null);
				router.setIp(new short[]{Dns.generateIpByte(ipRegion), Dns.generateIpByte(IpRegion.none), Dns.generateIpByte(IpRegion.none), 1});

				position = new Vector3((float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2));

				for(BackboneProviderNetwork b : internet.getBackboneProviderNetworks()) {
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

	@Override
	public void removeDevice(Router r, Device d) {
		servers.remove(d);
		r.unregister(d);
		// TODO add a call to remove d from the room, or just use the lists here
		// when redrawing
	}

	@Override
	public boolean addDevice(Router r, Device device) {
		DeviceType dType = device.getType();

		int total = 0;
		for(Device d : servers) {
			if(d.getType() == dType) {
				total++;
			}
		}

		boolean b = false;

		if(total < dnsLimit && dType == DeviceType.DNS) {
			Dns dns = (Dns) device;
			dnss.add(dns);
			b = true;
		} else if(total < routerLimit && dType == DeviceType.ROUTER) {
			Router router = (Router) device;
			routers.add(router);
			b = true;
		} else if(total < serverLimit && dType == DeviceType.SERVER) {
			Server server = (Server) device;
			servers.add(server);
			b = true;
		}

		device.setNetwork(this);
		r.register(device);

		return b;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public int getServerLimit() {
		return serverLimit;
	}

	@Override
	public void setServerLimit(int serverLimit) {
		this.serverLimit = serverLimit;
	}

	@Override
	public int getDnsLimit() {
		return dnsLimit;
	}

	@Override
	public void setDnsLimit(int dnsLimit) {
		this.dnsLimit = dnsLimit;
	}

	@Override
	public Stance getStance() {
		return stance;
	}

	@Override
	public void setStance(Stance stance) {
		this.stance = stance;
	}

	@Override
	public IpRegion getRegion() {
		return ipRegion;
	}

	@Override
	public void setRegion(IpRegion ipRegion) {
		this.ipRegion = ipRegion;
	}

	@Override
	public int getRouterLimit() {
		return routerLimit;
	}

	@Override
	public void setRouterLimit(int routerLimit) {
		this.routerLimit = routerLimit;
	}

	@Override
	public NetworkType getType() {
		return type;
	}

	@Override
	public void setType(NetworkType type) {
		this.type = type;
	}

	@Override
	public List<Device> getOtherDevices() {
		return otherDevices;
	}

	@Override
	public List<Server> getServers() {
		return servers;
	}

	@Override
	public List<Dns> getDnss() {
		return dnss;
	}

	@Override
	public List<Router> getRouters() {
		return routers;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public Internet getInternet() {
		return internet;
	}

	@Override
	public void setInternet(Internet internet) {
		this.internet = internet;
	}

	@Override
	public Model getModel() {
		return sphere;
	}

	@Override
	public void setModel(Model model) {
		this.sphere = model;
	}

	@Override
	public Vector3 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	@Override
	public Model getSphere() {
		return sphere;
	}

	@Override
	public void setSphere(Model sphere) {
		this.sphere = sphere;
	}

	@Override
	public ModelInstance getInstance() {
		return instance;
	}

	@Override
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
