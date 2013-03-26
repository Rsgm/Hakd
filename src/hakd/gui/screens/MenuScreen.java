package hakd.gui.screens;

import hakd.game.gameplay.Command;
import hakd.gui.Terminal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;

public class MenuScreen extends HakdScreen { // terminal directory: BOOT>_
	Terminal	terminal;
	BitmapFont	font;
	BitmapFont	consoleFont;
	Rectangle	background;		// ?

// TODO make two methods to add text to the screen, one that types the test letter by letter, and one that adds it to the display

	public MenuScreen(Game game) { // terminal screen
		super(game);
	}

	@Override
	public void show() {
		terminal = new Terminal(true);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("src/hakd/gui/resources/fonts/whitrabt.ttf"));
		consoleFont = generator.generateFont(16);
		generator.dispose();
	}

	@Override
	public void render(float delta) { // TODO use a view port type thing that scrolls through an off-screen terminal
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// runs the commands that have piled up since last render, this should only be one player command
		for (Command c : terminal.getCommandQueue()) { // unless a player(bot) can type more than 60 commands per second
			c.run(); // TODO try to make this loop run on a different thread
			terminal.getCommandQueue().remove(c);
		}

		batch.begin();
		consoleFont.setColor(0.0f, 0.7f, 0.0f, 1.0f);
		consoleFont.draw(batch, "this is a test: _-|~`{}[]()", 25, 160);
		batch.end();
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}
}
