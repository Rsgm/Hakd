package hakd.gui.screens;

import hakd.gui.windows.Terminal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MenuScreen extends HakdScreen { // terminal directory: BOOT>_
	private Terminal			terminal;
	private BitmapFont			consoleFont;

	private final GL10			gl		= Gdx.graphics.getGL10();
	private OrthographicCamera	cam;
	private float				time;

	private boolean				typed	= false;

// TODO make two methods to add text to the screen, one that types the test letter by letter, and one that adds it to the display

	public MenuScreen(Game game) { // terminal screen
		super(game);
	}

	@Override
	public void show() {
		terminal = new Terminal(true, null);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("src/hakd/gui/resources/fonts/whitrabt.ttf"));
		consoleFont = generator.generateFont(16);
		generator.dispose();

		consoleFont.setColor(consoleFontColor);

		batch = new SpriteBatch();
		cam = new OrthographicCamera(width, height); // this may need to check isMenu
		cam.setToOrtho(false, width, height);
		batch.getProjectionMatrix().setToOrtho2D(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.update();
		cam.apply(gl);

	}

	@Override
	public void render(float delta) { // TODO use a view port type thing that scrolls through an off-screen terminal
		super.render(delta);
		time += delta;

		batch.begin();
		terminal.render(consoleFont, batch, time);

		batch.end();

		batch.setProjectionMatrix(cam.combined);
		cam.update();
		cam.apply(gl);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public BitmapFont getConsoleFont() {
		return consoleFont;
	}

	public void setConsoleFont(BitmapFont font) {
		this.consoleFont = font;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public void setCam(OrthographicCamera cam) {
		this.cam = cam;
	}

	public GL10 getGl() {
		return gl;
	}

	public boolean isTyped() {
		return typed;
	}

	public void setTyped(boolean typed) {
		this.typed = typed;
	}
}
