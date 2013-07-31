package hakd.game;

import hakd.gui.windows.server.Terminal;
import hakd.networks.devices.Device;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.python.util.PythonInterpreter;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public final class Command {
    private String input;
    private final Device device;
    private final Terminal terminal;

    private final List<String> scriptPaths = new ArrayList<String>();

    public Command(String input, Device device) { // this may need to tell if a
						  // player issued it, so it
						  // won't write to the display
	this.input = input;
	this.device = device;
	this.terminal = device.getWindow().getTerminal();

	run();
    }

    // runs the desired command on a separate thread I think, not that any
    // program would be intensive at all, although while(true)...
    public void run() {
	Timer.post(new Task() {
	    @Override
	    public void run() {
		// TODO add scan(ports), cd(directory), cs(int server)/*change
		// server*/, traceroute(address), clone server to server(maybe
		// just hard
		// drive to hard drive)
		List<String> parameters = new ArrayList<String>();

		while (input.matches("\\s*[(?:\".*?\")|\\S+].*")) {
		    if (input.startsWith(" ")) {
			input = input.replaceFirst("\\s+", "");
		    }

		    String inputTemp = input;
		    input = input.replaceFirst("(?:\".*?\")|\\S+", "");
		    int l = input.length();

		    String next = inputTemp.substring(0, inputTemp.length() - l);
		    parameters.add(next);
		}

		System.out.println(parameters.toString());
		if (parameters != null && !parameters.isEmpty()) {
		    try {
			runPython(parameters);
		    } catch (IOError e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		    } catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		    }
		}
	    }
	});
    }

    private void runPython(List<String> parameters) {
	PythonInterpreter pi = new PythonInterpreter();

	File file = new File("python/" + parameters.get(0) + ".py");
	System.out.println(file.getPath());

	if (parameters.size() > 1) {
	    parameters.remove(0);
	    pi.set("parameters", parameters);
	}
	pi.set("device", device);

	// if (!checkPython(file)) {
	// return;
	// }

	// first parameter is always the command
	pi.execfile(file.getPath());
    }

    private boolean checkPython(File file) {
	Scanner s;
	try {
	    s = new Scanner(file);

	    String line;
	    while (s.hasNext("from")) {
		line = s.nextLine();

		// this may not work for device.addPart and other device methods
		if (line.startsWith("from hakd")
			&& !line.matches("^from hakd\\.game\\.pythonapi.+")) {
		    return false;
		}
	    }

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

	return true;
    }
}