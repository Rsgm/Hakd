package hakd.userinterface.threads;

public class RunMenu implements Runnable {
	Thread t;
	private static boolean running = true;

	public RunMenu() {

		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {

		do {

			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (running == true);
	}

	public static synchronized boolean isRunning() {
		return running;
	}

	public static synchronized void setRunning(boolean running) {
		RunMenu.running = running;
	}
}