package hakd.game.gameplay;

import hakd.gui_old.GuiController;
import hakd.networks.Network;
import hakd.networks.devices.Device;

public class Player {
	// player stats
	private int		money;			// in $ //add redundancy to money // triple redundancy with voting, maybe some rudimentary encryption, or no
// redundancy with strong encryption
	private String	name;
	private Network	home;			// meant to be used as the players home base
	private Network	currentNetwork;
	private Device	currentServer;

	// --------methods--------
	public Player(String name, Network home) {
		this.name = name;
		this.home = home;
		this.currentNetwork = home;
		this.currentServer = home.getMasterServer();
	}

	public void updateTerminalText() {
		GuiController.setIp(currentNetwork.getIp() + "/" + currentServer.getServerId() + ">"); // how should you distinguish between servers?
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getPlayerName() {
		return name;
	}

	public void setPlayerName(String playerName) {
		this.name = playerName; // TODO Maybe add homenetwork.setowner back?
	}

	public Network getHome() {
		return home;
	}

	public void setHome(Network homeNetwork) {
		this.home = homeNetwork;
	}

	public Network getCurrentNetwork() {
		return currentNetwork;
	}

	public void setCurrentNetwork(Network currentNetwork) {
		this.currentNetwork = currentNetwork;
	}

	public Device getCurrentServer() {
		return currentServer;
	}

	public void setCurrentServer(Device currentServer) {
		this.currentServer = currentServer;
	}

	// --------getters/setters--------

}
