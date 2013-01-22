package hakd.network;

import hakd.gameplay.PlayerController;
import hakd.gui.GameGui;
import hakd.networking.Dns;

import java.awt.Desktop;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.util.ArrayList;

public class Network { // TODO make all objects ArrayLists
	// stats
	private String						isp;									// address of region isp // what makes a network an isp? // for
// example infinity LTD.
	private int							level;									// 0-7, 0 for player because you start with almost nothing
	private int							speed;									// in Mb per second(1/1024*Gb), may want to change it to MBps
	private String						ip;									// all network variables will be in IP format
	private String						owner;									// owner, company, player
	private int							serverLimit;							// amount of server objects to make
	private int							networkId;
	private int							stance;								// friendly, enemy, neutral
	private String						connectedTo;
	private final ArrayList<String>		ports		= new ArrayList<String>();	// port, program, server

	// objects
	private ArrayList<Server>			servers		= new ArrayList<Server>();
	private static ArrayList<Network>	networks	= new ArrayList<Network>();

	// gui
	private int							region;								// where the network is in the world
	private int							xCoordinate;							// where the network is in the regionTab/map
	private int							yCoordinate;

	// --------constructor--------
	public Network(int type) { // make a network array in a regionTab class
		networkId = networks.size();
		level = (int) (Math.random() * 8);
		stance = 1;

		switch (type) {
			case 0:// new player // only happens at the start of the game
				region = 0;
				ip = Dns.assignIp(region);
				level = 0;
				serverLimit = 1;
				stance = 0;
				isp = ip;
				break;
			case 1: // company // random company name // company.assignName();
				region = 0;
				ip = Dns.assignIp(region);
				serverLimit = (int) (Math.random() * 19 + 1); // 1-19, the absolute maximum without upgrading
				level = (int) (Math.random() * 8);
				owner = "company"; // TODO choose random names, either from a file or an enum, so there will be no need to use io
				isp = PlayerController.getHomeNetwork().getIp();
				break;
			case 2:
				region = 0;
				ip = Dns.assignIp(region);
				owner = "test";
				serverLimit = 5;
				level = 7;
				stance = 2;
				isp = PlayerController.getHomeNetwork().getIp();
				break;
		}
		// do not add code referencing the player network(home/current) after here, because it has not been added to the playerController class yet,
// but more obvously it is still being initilized
	}

	// --------methods--------
	public void populate() { // populates the network after the network is created so the network devices can get variables from the network
		int s = (int) (Math.random() * (serverLimit - 1) + 1); // how many servers out of the maximum to give it, though can buy more
		for (int i = 0; i < s; i++) { // create servers on the network
			servers.add(new Server(this)); // creates two player network servers
			servers.get(servers.size() - 1).populate(servers.size() - 1);
		}
		addNetwork(); // creates a circle representing the network with squares on it representing the servers
		Dns.addConnection(this, isp); // creates a connection with an isp
	}

	public static boolean connect(String fromAddress, String toAddress, String program, int portInt) { // TODO test and if possible fix, otherwise
// rewrite
		int id = Dns.findNetwork(toAddress);
		String port = portInt + "";
		if (networks.get(id).ports.get(networks.get(id).ports.indexOf(port) + 1).equals(program) && Dns.findNetwork(fromAddress) != -1) {
			return (networks.get(id).servers.get(Integer.parseInt(networks.get(id).ports.get(networks.get(id).ports.indexOf(port) + 2))).connect(
					fromAddress, program, port));
		}
		return false;
	}

	public boolean addPorts(int port, String program, int server) {
		if (ports.contains(ports)) {
			ports.add(port + "");
			ports.add(program);
			ports.add(server + "");
			return true;
		}
		return false;
	}

	public boolean removePort(int port) {
		int index = ports.indexOf(port + "");
		if (index != -1) {
			ports.remove(index + 2);
			ports.remove(index + 1);
			ports.remove(index);
			return true;
		}
		return false;
	}

