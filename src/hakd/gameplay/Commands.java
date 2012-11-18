package hakd.gameplay;

import hakd.Hakd;
import hakd.network.Dns;
import hakd.network.Network;
import hakd.network.Server;
import hakd.userinterface.Controller;

import java.util.Scanner;
import java.util.Vector;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Commands {
	private static int recall = 0;
	private static Vector<String> lines = new Vector<String>(0, 1);

	public static void inputHandler(KeyEvent input) {
		if (input.getCode() == KeyCode.ENTER) {
			lines.add(Controller.terminalInput.getText());
			Controller.terminalDisplay
			.setText(Controller.terminalDisplay.getText() + "\n" + Controller.terminalInput
					.getText());
			System.out.println(Controller.terminalInput.getText());
			Commands.commands(Controller.terminalInput.getText());
			Controller.terminalInput.setText(Controller.getIp());
			recall = lines.size();
		} else if (input.getCode() == KeyCode.DOWN && recall < lines.size() - 1) {
			recall++;
			Controller.terminalInput.setText(lines.get(recall));
		} else if (input.getCode() == KeyCode.UP && recall > 0) {
			recall--;
			Controller.terminalInput.setText(lines.get(recall));
		} else if (input.getCode() == KeyCode.ESCAPE) {
			recall = lines.size();
			Controller.terminalInput.setText(Controller.getIp());
		} else {
			return;
		}
		Controller.terminalDisplay.end();
		Controller.terminalInput.end();
	}

	public static void commands(String input) { // TODO add scan(ports), cd(directory), cs(int server)/*change server*/, traceroute(address), clone server to server(maybe just hard drive to hard drive)
		Scanner scanner = new Scanner(input);
		Vector<String> args = new Vector<String>();
		
		if (input.matches(Controller.getIp() + ".+")) {
			scanner.useDelimiter("\\s*");
			for(int i=0; i<input.length();i++){
				args.add(scanner.next())
			}
			
			switch (args.get(0)) { //change all scanner groups to args
			case "help":
				help();
				break;
			default:
				Controller.terminalDisplay
				.setText(Controller.terminalDisplay.getText() + "\n'" + args.get(0) + "' is not a recognized command");
				break;
			case "add":
				Network.getNetworks().add(new Network(2));
				Network.getNetworks().get(Network.getNetworks().size() - 1).populate();
				break;
			case "quit":
				Hakd.quitGame(args.get(1)); // TODO this will give a null pointer
				break;
			case "test":
				System.out.println(Dns.getDnsList().size());
				break;
			case "home":
				PlayerController.setCurrentNetwork(PlayerController.getHomeNetwork());
				PlayerController.setCurrentServer(0);
				break;
			case "connect":
				connect(scanner);
				break;
			case "url":
				url(scanner);
				break;
			case "upgrade":
				upgrade(scanner);
				break;
			}
		} else if (input.equals(Controller.getIp())) {
		} else {
			Controller.terminalDisplay
			.setText(Controller.terminalDisplay.getText() + "\n" + "please do not modify the address");
		}
		scanner.close();
	}

	public static void ctfCommands(String input) {
	}

	//--------commands--------
	private static void url(Scanner scan) {
		System.out.println(Dns.addUrl(PlayerController.getHomeNetwork(), scan.match().group(2)));
	}

	private static void connect(Scanner scan) { // connect to network program port // proxies change the current networks while connecting
		//TODO get rid of this messy code, test it in the game test file, and get programs running to test connections with the game running
	}

	private static void upgrade(Scanner scan){
		Network network = Network.getNetworks().get(Dns.findNetwork(PlayerController.getCurrentNetwork()));

		switch(scan.match().group(2)){
		case "0": // server
			for(int i=0; i<Integer.parseInt(scan.match().group(3));i++){
				network.setServers(network.getServerLimit()+1);
			}
			for(int i=0; i<Integer.parseInt(scan.match().group(4));i++){
				network.getServers().add(new Server(network.getNetworkId()));
				Network.getNetworks().get(Dns.findNetwork(network.getIp())).addServer(i);
			}
			break;
		case "1": // mobo
			break;
		case "2": // cpu
			break;
		case "3": // memory
			break;
		case "4": // gpu
			break;
		case "5": // storage
			break;
		}
	}

	private static void help() {
		Controller.terminalDisplay
		.setText(Controller.terminalDisplay.getText() + "\nCommands:" + "\nquit - stops the game" + "\nadd - adds a server" + "\ntest - outputs the current amount of networks in the dns" + "\nconnect [option] [ip] [program] [port] - connects to a network" + "\nurl [string] - changes the home networks url to the entered string" +
				"\nhome - sets the current network and server to the homene twork and server 0" + "\nupgrade [part(int)] [slots to add] [parts to add]");
	}

}
