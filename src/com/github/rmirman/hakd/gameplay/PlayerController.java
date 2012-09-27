package com.github.rmirman.hakd.gameplay;

import com.github.rmirman.hakd.ui.UiController;

public class PlayerController {

	private static int money; // in $

	// networks
	private static String homeNetwork;

	private static String currentNetwork;
	private static int currentServer;

	public static int getMoney() {
		return money;
	}

	public static void setMoney(int money) {
		PlayerController.money = money;
	}

	public static String getHomeNetwork() {
		return homeNetwork;
	}

	public static void setHomeNetwork(String homeNetwork) {
		PlayerController.homeNetwork = homeNetwork;
	}

	public static String getCurrentNetwork() {
		return currentNetwork;
	}

	public static void setCurrentNetwork(String currentNetwork) {
		PlayerController.currentNetwork = currentNetwork;
		UiController.setIp(currentNetwork + "¬" + currentServer + "»");
	}

	public static int getCurrentServer() {
		return currentServer;
	}

	public static void setCurrentServer(int currentServer) {
		PlayerController.currentServer = currentServer;
		UiController.setIp(currentNetwork + "¬" + currentServer + "»");
	}
}