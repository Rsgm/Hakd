package hakd.game.gameplay;

import hakd.gui.windows.Window;
import hakd.networks.Network;
import hakd.networks.devices.Device;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Player {
	// player stats
	private int		money;			// in $ //add redundancy to money // triple redundancy with voting, maybe some rudimentary encryption, or no
// redundancy with strong encryption
	private String	name;
	private Network	home;			// meant to be used as the players home base
	private Network	currentNetwork;
	private Device	currentServer;

	private Sprite	sprite;

	private Window	openWindow;

	// --------methods--------
	public Player(String name, Network home, TextureAtlas textures) {
		this.name = name;
		this.home = home;
		this.currentNetwork = home;

		if (home != null) {
			this.currentServer = home.getMasterServer();
		}

		sprite = new Sprite(textures.findRegion("player0"));
	}

	public void move(float deltaX, float deltaY) {
		sprite.setX(sprite.getX() + deltaX);
		sprite.setY(sprite.getY() + deltaY);
	}

	// --------getters/setters--------
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public Network getHome() {
		return home;
	}

	public void setHome(Network home) {
		this.home = home;
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

	public String getName() {
		return name;
	}

	public Window getOpenWindow() {
		return openWindow;
	}

	public void setOpenWindow(Window openWindow) {
		this.openWindow = openWindow;
	}

	public Sprite getSprite() {
		return sprite;
	}
}
