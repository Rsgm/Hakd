package com.github.rmirman.hakd;

import com.github.rmirman.hakd.network.Dns;
import com.github.rmirman.hakd.network.Network;


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

