package com.github.rmirman.hakd.network;

import java.lang.Math;

import com.github.rmirman.hakd.ui.UiController;


public class Network {

	// stats
	//private String isp; // for example infinity LTD.
	private int level; // 0-7, 0 for player because you start with almost nothing // remember that there are 8 of most objects and object amounts start at 1 unlike level or arrays
	private String ip; // all network variables will be in IP format
	private String owner; // owner, company, player
	private int servers; // amount of server objects to make
	private int modems; // amount of modem objects to make
	private int networkId;
	private String connectedTo = null;
	public String[][] ports = new String[65536][3]; // this has two extra ports not used in real life, 0 and 65535

	// display
	private int region; // where the network is in the world
	private int xCoordinate; // where the network is in the region/map
	private int yCoordinate;

	//objects
	private String[][] networkList = new String[8][2]; // holds each server/modems ip
	private Server[] server = new Server[5];
	private Modem[] modem = new Modem[3];
	public static Network[] network = new Network[50];
	
	

	public Network(int id, String type){ // make a network array in a region class
		networkId = id;
		level = (int)(Math.random()*8);

		switch(type){
		case "new player":	// only happens at the start of the game
			region = 0;
			ip = Dns.assignIp(region);
			level = 0;
			owner = "rsgm";
			servers = 1;
			modems = 1;
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

	public void populate(){
		for(int i=0, m=0; m<modems;i++){ // create modems on the network and gives them an ip
			if (networkList[i][1] == null){
				modem[m] = new Modem(networkId);
				networkList[i][0] = "192.168.1." + (i+1);
				networkList[i][1] = "modem[" + m + "]";
				m++;
			}
		}
		for(int i=0, s=0; s<servers;i++){ // create servers on the network and gives them an ip
			if (networkList[i][1] == null){
				server[s] = new Server(networkId, s);
				networkList[i][0] = "192.168.1." + (i+1);
				networkList[i][1] = "server[" + s + "]";
				server[s].setIp(networkList[i][0]);
				server[s].populate();
				s++;
			}
		}
		UiController.addNetwork(region, networkId);
	}


	public void removeDevice(String oldIp){ // removes a device, like from 3 servers to 2
		for(int i=0; i<networkList.length; i++ ){
			if (networkList[i][0] == oldIp){
				networkList[i][1] = null;
				break;
			}
		}
	}
	
	public boolean connect(String address, String program, int port){
		if(program.equals(ports[port][0])&&ports[port][2].equals("open")){
			return (Network.network[networkId].server[Integer.parseInt(ports[port][1])].connect(address, program, port));
		}else{
			return false;
		}
	}
	
	//public void connect(String connectTo, String connectFrom){ //get the ip from the url and connect to the network using it
	//	if(ip == network) ip connect to default server or lowest server ip // or random server
	//}
	
	
	
	
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

	public String[][] getNetworkList() {
		return networkList;
	}

	public void setNetworkList(String[][] networkList) {
		this.networkList = networkList;
	}

	public Server[] getServer() {
		return server;
	}

	public void setServer(Server[] server) {
		this.server = server;
	}

	public Modem[] getModem() {
		return modem;
	}

	public void setModem(Modem[] modem) {
		this.modem = modem;
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
	
}
