package com.github.rmirman.hakd;



public class RunGame implements Runnable {
	Thread t;
	public RunGame() {
		t = new Thread(this);
		t.start();
	}

	public void run() {
		boolean running = true;
		do{
			try {
				
				
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(running == true);
	}
}

