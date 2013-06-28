package hakd.game.gameplay;

import hakd.gui.screens.GameScreen;
import hakd.gui.windows.Window;
import hakd.networks.Network;
import hakd.other.Util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Player {
	// player stats
	private int			money;		// in $ //add redundancy to money // triple redundancy with voting, maybe some rudimentary encryption, or no
// redundancy with strong encryption
	private String		name;
	private Network		network;		// meant to be used as the players network base

	private GameScreen	screen;

	private Sprite		sprite;
	private int			isoX;
	private int			isoY;

	private Window		openWindow;

	// --------methods--------
	public Player(String name, Network home, TextureAtlas textures, GameScreen screen) {
		this.name = name;
		this.network = home;

		this.screen = screen;

		sprite = new Sprite(textures.findRegion("player0"));
	}

	public void move(float deltaX, float deltaY) {
		sprite.setX(sprite.getX() + deltaX);
		sprite.setY(sprite.getY() + deltaY);

		int[] coords = Util.orthoToIso(sprite.getX() - (sprite.getWidth()), sprite.getY(), screen.getRoom().getFloor().getHeight());
		isoX = coords[0];
		isoY = coords[1];

// System.out.println(isoX + "	" + isoY);
// System.out.println(sprite.getX() + "	" + sprite.getY());
	}

	// --------getters/setters--------
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
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

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public int getIsoX() {
		return isoX;
	}

	public void setIsoX(int isoX) {
		this.isoX = isoX;
	}

	public int getIsoY() {
		return isoY;
	}

	public void setIsoY(int isoY) {
		this.isoY = isoY;
	}
}
