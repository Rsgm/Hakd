package hakd.gameplay;

import hakd.networking.Network;

public class PlayerController {
	// player stats
	private static int		money;			// in $ //add redundancy to money // triple redundancy with voting, maybe some rudimentary encryption, or
// no redundancy with strong encryption
	private static String	playerName;

	// networks
	private static Network	homeNetwork;	// meant to be used as the players home base

	private static Network	currentNetwork;
	private static Server	currentServer;

	// --------methods--------

	public static void updateCurrentIp() {
		GuiController.setIp(currentNetwork.getIp() + "/" + currentServer.getServerId() + ">");
	}

	// --------getters/setters--------
	public static int getMoney() {
		return money;
	}

	public static void setMoney(int money) {
		PlayerController.money = money;
	}

	public static Network getHomeNetwork() {
		return homeNetwork;
	}

	public static void setHomeNetwork(Network homeNetwork) {
		PlayerController.homeNetwork = homeNetwork;
	}

	public static Network getCurrentNetwork() {
		return currentNetwork;
	}

	public static void setCurrentNetwork(Network currentNetwork) {
		PlayerController.currentNetwork = currentNetwork;
	}

	public static Server getCurrentServer() {
		return currentServer;
	}

	public static void setCurrentServer(Server currentServer) {
		PlayerController.currentServer = currentServer;
	}

	public static String getPlayerName() {
		return playerName;
	}

	public static void setPlayerName(String playerName) {
		PlayerController.playerName = playerName;
		homeNetwork.setOwner(playerName);
	}
}
