package com.github.rmirman.hakd.network;


public class Modem {
	

	// location
	private int network;
	private int level;
	
	private String brand;
	private String model;
	
	//display
	//private int modemSpot;
	
	// stats
	private int speed; // in Mb per second, may want to change it to MBps


	public Modem(int networkId){
		network = networkId;
		level = Network.network[network].getLevel();
		
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setNetwork(int network) {
		this.network = network;
	}
	
	public int getNetwork() {
		return network;
	}
}