	private void addNetwork() {
		boolean taken = false;
		ArrayList<Circle> circles = GameGui.getCircles();
		int radius = GameGui.getRadius();

		do { // this will check if this network is too close to another network, and it will re-pick coordinates for it.
			taken = false;

			xCoordinate = (int) (Math.random() * 1920 + radius); // just a limit for now based on 1080p screens, not that you could fit the entire
// region tab on the screen
			yCoordinate = (int) (Math.random() * 1080 + radius);

			for (Circle c : circles/*old: int i = 0; i < circles.size() - 2; i++*/) { // this checks if this network is within r+2 of another network
				if (!((xCoordinate - 2 * radius - 20 <= c.getCenterX() && c.getCenterX() <= xCoordinate + 2 * radius + 20) && (yCoordinate - 2
						* radius - 20 <= c.getCenterX() && c.getCenterX() <= yCoordinate + 2 * radius + 20))) {
					taken = false;
				} else {
					System.out.println("too close to another network");
					taken = true;
					break;
				}
			}
		} while (taken == true);

		Circle c = new Circle();
		circles.add(c);
		c.setCenterX(xCoordinate);
		c.setCenterY(yCoordinate);

		for (int i = 0; i < servers.size(); i++) { // hide these before the network is scanned, maybe?
			addServer(i + 1);
		}

		c.setRadius(radius);

		switch (stance) {
			case 0:
				c.getStyleClass().add("friendly-network");
				break;
			case 1:
				c.getStyleClass().add("neutral-network");
				break;
			case 2:
				c.getStyleClass().add("enemy-network");
				break;
			default:
				c.getStyleClass().add("nuetral-network");
				break;
		}
		Tooltip.install(c, new Tooltip(ip + "\n" + owner));

		c.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.isAltDown()) {
					Clipboard clipboard = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					content.putString(ip);
					clipboard.setContent(content);
				} else {
					System.out.println("network " + ip + " " + owner);
					try { // open port 80 of that network in the web browser
						Desktop.getDesktop().browse(java.net.URI.create("http://localhost:80/network/" + ip));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		GameGui.updateRegion();
	}

	private void addServer(int server) {
		ArrayList<Polygon> polygons = GameGui.getPolygons();
		ArrayList<Circle> circles = GameGui.getCircles();
		int radius = GameGui.getRadius();

		Polygon poly = new Polygon();
		polygons.add(poly);
		poly.getPoints().addAll(new Double[] { -8.0, -8.0, -8.0, 8.0, 8.0, 8.0, 8.0, -8.0 });
		poly.setFill(Paint.valueOf("orange"));
		poly.setStroke(Paint.valueOf("black"));

		poly.setLayoutX(circles.get(circles.size() - 1).getCenterX() - (Math.sin(Math.toRadians(360 / serverLimit * server)) * radius));
		poly.setLayoutY(circles.get(circles.size() - 1).getCenterY() - (Math.cos(Math.toRadians(360 / serverLimit * server)) * radius));
		// GameGui.updateRegion();
	}

	// --------getters/setters--------
	public int getNetworkId() {
		return networkId;
	}

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public void setServers(int servers) {
		this.serverLimit = servers;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getConnectedTo() {
		return connectedTo;
	}

	public void setConnectedTo(String connectedTo) {
		this.connectedTo = connectedTo;
	}

	public ArrayList<Server> getServers() {
		return servers;
	}

	public void setServers(ArrayList<Server> server) {
		this.servers = server;
	}

	public static ArrayList<Network> getNetworks() {
		return networks;
	}

	public static void setNetworks(ArrayList<Network> network) {
		Network.networks = network;
	}

	public int getStance() {
		return stance;
	}

	public void setStance(int stance) {
		this.stance = stance;
	}

	public ArrayList<String> getPorts() {
		return ports;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public void setServerLimit(int serverLimit) {
		this.serverLimit = serverLimit;
	}
}
