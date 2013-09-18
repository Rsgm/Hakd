package hakd.game;

import hakd.gui.windows.server.Terminal;
import hakd.networks.devices.Device;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Command {
	private String input;
	private final Device device;
	private final Terminal terminal;
	private PrintWriter commandLog;
	private Thread t;
	private final Queue<Integer> userInputBuffer;

	/**
	 * Runs the desired command on a separate thread. Not that any program would
	 * be intensive at all, but sleep(n) or large iterations will not lock up
	 * the game.
	 */
	public Command(String input, Device device, Terminal terminal) {
		this.input = input;
		this.device = device;
		this.terminal = terminal;

		userInputBuffer = new ConcurrentLinkedQueue<Integer>();

		File f = new File("TerminalLog.txt");
		if(!f.exists()) {
			try {
				if(!f.createNewFile()) {
					throw new IOException();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		run();
	}

	private void run() {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				List<String> parameters = new ArrayList<String>();
				try {
					commandLog = new PrintWriter(new File("TerminalLog.txt"));
				} catch(FileNotFoundException e1) {
					e1.printStackTrace();
				}

				while(input.matches("\\s*[(?:\".*?\")|\\S+].*")) {
					if(input.startsWith(" ")) {
						input = input.replaceFirst("\\s+", "");
					}

					String inputTemp = input;
					input = input.replaceFirst("(?:\".*?\")|\\S+", "");
					int l = input.length();

					String next = inputTemp.substring(0, inputTemp.length() - l);
					parameters.add(next);
				}

				System.out.println(parameters.toString());
				commandLog.println(parameters.toString());
				if(!parameters.isEmpty()) {
					try {
						runPython(parameters);
					} catch(FileNotFoundException e) {
						System.out.println("Error: FileNotFound");
						commandLog.println("Error: FileNotFound");

						// } catch (PyException e) {
						// System.out.println("python had an error");
					}
				}
				commandLog.println();
				commandLog.flush();
				commandLog.close();

				terminal.setCommand(null);
			}
		});
		// t.setName(name); might want to change this
		t.start();
	}

	/**
	 * Stops the command. This uses the deprecated Thread.stop(), which means it
	 * will stop it no matter what. Normally this is very bad to do because it
	 * will be in the middle of something. This is realistic so I will leave it,
	 * there is also no better way.
	 */
	@SuppressWarnings("deprecation")
	public void stop() {
		commandLog.close();
		t.stop();
		terminal.setCommand(null);
	}

	private void runPython(List<String> parameters) throws FileNotFoundException {
		PythonInterpreter pi = new PythonInterpreter();

		File[] files = new File("python/programs/").listFiles();
		File file = null;

		assert files != null;
		for(File f : files) {
			if(f.getName().equals(parameters.get(0) + ".py")) {
				file = f;
			}
		}

		if(file == null || !file.exists()) {
			throw new FileNotFoundException();
		}
		System.out.println(file.getPath());

		if(parameters.size() > 1) {
			parameters.remove(0); // first parameter is always the command
			pi.set("parameters", parameters);
		}
		pi.set("terminal", terminal);

		// there really is no good way of doing this
		// if (!checkPythonForCheats(file)) {
		// return;
		// }

		pi.execfile(file.getPath());
	}

	private boolean checkPythonForCheats(File file) {
		Scanner s;
		try {
			s = new Scanner(file);

			String line;
			while(s.hasNext("from")) {
				line = s.nextLine();

				// this may not work for device.addPart and other device methods
				if(line.startsWith("from hakd") && !line.matches("^from hakd\\.game\\.pythonapi.+")) {
					return false;
				}
			}

		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}

	public Queue<Integer> getUserInputBuffer() {
		return userInputBuffer;
	}
}