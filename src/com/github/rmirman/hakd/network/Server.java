package com.github.rmirman.hakd.network;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import com.github.rmirman.hakd.network.serverparts.Motherboard;

public class Server {

	// stats
	private int network; // which network a server belongs to
	private int motherboards; // how many motherboards this server can have, adds to the customization
	private int level;
	private int webserver = 0; // 0 = 404
	private Vector<String> ports = new Vector<String>(1, 1); // port, program / if its closed just delete it
	private Vector<String> logs = new Vector<String>(0, 1); // connecting from and the action after that

	private String brand; // for example bell
	private String model;

	// display
	private int rLocation; // radial location of this server on the network for the network map, 330/n for spacing, 330 to account for the modem space

	// objects
	private Vector<Motherboard> motherboard = new Vector<Motherboard>(1, 1);

	// total stats?

	//--------constructor--------
	public Server(int networkId){

		network = networkId;
		//level = Network.getNetwork().get(network).getLevel();

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

	//--------methods--------
	void populate(int id){
		for(int i=0; i<motherboards;i++){
			motherboard.add(new Motherboard(network, id));
			motherboard.get(motherboard.size()-1).populate(motherboard.size()-1);
		}
	}

	boolean connect(String address, String program, String port){
		logs.add(address);
		logs.add("connected " + program + " " + port);
		if (ports.get(ports.indexOf(port)+1).equals(program)){
			try{
				switch(port){
				default: // 80, 443, others but just get directed to a 404 if not a website
					if(webserver > 0){ // TODO find a way to open from the game directory so it doesnt have to be g:/files/...
						Desktop.getDesktop().browse(new File("/files/Hakd/src/com/github/rmirman/hakd/web/websites/" + webserver +".html").toURI());
					}else{
						Desktop.getDesktop().browse(new File("/files/Hakd/src/com/github/rmirman/hakd/web/websites/404.html").toURI());
					}
					break;
				case "31337":
					// grant complete(root?) access
					// open ssh
					break;
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} 
		return false;

	}

	public boolean addPorts(int port, String program){
		if(ports.indexOf(ports) == -1){
			ports.add(port + "");
			ports.add(program);
			return true;
		}
		return false;
	}

	public boolean removePort(int port){
		int index = ports.indexOf(port);
		if (ports.indexOf(port) != -1){
			ports.remove(index+1);
			ports.remove(index);
			return true;
		}
		return false;
	}

	//--------getters/setters--------
	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Vector<String> getLogs() {
		return logs;
	}
}