package com.github.rmirman.hakd;

import com.github.rmirman.hakd.network.Network;




public class RunGame implements Runnable {
	Thread t;
	public RunGame() {
		t = new Thread(this);
		t.start();
	}
	
	public void run() {
		boolean running = true;
		
		for(int i=0; i<15; i++){
			Network.getNetwork().add(new Network(2));
			Network.getNetwork().get(Network.getNetwork().size()-1).populate();
		}
		
		do{
			
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}while(running == true);
	}
}
