package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HakdScreen implements Screen {
	int				width				= Gdx.graphics.getWidth();
	int				height				= Gdx.graphics.getHeight();

	Game			game;
	SpriteBatch		batch				= new SpriteBatch();
	TextureAtlas	textures			= new TextureAtlas("src/hakd/gui/resources/textures.txt");

	Color			fontColor			= new Color(1.0f, 1.0f, 1.0f, 1.0f);
	Color			consoleFontColor	= new Color(0.0f, 0.7f, 0.0f, 1.0f);						// or read from, and write to, a preference or
// .ini file

	public HakdScreen(Game game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);

	}
}
