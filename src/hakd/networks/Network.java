package hakd.networks;

import hakd.internet.NetworkController;
import hakd.networks.devices.Device;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;
import hakd.other.enumerations.DeviceType;
import hakd.other.enumerations.NetworkType;
import hakd.other.enumerations.Protocol;
import hakd.other.enumerations.Region;
import hakd.other.enumerations.Stance;

import java.util.ArrayList;

public class Network { // this only holds a set of devices and info, connecting to this just forwards you to the router
	// stats
	private ServiceProvider		isp;								// the network's isp
	private int					level;								// 0-7, 0 for player because you start with almost nothing
	private int					speed;								// in Mb per second(1/1024*Gb), may want to change it to MBps
	private String				ip;								// all network variables will be in IP format
	private String				address;
	private String				owner;								// owner, company, player
	private int					serverLimit;						// amount of server objects to begin with and the limit
	private int					dnsLimit;							// same as serverLimit but for DNSs
	private int					routerLimit;
	private Stance				stance;
	private NetworkType			type;
	private Router				router;							// main router

	// objects
	private ArrayList<Device>	devices	= new ArrayList<Device>();

	// gui
	private Region				region;							// where the network is in the world
	private int					xCoordinate;						// where the network is in the regionTab/map
	private int					yCoordinate;

	// --------constructor--------
	@Deprecated
	public Network(NetworkType type) { // this can't be used, you must use the add network or add public network methods in network controller
		level = (int) (Math.random() * 8);
		stance = Stance.NEUTRAL;
		this.type = type;

		if (type != NetworkType.ISP) {
			isp = NetworkController.getServiceProviders().get((int) (Math.random() * NetworkController.getServiceProviders().size()));
			// TODO what does the isp connect to?
		}

		ip = "127.0.0.1";

		switch (type) { // I should really clean these up, meh, later
			case PLAYER:// new player // only happens at the start of the game
				region = Region.NA;
				level = 0;
				serverLimit = 1;
				routerLimit = 1;
				stance = Stance.FRIENDLY;
				break;
			case TEST:
				region = Region.ASIA;
				owner = "test";
				serverLimit = 32; // max possible
				dnsLimit = 2;
				routerLimit = 1; // for now just one
				level = 7;
				stance = Stance.NEUTRAL;
				break;
			case COMPANY: // company // random company
				region = Region.COMPANIES;
				serverLimit = (int) ((level + 1) * (Math.random() * 3 + 1)); // the absolute maximum without upgrading
				dnsLimit = (int) (level / 3.5);
				routerLimit = 1;
				owner = "company";
				break;
			case ISP:
				region = Region.COMPANIES;
				serverLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
				level = (int) (Math.random() * 8);
				dnsLimit = 3;
				routerLimit = 1;
				break;
			default:
				region = Region.EUROPE;
				owner = "test";
				serverLimit = 4; // max possible
				dnsLimit = 1;
				routerLimit = 1;
				stance = Stance.NEUTRAL;
				break;
		}

		// used to add randomness to the amount of servers to make given serverLimit
		int s = (int) Math.round(serverLimit * (Math.random() * 0.35 + 0.65));

		for (int i = 0; i < s; i++) { // create servers on the network
			devices.add(new Server(this, level));
		}

		for (int i = 0; i < dnsLimit; i++) { // create DNSs on the network
			if (type == NetworkType.ISP && i == 0) {
				devices.add(new Dns(true, isp, level));
			} else {
				devices.add(new Dns(false, isp, level));
			}
		}

		for (int i = 0; i < routerLimit; i++) { // create servers on the network
			devices.add(new Router(this, level));
		}

	}

	// --------methods--------
	public boolean Connect(Device client, String program, int port, Protocol protocol) {
		for (Device d : devices) {
			if (d.getType() == DeviceType.ROUTER) {
				return d.Connect(client, program, port, protocol);
			}
		}
		return false;
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

	public ArrayList<Device> getDevices() {
		return devices;
	}

	public void setDevices(ArrayList<Device> devices) {
		this.devices = devices;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
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

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}
}
