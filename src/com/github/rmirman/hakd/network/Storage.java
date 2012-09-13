package com.github.rmirman.hakd.network;

import java.lang.Math;

public class Storage {
	
	// stats
	private int network;
	private int server;
	private int motherboard;
	private int level;
	
	private boolean ssd; // doubles the speed
	private int speed; // in MB/s		// what do i use speed for? an extra factor in start up times(for security machines?), copying/pasting files
	private int capacity; // in GB
	
	private String brand;
	private String model;
	
	// data
	private String[] data;
	
	
	public Storage(){
		level = 7; // (int)(Math.random()*7); //Network.network[network].getLevel();
		
		switch(level){
		case 0:
			speed = (level+1)*30+(int)(Math.random()*30);
			capacity = (int) Math.pow(2, (level+4)+(int)(Math.random()*3-1));
		default:
			speed = (level+1)*30+((int)(Math.random()*60-30));
			capacity = (int) Math.pow(2, (level+4)+(int)(Math.random()*3+1)-2); // start at 16 GB at level 1 and make sure the OS takes up 15 GB
		}
		if (ssd == true){
			speed *= 2;
			capacity /= 2;
		}
		data = new String[capacity];
	}
	
	public boolean addProgram(String name, int size){
		for(int i=0; i<(data.length-size); i++){	// problem when a program is for e.g. 4 GB and i = 3
			for(int j=0; j<size;){
				if(j==(size-1) && data[i+j]==null){
					for(int k=0; k<size; k++){
						data[i+k] = name;
					}
					return true;
				}else if(data[i+j] == null && j!=size /* -1? */ ){
					j++;
				}
				else if(data[i+j] != null){
					break;
				}
			}
		}
		return false;
	}
	
	public boolean findProgram(String name){
		for(int i=0;i<data.length;i++){
			if(data[i] == name){
				return true;
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

	public boolean isSsd() {
		return ssd;
	}

	public void setSsd(boolean ssd) {
		this.ssd = ssd;
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}
