package hakd.gui_old;

import hakd.Main;

public class UpdateThread implements Runnable {

	UpdateThread() {
		Thread t = new Thread();
		t.start();
	}

	@Override
	public void run() {
		do {// this updates the gui every 1/60 seconds(can be changed), and kind of runs the game
			GameGui.updateRegion();

			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
			}
		} while (Main.running == true);

	}

}
