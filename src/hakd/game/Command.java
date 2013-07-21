package hakd.game;

import hakd.gui.windows.server.Terminal;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;

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
		List<String> args = new ArrayList<String>();

		while (input.matches(".*?[(?:\".*?\")|\\S+].*")) {
		    if (input.startsWith(" ")) {
			input = input.replaceFirst("\\s+", "");
		    }

		    String inputTemp = input;
		    input = input.replaceFirst("(?:\".*?\")|\\S+", "");
		    int l = input.length();

		    String next = inputTemp.substring(0, inputTemp.length() - l);
		    args.add(next);
		}

		// run(args);
		System.out.println(args.toString());
	    }
	});
    }
}