package hakd.network.serverparts;

import java.lang.Math;
import java.util.Vector;

public class Motherboard{

	// stats	// buy new motherboard with more slots, network.server[4].motherboard[2].setCpus
	private int network;
	private int server;
	private int level;

	private String brand; // for example bell
	private String model;

	private int cpuSockets; // easier than using a for loop to count the amount, just remember to change this value
	private int memorySlots;
	private int storageSlots;
	private int gpuSlots;

	// objects
	private Vector<Cpu> cpu = new Vector<Cpu>(1,1);
	private Vector<Gpu> gpu = new Vector<Gpu>(1,1);
	private Vector<Memory> memory = new Vector<Memory>(1,1);
	private Vector<Storage> storage = new Vector<Storage>(1,1);

	public Motherboard(int networkId, int id){ // network // on create cpus = network[whatever].getcpus and other parts
		network = networkId;
		server = id;
		level = 7;//Network.network[network].getLevel();

		switch(level){
		case 0:
			cpuSockets = (level+1)+((int)(Math.random()*2));
			memorySlots = (level+1)+((int)(Math.random()*2));
			storageSlots = (level+1)+((int)(Math.random()*2));
			gpuSlots = (level+1)+((int)(Math.random()*2));
			break;
		case 7:
			cpuSockets = (level+1)+(((int)(Math.random()*2))-1);
			memorySlots = (level+1)+(((int)(Math.random()*2))-1);
			storageSlots = (level+1)+(((int)(Math.random()*2))-1);
			gpuSlots = (level+1)+(((int)(Math.random()*2))-1);
			break;
		default:
			cpuSockets = (level+1)+(((int)(Math.random()*3))-1);
			memorySlots = (level+1)+(((int)(Math.random()*3))-1);
			storageSlots = (level+1)+(((int)(Math.random()*3))-1);
			gpuSlots = (level+1)+(((int)(Math.random()*3))-1);
			break;
		}
	}

	public void populate(int id){
		for(int i=0; i<cpuSockets;i++){
			cpu.add(new Cpu());
		}
		for(int i=0; i<gpuSlots;i++){
			gpu.add(new Gpu());
		}
		for(int i=0; i<memorySlots;i++){
			memory.add(new Memory());
		}
		for(int i=0; i<storageSlots;i++){
			storage.add(new Storage());
		}
		return;
	}

	public boolean addProgram(String name, int size){ // TODO test this
		for(int i=0; i<storage.size(); i++){
			if(storage.get(i).getData().indexOf(name) == -1){
				break;
			}else if(storage.get(i).getData().size() <= storage.get(i).getCapacity()){
				for(int j=0; j<size; j++){
					storage.get(i).getData().add(name);
				}
			}
			
		}
		return false; // could not install
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
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

	public int getCpuSockets() {
		return cpuSockets;
	}

	public void setCpuSockets(int cpuSockets) {
		this.cpuSockets = cpuSockets;
	}

	public int getMemorySlots() {
		return memorySlots;
	}

	public void setMemorySlots(int memorySlots) {
		this.memorySlots = memorySlots;
	}

	public int getStorageSlots() {
		return storageSlots;
	}

	public void setStorageSlots(int storageSlots) {
		this.storageSlots = storageSlots;
	}

	public int getGpuSlots() {
		return gpuSlots;
	}

	public void setGpuSlots(int gpuSlots) {
		this.gpuSlots = gpuSlots;
	}

	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Vector<Cpu> getCpu() {
		return cpu;
	}

	public Vector<Gpu> getGpu() {
		return gpu;
	}

	public Vector<Memory> getMemory() {
		return memory;
	}

	public Vector<Storage> getStorage() {
		return storage;
	}
}