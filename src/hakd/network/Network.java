package hakd.network;

import hakd.gameplay.PlayerController;
import hakd.userinterface.Controller;

import java.awt.Desktop;
import java.io.IOException;
import java.util.Vector;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Network { // todo make all objects vectors
	// stats
	// private String isp; // address of region isp // what makes a network an isp? // for example infinity LTD.
	private int						level;										// 0-7, 0 for player because you start with almost nothing
	private int						speed;										// in Mb per second, may want to change it to MBps
	private String					ip;										// all network variables will be in IP format
	private String					owner;										// owner, company, player
	private int						serverLimit;								// amount of server objects to make
	private int						networkId;
	private int						stance;									// friendly, enemy, neutral
	private String					connectedTo	= null;
	private final Vector<String>	ports		= new Vector<String>(1, 1);	// port, program, server

	// display

	// objects
	private Vector<Server>			servers		= new Vector<Server>(1, 1);
	private static Vector<Network>	networks	= new Vector<Network>(1, 1);

	// user interface
	private int						region;									// where the network is in the world
	private int						xCoordinate;								// where the network is in the regionTab/map
	private int						yCoordinate;
	// private Vector<String>
	private static Vector<Circle>	circles		= new Vector<Circle>(1, 1);
	private final Vector<Polygon>	polygons	= new Vector<>(1, 1);
	private static final int		radius		= 35;

	// --------constructor--------
	public Network(int type) { // make a network array in a regionTab class
		networkId = networks.size();
		level = (int) (Math.random() * 8);
		stance = 1;
		// isp =

		switch (type) {
			case 0:// new player // only happens at the start of the game
				region = 0;
				ip = Dns.assignIp(region);
				level = 0;
				serverLimit = 1;
				stance = 0;
				PlayerController.setHomeNetwork(ip);
				PlayerController.setCurrentNetwork(ip);
				PlayerController.setCurrentServer(0);
				break;
			case 1: // company // random company name // company.assignName();
				region = 0;
				ip = Dns.assignIp(region);
				serverLimit = (int) (Math.random() * 19 + 1); // 3-5
				level = (int) (Math.random() * 8);
				owner = "company";
				break;
			case 2:
				region = 0;
				ip = Dns.assignIp(region);
				owner = "test";
				serverLimit = 5;
				level = 7;
				break;
		}
	}

	// --------methods--------
	public void populate() { // populates the network after the network is created so the network devices can get variables from the network
		for (int i = 0; i < serverLimit; i++) { // create servers on the network
			servers.add(new Server(networkId));
			servers.get(servers.size() - 1).populate(servers.size() - 1);
		}
		addNetwork();
		Dns.addConnection(this, PlayerController.getHomeNetwork());
	}

	public static boolean connect(String fromAddress, String toAddress, String program, int portInt) {
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
		int index = ports.indexOf(port);
		if (ports.indexOf(port) != -1) {
			ports.remove(index + 2);
			ports.remove(index + 1);
			ports.remove(index);
			return true;
		}
		return false;
	}

	private synchronized void addNetwork() {
		boolean taken = false;
		circles.add(new Circle());

		do {
			taken = false;

			xCoordinate = (int) (Math.random() * 1920 + radius);
			yCoordinate = (int) (Math.random() * 1080 + radius);

			for (int i = 0; i < circles.size() - 2; i++) { // TODO change this to if within x+2r of another network or x+r of a side choose a new one
				if (!((xCoordinate - 2 * radius - 20 <= circles.get(i).getCenterX() && circles.get(i).getCenterX() <= xCoordinate + 2 * radius + 20) && (yCoordinate
						- 2 * radius - 20 <= circles.get(i).getCenterX() && circles.get(i).getCenterX() <= yCoordinate + 2 * radius + 20))) {
					taken = false;
				} else {
					System.out.println("too close to another network");
					taken = true;
					break;
				}
			}
		} while (taken == true);

		circles.get(circles.size() - 1).setCenterX(xCoordinate);
		circles.get(circles.size() - 1).setCenterY(yCoordinate);

		Controller.regionView.get(region).getChildren().add(circles.get(circles.size() - 1));

		for (int i = 0; i < servers.size(); i++) { // hide these before the network is scanned, maybe?
			addServer(i + 1);
		}

		circles.get(circles.size() - 1).setRadius(radius);

		switch (stance) {
			case 0:
				circles.get(circles.size() - 1).getStyleClass().add("friendly-network");
				break;
			case 1:
				circles.get(circles.size() - 1).getStyleClass().add("nuetral-network");
				break;
			case 2:
				circles.get(circles.size() - 1).getStyleClass().add("enemy-network");
				break;
			default:
				circles.get(circles.size() - 1).getStyleClass().add("nuetral-network");
				break;
		}
		Tooltip.install(circles.get(circles.size() - 1), new Tooltip(ip + "\n" + owner));

		circles.get(circles.size() - 1).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				System.out.println("network " + ip + " " + owner);

				try { // open port 80 of that network in the web browser
					Desktop.getDesktop().browse(java.net.URI.create("http://localhost:80/network/" + ip));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void addServer(int server) {

		Polygon poly = new Polygon();
		polygons.add(poly);
		poly.getPoints().addAll(new Double[] { -8.0, -8.0, -8.0, 8.0, 8.0, 8.0, 8.0, -8.0 });
		poly.setFill(Paint.valueOf("green"));
		poly.setStroke(Paint.valueOf("black"));

		poly.setLayoutX(circles.get(circles.size() - 1).getCenterX() - (Math.sin(Math.toRadians(360 / serverLimit * server)) * radius));
		poly.setLayoutY(circles.get(circles.size() - 1).getCenterY() - (Math.cos(Math.toRadians(360 / serverLimit * server)) * radius));

		Controller.regionView.get(region).getChildren().add(poly);
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

	public Vector<Server> getServers() {
		return servers;
	}

	public void setServers(Vector<Server> server) {
		this.servers = server;
	}

	public static Vector<Network> getNetworks() {
		return networks;
	}

	public static void setNetworks(Vector<Network> network) {
		Network.networks = network;
	}

	public int getStance() {
		return stance;
	}

	public void setStance(int stance) {
		this.stance = stance;
	}

	public Vector<String> getPorts() {
		return ports;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public static int getRadius() {
		return radius;
	}
}
