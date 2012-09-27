package com.github.rmirman.hakd.gameplay;

import com.github.rmirman.hakd.StartGame;
import com.github.rmirman.hakd.network.Dns;
import com.github.rmirman.hakd.network.Network;
import com.github.rmirman.hakd.ui.UiController;

public class Commands { // TODO create a parser in here for commands and their parameters/arguments




	public static void command(String input){


		if(input.equals(UiController.getIp() + "help")){
			help();
		}else if(input.equals(UiController.getIp() + "add")){
			Network.getNetwork().add(new Network("company"));
			Network.getNetwork().get(Network.getNetwork().size()-1).populate();
		}else if(input.equals("quit")||input.equals(UiController.getIp() + "quit")){
			StartGame.quitGame(null);
		}else if(input.equals(UiController.getIp() + "test")){
			System.out.println(Dns.getDnsList().size());
		}else{
			UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() + "\n'" + input + "' is not a recognized command");
		}


	}
	// TODO add a connect method and test connecting

	public static void help(){
		UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() +
				"\nCommands:\nquit - stops the game\nadd - adds a server\ntest - outputs the current amount of networks in the dns");
	}

}
