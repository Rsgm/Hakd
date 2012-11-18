package hakd.network.serverparts;

import java.lang.Math;

public class Cpu{
	//does it need a location if its created from another object?
	
	// stats
	private int network;
	private int server;
	private int motherboard;
	private int level;
	
	private int speed; // in MHz, 3.5GHz -> 3500MHz
	private int cores;
	
	private String brand;
	private String model;
	
	public Cpu(){ // 0=default settings, 1=random low settings, 2=random medium, 3=random high, 4=testing
		level = 7; // (int)(Math.random()*7); //Network.network[network].getLevel();
		
		switch(level){
		case 0:
			speed = ((int)(Math.random()*5+1))*105+100;
			cores = 1;
			break;
		default:
			speed = (level+1)*625+(((int)(Math.random()*400+1))*5-1000);
			if(level>=4){
				cores = (int) Math.pow(2, (level-3)-((int)(Math.random()*2)));
			}else{
				cores = 1;
			}
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

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
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