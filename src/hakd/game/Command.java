package hakd.game;

import hakd.gui.windows.server.Terminal;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Command {
    private final String input;
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

		Scanner scanner = new Scanner(input);
		List<String> args = new ArrayList<String>();
		boolean inQuotes = false; // credit to redditor BritPack for
					  // this, thank you

		if (input.matches(device.getNetwork().getIp() + ">.+")) {
		    scanner.skip(".+>");
		    scanner.useDelimiter("\\s+");

		    while (scanner.hasNext()) {
			// Get the current token
			String next = scanner.next();
			// Are we inside quotes
			if (inQuotes) {
			    // If so, add this string to the end of the last
			    // value
			    int offset = args.size() - 1;
			    args.set(offset, args.get(offset) + " " + next);
			    // If it ends in a quotation then we exit quotes
			    if (next.endsWith("\"")) {
				inQuotes = false;
			    }
			} else {
			    // Add the string to the values
			    args.add(next);
			    // Are we moving into quotes?
			    if (next.startsWith("\"")
				    && scanner.hasNext("\\S*\"")) {
				inQuotes = true;
			    }
			}
		    }

		    // run(args);
		    System.out.println(args.toString());
		}
		scanner.close();
	    }
	});
    }
}