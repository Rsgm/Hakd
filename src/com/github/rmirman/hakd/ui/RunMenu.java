package com.github.rmirman.hakd.ui;



public class RunMenu implements Runnable {
	Thread t;
	public RunMenu() {
		t = new Thread(this);
		t.start();
	}

	public void run() {
		boolean running = false;
		do {
			
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (running == true);

	}
}