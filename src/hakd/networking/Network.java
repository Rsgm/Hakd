package hakd.networking;

import hakd.gameplay.PlayerController;
import hakd.networking.devices.Device;
import hakd.networking.devices.Dns;
import hakd.networking.devices.Server;

import java.util.ArrayList;

public class Network implements ConnectableNetwork {
	// stats
	private ServiceProvider			isp;								// address of region isp // for example infinity LTD.
	private int						level;								// 0-7, 0 for player because you start with almost nothing
	private int						speed;								// in Mb per second(1/1024*Gb), may want to change it to MBps
	private String					ip;								// all network variables will be in IP format
	private String					owner;								// owner, company, player
	private int						serverLimit;						// amount of server objects to make
	private Stances					stance;							// friendly, enemy, neutral
	private String					connectedTo;
	private final ArrayList<String>	ports	= new ArrayList<String>();	// port, program, server

	// objects
	private final ArrayList<Device>	devices	= new ArrayList<Device>();

	// gui
	private int						region;							// where the network is in the world
	private int						xCoordinate;						// where the network is in the regionTab/map
	private int						yCoordinate;

	// --------constructor--------
	public Network(Types type) { // make a network array in a regionTab class
		level = (int) (Math.random() * 8);
		stance = Stances.NEUTRAL;

		switch (type) {
			case PLAYER:// new player // only happens at the start of the game
				region = 0;
				ip = Dns.assignIp(region);
				level = 0;
				serverLimit = 1;
				stance = Stances.FRIENDLY;
				isp = ip; // TODO this
				break;
			case COMPANY: // company // random company name // company.assignName();
				region = 0;
				ip = Dns.assignIp(region);
				serverLimit = (int) (Math.random() * 19 + 1); // 1-19, the absolute maximum without upgrading
				level = (int) (Math.random() * 8);
				owner = "company"; // TODO choose random names, either from a file or an enum, so there will be no need to use io
				isp = PlayerController.getHomeNetwork().getIp();
				break;
			case TEST: // TODO remove this if necessary
				region = 0;
				ip = Dns.assignIp(region);
				owner = "test";
				serverLimit = 5;
				level = 7;
				stance = Stances.NEUTRAL;
				isp = PlayerController.getHomeNetwork().getIp(); // TODO random isp in region
				break;
			case ISP: // company // random company name // company.assignName();
				region = 0;
				ip = Dns.assignIp(region);
				serverLimit = (int) (Math.random() * 19 + 1); // 1-19, the absolute maximum without upgrading
				level = (int) (Math.random() * 8);
				owner = "company"; // TODO choose random names, either from a file or an enum, so there will be no need to use io
				isp = PlayerController.getHomeNetwork().getIp(); // what does the isp connect to?
				break;
		}

		int s = (int) (Math.random() * (serverLimit - 1) + 1); // how many servers out of the maximum to give it, though can buy more
		for (int i = 0; i < s; i++) { // create servers on the network
			servers.add(new Server(this)); // creates two player network servers
		}
// addNetwork(); // creates a circle representing the network with squares on it representing the servers
		Dns.addConnection(this, isp); // creates a connection with an isp
	}

	// --------methods--------

	@Override
	public boolean Connect(int Network, String program, int port) {

		return false;
	}

	@Override
	public boolean Disconnect(int Network, String program, int port) {

		return false;
	}

	@Override
	public boolean addPorts(int port, String program, int server) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePort(int port, String program, int server) {
		// TODO Auto-generated method stub
		return false;
	}

	// --------enumerations--------
	public enum Types {
		PLAYER(0), COMPANY(1), TEST(2), ISP(3), DNS(4);
		private int	value;

		private Types(int value) {
			this.value = value;
		}
	};

	public enum Stances {
		FRIENDLY(0), NEUTRAL(1), ENEMY(2);
		private int	value;

		private Stances(int value) {
			this.value = value;
		}

	};

	public enum Regions {
		NA(0), SA(1), ASIA(2); // others
		private int	value;

		private Regions(int value) {
			this.value = value;
		}

	};
}
