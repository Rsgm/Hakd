package hakd.userinterface.threads;

import hakd.userinterface.Controller;

public class RunGame implements Runnable {
	Thread t;
	private static boolean running = true;

	//private static DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

	public RunGame() {
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		do {
			//Controller.time.setText(dateFormat.format(new Date()));
			Controller.time.setVisible(true);
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Controller.time.setVisible(false);
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (running == true);
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		RunGame.running = running;
	}
}
