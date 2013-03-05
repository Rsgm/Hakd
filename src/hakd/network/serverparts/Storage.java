package hakd.network.serverparts;

import java.util.ArrayList;

public class Storage {

	// stats
	private int		network;
	private int		server;
	private int		motherboard;
	private int		level;

	private boolean	ssd;									// doubles the speed
	private int		speed;									// in MB/s // what do i use speed for? an extra factor in start up times(for security
	// machines?), copying/pasting files
	private int		capacity;								// in GB

	private String	brand;
	private String	model;

	// storage ArrayLists
	ArrayList<File>	osFiles		= new ArrayList<File>();	// operating system files, !FUN!.
	ArrayList<File>	userFiles	= new ArrayList<File>();	// random files people save
	ArrayList<File>	programFiles			= new ArrayList<File>();	// (lua)programs able to run // is this copywritten?(will Microsoft sue me?)

	public Storage(int level, int network, int server) {
		this.level = level; // (int)(Math.random()*7); //Network.network[network].getLevel();
		this.network = network;
		this.server = server;

		switch (level) {
		case 0:
			speed = (level + 1) * 30 + (int) (Math.random() * 30);
			capacity = (int) Math.pow(2, (level + 4) + (int) (Math.random() * 3 - 1));
		default:
			speed = (level + 1) * 30 + ((int) (Math.random() * 60 - 30));
			capacity = (int) Math.pow(2, (level + 4) + (int) (Math.random() * 3 + 1) - 2); // start at 16 GB at level 1 and make sure the OS takes
			// up 15 GB
			if (1 == (int) ((Math.random() * 30) + 1)) {
				ssd = true;
				speed *= 2;
				capacity /= 2;
			}
		}

	}

	public void addProgram(int size, String name, String data, String type) {
		userFiles.add(new File(size, name, data, type));
	}

	public void removeProgram(int directory, String name){// removes the first file with the specified name from the specified directory(0==os, 1==user, 2==bin)
		switch (directory) {
		case 0:
			for(File f : osFiles){
				if(f.getName()==name){
					osFiles.remove(f);
				}
			}
			break;
		default:
			for(File f : userFiles){
				if(f.getName()==name){
					userFiles.remove(f);
				}
			}
			break;
		case 2:
			for(File f : programFiles){
				if(f.getName()==name){
					programFiles.remove(f);
				}
			}
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

	public ArrayList<File> getOsFiles() {
		return osFiles;
	}

	public void setOsFiles(ArrayList<File> osFiles) {
		this.osFiles = osFiles;
	}

	public ArrayList<File> getUserFiles() {
		return userFiles;
	}

	public void setUserFiles(ArrayList<File> userFiles) {
		this.userFiles = userFiles;
	}

	public ArrayList<File> getBin() {
		return programFiles;
	}

	public void setBin(ArrayList<File> bin) {
		this.programFiles = bin;
	}
}
