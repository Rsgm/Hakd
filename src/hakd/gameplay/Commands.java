package hakd.gameplay;

import hakd.Hakd;
import hakd.gui.GuiController;
import hakd.networking.Dns_old;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Commands {
	private static int					recall	= 0;						// holds the position of the ArrayList
	private static ArrayList<String>	lines	= new ArrayList<String>();	// holds previously used commands for easy access

	public static void inputHandler(KeyEvent input) {
		if (input.getCode() == KeyCode.ENTER) {
			lines.add(GuiController.terminalInput.getText());
			GuiController.terminalDisplay.setText(GuiController.terminalDisplay.getText() + "\n" + GuiController.terminalInput.getText());
			System.out.println(GuiController.terminalInput.getText());
			commands(GuiController.terminalInput.getText());
			GuiController.terminalInput.setText(GuiController.getIp());
			recall = lines.size();
		} else if (input.getCode() == KeyCode.DOWN && recall < lines.size() - 1) {
			recall++;
			GuiController.terminalInput.setText(lines.get(recall));
			GuiController.terminalInput.end();
		} else if (input.getCode() == KeyCode.UP && recall > 0) {
			recall--;
			GuiController.terminalInput.setText(lines.get(recall));
			GuiController.terminalInput.end();
		} else if (input.getCode() == KeyCode.ESCAPE) {
			recall = lines.size();
			GuiController.terminalInput.setText(GuiController.getIp());
		} else {
			return;
		}
		GuiController.terminalDisplay.end();
		GuiController.terminalInput.end();
	}

	public static void commands(String input) { // picks the desired command
		// TODO add scan(ports), cd(directory), cs(int server)/*change server*/, traceroute(address), clone
		// server to server(maybe just hard drive to hard drive)
		Scanner scanner = new Scanner(input);
		ArrayList<String> args = new ArrayList<String>();

		if (input.matches(GuiController.getIp() + ".+")) {
			scanner.useDelimiter("\\s+");
			scanner.skip(".+>");
			for (int i = 0; i < input.length(); i++) {
				if (scanner.hasNext()) {
					args.add(scanner.next());
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
					Network.getNetworks().add(new Network(1));
					Network.getNetworks().get(Network.getNetworks().size() - 1).populate();
					break;
				case "quit":
					Hakd.quitGame(null); // TODO this will give a null pointer
					break;
				case "test":
					System.out.println(Dns_old.getDnsList().size());
					break;
				case "home":
					PlayerController.setCurrentNetwork(PlayerController.getHomeNetwork());
					PlayerController.setCurrentServer(PlayerController.getHomeNetwork().getServers().get(0));
					PlayerController.updateCurrentIp();
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