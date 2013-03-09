package hakd.gameplay;

import hakd.gui.GuiController;
import hakd.networks.Network;
import hakd.networks.devices.Device;

public class PlayerController {
	// player stats
	private static int		money;			// in $ //add redundancy to money // triple redundancy with voting, maybe some
// rudimentary encryption, or no redundancy with strong encryption
	private static String	playerName;
	private static String	terminalText;

	// networks
	private static Network	homeNetwork;	// meant to be used as the players home base

	private static Network	currentNetwork;
	private static Device	currentServer;

	// --------methods--------

	public static void updateTerminalText() {
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

	public static String getPlayerName() {
		return playerName;
	}

	public static void setPlayerName(String playerName) {
		PlayerController.playerName = playerName;
		homeNetwork.setOwner(playerName);
	}

	public static String getTerminalText() {
		return terminalText;
	}

	public static void setTerminalText(String terminalText) {
		PlayerController.terminalText = terminalText;
	}

	public static Device getCurrentServer() {
		return currentServer;
	}

	public static void setCurrentServer(Device currentServer) {
		PlayerController.currentServer = currentServer;
	}
}
