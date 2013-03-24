package hakd.gui.screens;

import hakd.game.InputHandler;
import hakd.game.gameplay.Command;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;

public class MenuScreen extends HakdScreen { // terminal directory: BOOT>_
	InputHandler	input;
	BitmapFont		font;
// BitmapFont consoleFont;
	Rectangle		background;

// TODO make two methods to add text to the screen, one that types the test letter by letter, and one that adds it to the display

	public MenuScreen(Game game) { // terminal screen
		super(game);
	}

	@Override
	public void show() {
		input = new InputHandler();
		Gdx.input.setInputProcessor(input);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("src/hakd/gui/resources/fonts/whitrabt.ttf"));
		font = generator.generateFont(16);
		generator.dispose();
	}

	@Override
	public void render(float delta) { // TODO use a view port type thing that scrolls through an off-screen terminal
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// runs the commands that have piled up since last render, this should only be one player command unless a player(bot) can type more than 60
// commands per second
		for (Command c : input.getCommandQueue()) { // TODO try to make this loop run on a different thread
			c.run();

		}

		batch.begin();
		font.setColor(0.0f, 0.7f, 0.0f, 1.0f);
		font.draw(batch, "this is a test:,;'\"(!?)+-*/=", 25, 160);
		batch.end();
	}

	@Override
	public void hide() {
	}
}
