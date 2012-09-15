package com.github.rmirman.hakd.gameplay;

import com.github.rmirman.hakd.ui.UiController;

public class Commands {




	public static void command(String input){


		if(input.equals(UiController.ip + "help")){
			help();
		}else if(input.equals(UiController.ip + "")){
			
		}else{


		}








	}


	public static void help(){
		UiController.terminalDisplay.setText(UiController.terminalDisplay.getText() + "\nCommands:\nquit - stops the game\nadd - adds a server\ntab - reopens the region tab");

	}

}
