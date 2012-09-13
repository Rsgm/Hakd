package com.github.rmirman.hakd;

import com.github.rmirman.hakd.network.*;
import com.github.rmirman.hakd.store.*;
import com.github.rmirman.hakd.ui.*;

public class StartGame {

	public static void main(String [ ] args){
		UiController.run(args);
		Dns dnsServer = new Dns();
		
		Network.network[0] = new Network(0, "new player");
		Network.network[0].populate();
		Store store = new Store();
		//UiController.addServer();

	}

	public void quitGame(String reason){
		if(reason == null){
			System.exit(0);
		}
		if(reason != null){
			System.exit(1);
		}
	}
} 

/*This is not a game for eight year old kids who call themselves hackers on COD.
 * This is a game for those who wish to see what it is like hacking minus the programming (but with the logic?).
 * If you want to take a step further, then I encourage you to write your own hacks to use in this game,
 * but I will leave that up to you to figure out.*/


/*have some servers in this game with port 80 open direct you to a website like xkcd using a built in web browser, is this plagerism/illegal?*/