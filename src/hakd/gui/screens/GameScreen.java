package hakd.gui.screens;

import hakd.game.gameplay.GamePlay;
import hakd.gui.Terminal;

import com.badlogic.gdx.Game;

public class GameScreen extends HakdScreen {
	private Terminal	terminal;

	public GameScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		GamePlay.generateGame();

		terminal = new Terminal(false);
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
