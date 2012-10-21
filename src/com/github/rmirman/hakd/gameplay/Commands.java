package com.github.rmirman.hakd.gameplay;

import java.util.Scanner;

import com.github.rmirman.hakd.StartGame;
import com.github.rmirman.hakd.network.Dns;
import com.github.rmirman.hakd.network.Network;
import com.github.rmirman.hakd.ui.UiController;

public class Commands {

	public static void command(String input){ // TODO add scan(ports), cd(change server), traceroute, clone server to server(maybe just hard drive to hard drive)
		Scanner scanner = new Scanner(input);
		if(input.matches(".+[»].+")){
			scanner.findInLine("»(\\S+)\\s*(\\S*)\\s*(\\S*)\\s*(\\S*)\\s*(\\S*)\\s*(\\S*)");
			switch(scanner.match().group(1)){
			case "help":
				help();
				break;
			case "add":
				Network.getNetwork().add(new Network(2));
				Network.getNetwork().get(Network.getNetwork().size()-1).populate();
				break;
			case "quit":
				StartGame.quitGame(scanner.match().group(2));
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
			default:
				UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() + "\n'" +
						scanner.match().group(1) + " " +
						scanner.match().group(2) + " " +
						scanner.match().group(3) + " " +
						scanner.match().group(4) + " " +
						scanner.match().group(5) + " " +
						scanner.match().group(6) +
						"' is not a recognized command");
				break;
			}
		}else if(input.matches(".+»+$")){
			UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() + "\n'" + "please do not modify the address");
		}else{
			UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() + "\n'" + "please do not modify the address");
		}
		scanner.close();
	}
	private static void url(Scanner scan) {
		System.out.println(Dns.addUrl(PlayerController.getHomeNetwork(), scan.match().group(2)));
	}

	private static void connect(Scanner scan) { // connect to network program port // proxies change the current networks while connecting
		//TODO get rid of this messy code, test it in the game test file, and get programs running to test connections with the game running
	}


	private static void help(){
		UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() +
				"\nCommands:" +
				"\nquit - stops the game" +
				"\nadd - adds a server" +
				"\ntest - outputs the current amount of networks in the dns" +
				"\nconnect [option] [ip] [program] [port] - connects to a network" +
				"\nurl [string] - changes the home networks url to the entered string" +
				"\nhome - sets the current network and server to the homene twork and server 0");
	}

}
