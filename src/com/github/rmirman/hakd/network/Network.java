package com.github.rmirman.hakd.network;

import java.util.Vector;

import com.github.rmirman.hakd.gameplay.PlayerController;



public class Network { // todo make all objects vectors

	// stats
	//private String isp; // for example infinity LTD.
	private int level; // 0-7, 0 for player because you start with almost nothing // remember that there are 8 of most objects and object amounts start at 1 unlike level or arrays
	private String ip; // all network variables will be in IP format
	private String owner; // owner, company, player
	private int servers; // amount of server objects to make
	private int modems; // amount of modem objects to make
	private int networkId;
	private int circleId;
	private int stance; // friendly, enemy, nutral
	private String connectedTo = null;
	private Vector<String> ports = new Vector<String>(1, 1); // port, program, server

	// display
	private int region; // where the network is in the world
	private int xCoordinate; // where the network is in the region/map
	private int yCoordinate;

	//objects
	private Vector<Server> server = new Vector<Server>(1, 1);
	private Vector<Modem> modem = new Vector<Modem>(1, 1);
	private static Vector<Network> network = new Vector<Network>(1, 1);



	public Network(String type){ // make a network array in a region class
		networkId = network.size();
		level = (int)(Math.random()*8);
		stance = 1;

		switch(type){
		case "new player":	// only happens at the start of the game
			region = 0;
			ip = Dns.assignIp(region);
			level = 0;
			owner = "rsgm";
			servers = 1;
			modems = 1;
			stance = 0;
			PlayerController.setHomeNetwork(ip);
			PlayerController.setCurrentNetwork(ip);
			PlayerController.setCurrentServer(0);
			break;
		case "company":
			region = 4;
			ip = Dns.assignIp(region);
			owner = "company"; // random company name // company.assignName();
			servers = (int)(Math.random()*3+3); // 3-5
			modems = (int)(Math.random()*2+1); // 1-2
			level = (int)(Math.random()*8);
			break;

		case "test":
			region = 0;
			ip = Dns.assignIp(region);
			owner = "test";
			servers = 5;
			modems = 3;
			level = 7;
			break;
		}
	}

	public void populate(){ // populates the network after the network is created so the network devices can get variables from the network

		for(int i=0; i<modems;i++){ // create modems on the network
			modem.add(new Modem(networkId));
		}
		for(int i=0; i<servers;i++){ // create servers on the network
			server.add(new Server(networkId));
			server.get(server.size()-1).populate(server.size()-1);
		}
		//UiController.addNetwork(networkId);
	}

	public boolean connect(String address, String program, int port){
		if(program.equals(ports.get(port*3))&&ports.get(port*3+2).equals("open")){
			return (server.get(Integer.parseInt(ports.get(port+1))).connect(address, program, port));
		}else{
			return false;
		}
	}

	public void testArray(){
		for(int i=0; i<ports.size()-1; i++){
			ports.add(i*3, i + "");
		}
	}


	public boolean setPorts(int port, String program, int server){
		for(int i=0; i<ports.size()-1; i+=3){
			if (ports.get(i).equals(port + "")){
				return false;
			}
		}
		ports.add(port + "");
		ports.add(program);
		ports.add(server + "");
		return true;
	}
	
	public boolean removePort(int port){
		for(int i=0; i<ports.size()-1; i+=3){
			if (ports.get(i).equals(port + "")){
				ports.remove(i+2);
				ports.remove(i+1);
				ports.remove(i);
				return true;
			}
		}
		return false;
	}


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

	public int getModems() {
		return modems;
	}

	public void setModems(int modems) {
		this.modems = modems;
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

	public Vector<Modem> getModem() {
		return modem;
	}

	public void setModem(Vector<Modem> modem) {
		this.modem = modem;
	}

	public static Vector<Network> getNetwork() {
		return network;
	}

	public static void setNetwork(Vector<Network> network) {
		Network.network = network;
	}

	public int getCircleId() {
		return circleId;
	}

	public void setCircleId(int circleId) {
		this.circleId = circleId;
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
}
