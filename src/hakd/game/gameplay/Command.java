package hakd.game.gameplay;

import hakd.gui.Terminal;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Command {
	String		input;
	Player		player;
	Terminal	terminal;

	public Command(String input, Player player) { // this may need to tell if a player issued it, so it won't write to the display
		this.input = input;
		this.player = player;
		this.terminal = player.getTerminal();
	}

// runs the desired command on a separate thread I think, not that any program would be intensive at all, although while(true)...
	public void run() {
		Timer.post(new Task() {
			@Override
			public void run() {

				// TODO add scan(ports), cd(directory), cs(int server)/*change server*/, traceroute(address), clone server to server(maybe just hard
// drive to hard drive)

				Scanner scanner = new Scanner(input);
				ArrayList<String> args = new ArrayList<String>();

				if (input.matches(player.getCurrentNetwork().getIp() + ">.+")) {
					scanner.useDelimiter("\\s+");
					scanner.skip(".+>");
					for (int i = 0; i < input.length(); i++) {
						if (scanner.hasNext()) {
							args.add(scanner.next());
						} else {
							break;
						}
					}

					if (terminal.isMenu()) {
						args.set(0, "menu-" + args.get(0)); // TODO what to do?
					}

					Programs.run(args);

					/*switch (args.get(0)) { // change all scanner groups to args
						default:
							terminal.addText("'"+ args.get(0)+ "' is not a recognized command");
							break;
						case "help":
							help();
							break;
						case "add":
							NetworkController.getNetworks().add(new Network(NetworkType.NPC));
							break;
						case "quit":
							Hakd.quitGame(null); // TODO this will give a null pointer
							break;
						case "test":
							System.out.println("Total networks - " + NetworkController.getNetworks().size());
							break;
						case "home":
							PlayerController.setCurrentNetwork(PlayerController.getHomeNetwork());
							PlayerController.setCurrentServer(PlayerController.getHomeNetwork();
							break;
						case "run": // runs a lua script program, can I do it without using the word "run"? yes, make all commands lua
							break;
						case "debug":
							hakd.gui.Windows.debug(args.get(1));
							break;}*/
				}

				scanner.close();
			}
		});
	}
}