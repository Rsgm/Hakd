package hakd.gui.screens;

import com.badlogic.gdx.Game;

public class TitleScreen extends HakdScreen {

	public TitleScreen(Game game) { // simple title
		super(game);
	}

	@Override
	public void show() {
		game.setScreen(new MenuScreen(game));
	}

	@Override
	public void render(float delta) {
	}

	@Override
	public void hide() {
	}
}
