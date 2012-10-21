package com.github.rmirman.hakd;

import com.github.rmirman.hakd.ui.UiController;

public class StartGame{

	public static void main(String[] args){
		UiController.run(args);
	}

	public static void quitGame(String reason){
		if(reason == null){
			System.exit(0);
		}
		if(reason != null){
			System.exit(1);
		}
	}
	
	public static void runGame(){
		UiController.launch((String[])null);
	}
}

/*have some servers in this game with port 80 open direct you to a website like xkcd using a built in web browser, is this plagerism/illegal?*/