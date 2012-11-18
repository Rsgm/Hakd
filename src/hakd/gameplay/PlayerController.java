package hakd.gameplay;

import hakd.network.Dns;
import hakd.network.Network;
import hakd.userinterface.Controller;

public class PlayerController {

	private static int money; // in $
	private static String playerName;

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
		Controller.setIp(currentNetwork + "/" + currentServer + ">");
	}

	public static int getCurrentServer() {
		return currentServer;
	}

	public static void setCurrentServer(int currentServer) {
		PlayerController.currentServer = currentServer;
		Controller.setIp(currentNetwork + "/" + currentServer + ">");
	}

	public static String getPlayerName() {
		return playerName;
	}

	public static void setPlayerName(String playerName) {
		PlayerController.playerName = playerName;
		Network.getNetworks().get(Dns.findNetwork(homeNetwork)).setOwner(playerName);
	}
}