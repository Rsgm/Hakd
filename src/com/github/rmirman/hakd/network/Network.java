package com.github.rmirman.hakd.network;

import java.util.Vector;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import com.github.rmirman.hakd.gameplay.PlayerController;
import com.github.rmirman.hakd.ui.UiController;

public class Network { // todo make all objects vectors
	// stats
//	private String isp; // for example infinity LTD.
	private int level; // 0-7, 0 for player because you start with almost nothing // remember that there are 8 of most objects and object amounts start at 1 unlike level or arrays
	private int speed; // in Mb per second, may want to change it to MBps
	private String ip; // all network variables will be in IP format
	private String owner; // owner, company, player
	private int servers; // amount of server objects to make
	private int networkId;
	private int stance; // friendly, enemy, nutral
	private String connectedTo = null;
	private Vector<String> ports = new Vector<String>(1, 1); // port, program, server

	// display

	// objects
	private Vector<Server> server = new Vector<Server>(1, 1);
	private static Vector<Network> network = new Vector<Network>(1, 1);

	// user interface
	private int region; // where the network is in the world
	private int xCoordinate; // where the network is in the regionTab/map
	private int yCoordinate;
	//private Vector<String> 
	private Vector<Circle> circles = new Vector<Circle>(1, 1);
	private Vector<Polygon> polygons = new Vector<>(1, 1);


	//--------constructor--------
	public Network(int type){ // make a network array in a regionTab class
		networkId = network.size();
		level = (int)(Math.random()*8);
		stance = 1;

		switch(type){
		case 0:// new player // only happens at the start of the game
			region = 0;
			ip = Dns.assignIp(region);
			level = 0;
			servers = 1;
			stance = 0;
			PlayerController.setHomeNetwork(ip);
			PlayerController.setCurrentNetwork(ip);
			PlayerController.setCurrentServer(0);
			break;
		case 1: // company // random company name // company.assignName();
			region = 4;
			ip = Dns.assignIp(region);
			servers = (int)(Math.random()*3+3); // 3-5
			level = (int)(Math.random()*8);
			break;
		case 2:
			region = 0;
			ip = Dns.assignIp(region);
			owner = "test";
			servers = 5;
			level = 7;
			break;
		}
	}

	//--------methods--------
	public void populate(){ // populates the network after the network is created so the network devices can get variables from the network
		for(int i=0; i<servers;i++){ // create servers on the network
			server.add(new Server(networkId));
			server.get(server.size()-1).populate(server.size()-1);
		}
		addNetwork(networkId);
	}

	public static boolean connect(String fromAddress, String toAddress, String program, int portInt){ // TODO fix this some how
		int id = Dns.findNetwork(toAddress);
		String port = portInt + "";
		if(network.get(id).ports.get(network.get(id).ports.indexOf(port)+1).equals(program)&&Dns.findNetwork(fromAddress) != -1){
			return (network.get(id).server.get(Integer.parseInt(network.get(id).ports.get(network.get(id).ports.indexOf(port)+2))).connect(fromAddress, program, port));
		}
		return false;
	}

	public boolean addPorts(int port, String program, int server){
		if(ports.indexOf(ports) == -1){
			ports.add(port + "");
			ports.add(program);
			ports.add(server + "");
			return true;
		}
		return false;
	}

	public boolean removePort(int port){
		int index = ports.indexOf(port);
		if (ports.indexOf(port) != -1){
			ports.remove(index+2);
			ports.remove(index+1);
			ports.remove(index);
			return true;
		}
		return false;
	}	

	private synchronized void addNetwork(int network){
		boolean taken = false;
		circles.add(new Circle());

		UiController.regionView.get(region).getChildren().add(circles.get(circles.size()-1));

		do{
			circles.get(circles.size()-1).setCenterX((int)(Math.random()*600+40)); // TODO change this
			circles.get(circles.size()-1).setCenterY((int)(Math.random()*500+40));
			
			for(int i=0; i<circles.size()-2; i++){ // TODO change this to if within x+2r of another network or x+r of a side choose a new one
				if(circles.get(i).getLayoutX() != circles.get(circles.size()-1).getLayoutX()||circles.get(i).getLayoutY() != circles.get(circles.size()-1).getLayoutY()){
					taken = false;
				}else{
					System.out.println("too close to another network");
					taken = true;
					break;
				}
			}
		}while(taken == true);

		for(int i=0; i<servers; i++){ // hide these before the network is scanned, maybe?
			polygons.add(new Polygon());
			polygons.get(polygons.size()-1).getPoints().addAll(new Double[]{
					-8.0, -8.0,
					-8.0, 8.0,
					8.0, 8.0,
					8.0, -8.0});
			polygons.get(polygons.size()-1).setFill(Paint.valueOf("green"));
			polygons.get(polygons.size()-1).setStroke(Paint.valueOf("black"));
			
			polygons.get(polygons.size()-1).setLayoutX(circles.get(circles.size()-1).getCenterX() - (Math.sin(Math.toRadians(360/servers*i))*35));
			polygons.get(polygons.size()-1).setLayoutY(circles.get(circles.size()-1).getCenterY() - (Math.cos(Math.toRadians(360/servers*i))*35));
			
			UiController.regionView.get(region).getChildren().add(polygons.get(polygons.size()-1));
		}
		
		circles.get(circles.size()-1).setRadius(35);

		switch(stance){
		case 0:
			circles.get(circles.size()-1).getStyleClass().add("friendly-network");
			break;
		case 1:
			circles.get(circles.size()-1).getStyleClass().add("nuetral-network");
			break;
		case 2:
			circles.get(circles.size()-1).getStyleClass().add("enemy-network");
			break;
		default:
			circles.get(circles.size()-1).getStyleClass().add("nuetral-network");
			break;
		}
		Tooltip.install(circles.get(circles.size()-1), new Tooltip(ip + "\n" + owner));

		xCoordinate = (int) circles.get(circles.size()-1).getLayoutX();
		yCoordinate = (int) circles.get(circles.size()-1).getLayoutY();


		circles.get(circles.size()-1).setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				System.out.println("network " + ip + " " + owner);
			}
		});
	}

	//--------getters/setters--------
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

	public int getServers() {
		return servers;
	}

	public void setServers(int servers) {
		this.servers = servers;
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

	public Vector<Server> getServer() {
		return server;
	}

	public void setServer(Vector<Server> server) {
		this.server = server;
	}

	public static Vector<Network> getNetwork() {
		return network;
	}

	public static void setNetwork(Vector<Network> network) {
		Network.network = network;
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

}
