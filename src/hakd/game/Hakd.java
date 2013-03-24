package hakd.game;

import hakd.gui.screens.TitleScreen;

import com.badlogic.gdx.Game;

public class Hakd extends Game {

	@Override
	public void create() {
		setScreen(new TitleScreen(this));
	}
}
