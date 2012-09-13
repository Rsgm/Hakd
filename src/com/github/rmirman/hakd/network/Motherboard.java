package com.github.rmirman.hakd.network;

import java.lang.Math;

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
	private Cpu[] cpu = new Cpu[8];
	private Gpu[] gpu = new Gpu[8];
	private Memory[] memory = new Memory[8];
	private Storage[] storage = new Storage[8];

	public Motherboard(int networkId){ // network // on create cpus = network[whatever].getcpus and other parts
		network = networkId;
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

	public void populate(){
		for(int i=0; i<cpuSockets;i++){
			if(cpu[i] == null){
				cpu[i] = new Cpu();
			}
		}
		for(int i=0; i<gpuSlots;i++){
			if(gpu[i] == null){
				gpu[i] = new Gpu();
			}
		}
		for(int i=0; i<memorySlots;i++){
			if(memory[i] == null){
				memory[i] = new Memory();
			}
		}
		for(int i=0; i<storageSlots;i++){
			if(storage[i] == null){
				storage[i] = new Storage();
			}
		}
		return;
	}

	public boolean installProgram(String name, int size){
		for(int i=0; i<(storage.length-1);){
			if(storage[i] != null && storage[i].addProgram(name, size) == true){
				return true;
			}
			else break;
		}
		return false;
	}

	public boolean find(String name){
		if(name != null){
			return storage[0].findProgram(name);
		}
		return false;
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

	public Cpu[] getCpu() {
		return cpu;
	}

	public void setCpu(Cpu[] cpu) {
		this.cpu = cpu;
	}

	public Gpu[] getGpu() {
		return gpu;
	}

	public void setGpu(Gpu[] gpu) {
		this.gpu = gpu;
	}

	public Memory[] getMemory() {
		return memory;
	}

	public void setMemory(Memory[] memory) {
		this.memory = memory;
	}

	public Storage[] getStorage() {
		return storage;
	}

	public void setStorage(Storage[] storage) {
		this.storage = storage;
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
}