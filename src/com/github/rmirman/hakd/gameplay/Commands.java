package com.github.rmirman.hakd.gameplay;

import com.github.rmirman.hakd.StartGame;
import com.github.rmirman.hakd.network.Dns;
import com.github.rmirman.hakd.network.Network;
import com.github.rmirman.hakd.ui.UiController;

public class Commands {




	public static void command(String input){


		if(input.equals(UiController.ip + "help")){
			help();
		}else if(input.equals(UiController.ip + "add")){
			Network.network[0] = new Network(0, "test");
			Network.network[0].populate();
		}else if(input.equals("quit")||input.equals(UiController.ip + "quit")){
			StartGame.quitGame(null);
		}else if(input.equals(UiController.ip + "test")){
			System.out.println(Dns.dnsList.length);
		}else{
			UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() + "\n'" + input + "' is not a recognized command");
		}


	}


	public static void help(){
		UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() + "\nCommands:\nquit - stops the game\nadd - adds a server\ntab - reopens the region tab");
	}

}
