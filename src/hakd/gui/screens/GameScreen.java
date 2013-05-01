package hakd.gui.screens;

import hakd.game.gameplay.GamePlay;
import hakd.game.gameplay.Player;
import hakd.gui.Terminal;
import hakd.internet.NetworkController;
import hakd.networks.Network;
import hakd.other.enumerations.NetworkType;

import com.badlogic.gdx.Game;

public class GameScreen extends HakdScreen {
	private Terminal	terminal;
	private Player		player;

	public GameScreen(Game game, String name) {
		super(game);

		GamePlay.generateGame();

		Network n = NetworkController.addPublicNetwork(NetworkType.PLAYER);
		player = new Player(name, n, terminal);

		terminal = new Terminal(false, this);
	}

	@Override
	public void show() {
		super.show();

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

}
