package hakd.network.serverparts;

public class Memory {

	// stats
	private int	network;
	private int	server;
	private int	level;

	private int	brand;
	private int	model;

	private int	speed;
	private int	capacity;

	public Memory(int level, int network, int server) {
		this.level = level; // (int)(Math.random()*7); //Network.network[network].getLevel();
		this.network = network;
		this.server = server;

		switch (level) {
			case 0:
				capacity = 1 + (int) (Math.random() * 3 - 3);
				break;
			default:
				capacity = (int) Math.pow(2, (level + 1) / 2 + ((int) (Math.random() * 3 + 1) - 2));
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getBrand() {
		return brand;
	}

	public void setBrand(int brand) {
		this.brand = brand;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
