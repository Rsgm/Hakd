package com.github.rmirman.hakd.network;

import java.lang.Math;

public class Server {

	// stats
	private int network; // which network a server belongs to
	private String ip; // network ip location
	private int motherboards; // how many motherboards this server can have, adds to the customization
	private int level; // level of the network
	
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