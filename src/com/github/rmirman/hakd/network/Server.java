package com.github.rmirman.hakd.network;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Server {

	// stats
	private int network; // which network a server belongs to
	private String ip; // network ip location
	private int motherboards; // how many motherboards this server can have, adds to the customization
	private int level;
	int webserver = 0;
	public String[][] ports = new String[65536][2];
	private String [][] logs = new String[100][2]; // (connecting from, action)
	
	private String brand; // for example bell
	private String model;
	
	// display
	private int rLocation; // radial location of this server on the network for the network map, 330/n for spacing, 330 to account for the modem space
	
	// objects
	private Motherboard[] motherboard = new Motherboard[8];
	
	// total stats?

	public Server(int networkId, int serverId){
		network = networkId;
		level = Network.network[network].getLevel();
		
		switch(level){
		case 0:
			motherboards = (int)(Math.random()*2+1);
			break;
		case 7:
			motherboards = (int)(Math.random()*2+7);
			break;
		default:
			motherboards = (int)(Math.random()*3+level);
			break;
		}
	}

	void populate(){
		for(int i=0, m=0; m<motherboards;i++){
			if(motherboard[i] == null){
				motherboard[i] = new Motherboard(network);
				motherboard[i].populate();
				m++;
			}
		}
		return;
	}

	public boolean connect(String address, String program, int port){
		
		for(int i=0; i<logs.length; i++)
			if(logs[i][0] != null){
				logs[i][0] = address;
				logs[i][1] = "connected";
				break;
			}
		
		if(program.equals(ports[port][0])&&ports[port][1].equals("open")){
			switch(port){
			case 31337:
				// grant complete access
				// open ssh
				break;
			default: // 80, 443
				if(webserver > 0){ // find a way to open from the game directory so it doesnt have to be g:/files/...
					try {
						Desktop.getDesktop().browse(new File("/com/github/rmirman/hakd/web/files/" + webserver + ".html").toURI());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					try {
						Desktop.getDesktop().browse(new File("/com/github/rmirman/hakd/web/files/error.html").toURI());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return false;

	}



	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getrLocation() {
		return rLocation;
	}

	public void setrLocation(int rLocation) {
		this.rLocation = rLocation;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getMotherboards() {
		return motherboards;
	}

	public void setMotherboards(int motherboards) {
		this.motherboards = motherboards;
	}

	public Motherboard[] getMotherboard() {
		return motherboard;
	}

	public void setMotherboard(Motherboard[] motherboard) {
		this.motherboard = motherboard;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}