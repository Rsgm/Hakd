package hakd.gui.threads;

public class RunGame implements Runnable {
	Thread					t;
	private static boolean	running	= true;

	// private static DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

	public RunGame() {
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		do {
			// String s = dateFormat.format(new Date());
			// GuiController.time.setText(s); // TODO fix this
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
