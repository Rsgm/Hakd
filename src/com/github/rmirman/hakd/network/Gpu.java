package com.github.rmirman.hakd.network;

import java.lang.Math;

public class Gpu {

	// stats
	private int network;
	private int server;
	private int motherboard;
	private int level;

	private int speed; // in MHz

	private String brand;
	private String model;

	public Gpu(){
		level = 7; // (int)(Math.random()*7); //Network.network[network].getLevel();

		switch(level){
		case 0:
			speed = (int)(Math.random()*200+100);
			break;
		default:
			speed = (level+1)*150+(int)(Math.random()*400-200);
			break;
		}
	}

	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}

	public int getMotherboard() {
		return motherboard;
	}

	public void setMotherboard(int motherboard) {
		this.motherboard = motherboard;
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
	
}
