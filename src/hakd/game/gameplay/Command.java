package hakd.game.gameplay;

import hakd.game.Hakd;
import hakd.gui_old.GuiController;
import hakd.internet.NetworkController;
import hakd.networks.Network;
import hakd.other.enumerations.NetworkType;

import java.util.ArrayList;
import java.util.Scanner;

public class Command {
	String	input;

	public Command(String input) { // this may need to tell if a player issued it, so it won't write to the display
		this.input = input;
	}

	public void run() { // TODO use enumerations // picks the desired command
		// TODO add scan(ports), cd(directory), cs(int server)/*change server*/, traceroute(address), clone server to server(maybe just hard drive to hard drive)
		Scanner scanner = new Scanner(input);
		ArrayList<String> args = new ArrayList<String>();

		if (input.matches(">.+")) {
			scanner.useDelimiter("\\s+");
			scanner.skip(".+>");
			for (int i = 0; i < input.length(); i++) {
				if (scanner.hasNext()) {
					args.add(scanner.next());
				} else {
					break;
				}
			}

			switch (args.get(0)) { // change all scanner groups to args
				default:
					GuiController.terminalDisplay.setText(GuiController.terminalDisplay.getText() + "\n'" + args.get(0)
							+ "' is not a recognized command");
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
				case "run": // runs a lua script program, can I do it without using the word "run"
					break;
				case "debug":
					hakd.gui.Windows.debug(args.get(1));
					break;
			}
		} else if (input.equals(GuiController.getIp())) {
		} else {
			GuiController.terminalDisplay.setText(GuiController.terminalDisplay.getText() + "\n" + "please do not modify the address");
		}
		scanner.close();
	}

	public static void ctfCommands(String input) {
	}

	// --------commands-------- // runs the code of the command
	private static void help() {
		GuiController.terminalDisplay.setText(GuiController.terminalDisplay.getText() + "\nCommands:" + "\nquit - stops the game"
				+ "\nadd - adds a server" + "\ntest - outputs the current amount of networks in the dns"
				+ "\nhome - sets the current network and server to the homene twork and server 0"
				+ " \ndebug [address] - adds variables of the network at that address to the debug tab");
	}

}