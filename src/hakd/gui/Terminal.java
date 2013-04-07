package hakd.gui;

import hakd.game.gameplay.Player;
import hakd.game.gameplay.Programs;
import hakd.gui.input.GameInputHandler;
import hakd.gui.input.MenuInputHandler;
import hakd.gui.input.TerminalInput;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.HakdScreen;
import hakd.gui.screens.MenuScreen;
import hakd.internet.NetworkController;
import hakd.networks.Network;
import hakd.other.enumerations.NetworkType;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Terminal {
	private boolean					menu;
	private TerminalInput			input;
	private int						lineSpacing	= 16;					// this is declared and has a getter/setter but never used...
	private Player					player;
	private MenuScreen				mScreen;
	private GameScreen				gScreen;

	private final ArrayList<String>	text		= new ArrayList<>();

	@SuppressWarnings("deprecation")
	public Terminal(boolean isMenu, HakdScreen screen) {
		menu = isMenu;

		if (menu) { // new network can be used here because this is a pseudo-player used to handle the menu
			player = new Player("", new Network(NetworkType.MENU), this);
			input = new MenuInputHandler(this);
			this.mScreen = (MenuScreen) screen;
		} else {
			player = new Player("player"/*TODO change this, get from prefs.ini*/, NetworkController.addPublicNetwork(NetworkType.PLAYER), this);
			input = new GameInputHandler(this);
			this.gScreen = (GameScreen) screen;
		}

		Programs.setTerminal(this);
	}

	public void addText(String t) {
		if (t.matches("\\n")) {
			text.add(t);
			return;
		}

		Scanner s = new Scanner(t);
		s.useDelimiter("\n");
		while (s.hasNext()) {
			text.add(s.next());
		}
		s.close();
	}

	public void render(BitmapFont font, SpriteBatch batch, float time) {
		for (int i = 0; i < text.size(); i++) {
			font.draw(batch, text.get(i), 5, 16 + 16 * (text.size() - i));
		}
		String s;

		if (time % 1.4 > .7) {
			s = input.getText().substring(0, input.getCursor()) + "|" + input.getText().substring(input.getCursor(), input.getText().length());
		} else {
			s = input.getText().substring(0, input.getCursor()) + " " + input.getText().substring(input.getCursor(), input.getText().length());
		}

		font.draw(batch, s, 5, 16);
	}

	public OrthographicCamera getCam() {
		return mScreen.getCam(); // TODO add an if statement, past me is lazy
	}

	public boolean isMenu() {
		return menu;
	}

	public TerminalInput getInput() {
		return input;
	}

	public int getLineSpacing() {
		return lineSpacing;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public MenuScreen getScreen() {
		return mScreen;
	}

	public void setScreen(MenuScreen screen) {
		this.mScreen = screen;
	}

	public ArrayList<String> getText() {
		return text;
	}

	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	public void setInput(TerminalInput input) {
		this.input = input;
	}

	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	public MenuScreen getmScreen() {
		return mScreen;
	}

	public void setmScreen(MenuScreen mScreen) {
		this.mScreen = mScreen;
	}

	public GameScreen getgScreen() {
		return gScreen;
	}

	public void setgScreen(GameScreen gScreen) {
		this.gScreen = gScreen;
	}
}
