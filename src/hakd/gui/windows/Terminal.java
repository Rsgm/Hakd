package hakd.gui.windows;

import hakd.gui.input.GameInput;
import hakd.gui.input.TerminalInput;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.HakdScreen;
import hakd.gui.screens.MenuScreen;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Terminal implements Window { // this may need a quit method to free and save resources
	private boolean				menu;
	private Device				device;
	private TerminalInput		input;

	private List<String>		history		= new ArrayList<String>();				// holds previously used commands for easy access
	private final List<String>	text		= new ArrayList<String>();
	private String				currentText;
	private int					cursor;
	private int					line		= 0;									// holds the position of the history
	private Color				fontColor	= new Color(0.0f, 0.7f, 0.0f, 1.0f);
	private BitmapFont			font;

	private Sprite				border;
	private Sprite				display;
	private Sprite				close;
	private int					x;
	private int					y;

	private int					scroll;
	private boolean				inputUsed;
	private float				cursorTime	= 0;
	private float				inputTime	= 0;

	private GameScreen			screen;
	private MenuScreen			menuScreen;

	public Terminal(boolean isMenu, Device d, HakdScreen screen) {
		menu = isMenu;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("src/hakd/gui/resources/fonts/whitrabt.ttf"));
		font = generator.generateFont(16);
		generator.dispose();

		font.setColor(fontColor);

// if (menu) { // it may not be the best to let this class handle both the menu and the game terminal
// menuscreen=(menuscreen)screen;
// } else {
		device = d;
		this.screen = (GameScreen) screen;
// }

	}

	public void addText(String t) { // support both \r and \n, if this does not work
		if (t.matches("\\n") || t.matches("\\r")) {
			text.add(t);
			return;
		}

		Scanner s = new Scanner(t);
		s.useDelimiter("[\n\r]");
		while (s.hasNext()) {
			text.add(s.next());
		}
		s.close();
	}

	public void addText(String[] text) {
		for (String s : text) { // starts at 1, not 0
// String s = v.arg(i).tojstring(); // jsut an idea to sanitize inputs, although where is the fun in that?
// if (s.matches("[(,)")) {
// s.replaceAll("[(,)]", "");
// }
			addText(s);
		}
	}

	@Override
	public void open(TextureAtlas textures) {
		int width;
		int height;

// if (menu) {
// height = Gdx.graphics.getHeight();
// width = Gdx.graphics.getWidth();
// } else {
		width = (int) (Gdx.graphics.getWidth() * .8);
		height = (int) (Gdx.graphics.getHeight() * .8);
// }

		x = (Gdx.graphics.getWidth() - width) / 2;
		y = (Gdx.graphics.getHeight() - height) / 2;

// System.out.println(width + "	" + height);
// System.out.println(x + "	" + y);

		border = new Sprite(textures.findRegion("windowBorder"));
		border.setBounds(x, y, width, height);
		display = new Sprite(textures.findRegion("windowDisplay")); // this, being one pixel, saved you about 2KB of memory, you're welcome
		display.setBounds(x + 20, y + 20, width - 40, height - 40);

		close = new Sprite(textures.findRegion("close"));
		close.setPosition(Gdx.graphics.getWidth() - x - 20, Gdx.graphics.getHeight() - y - 20);

		cursorTime = 0;
		inputTime = 0;

		input = new TerminalInput(this);
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(Camera cam, SpriteBatch batch, float delta) {
		cursorTime += delta;

		batch.begin();
		border.draw(batch);
		display.draw(batch);
		close.draw(batch);

		for (int i = 0; i < text.size(); i++) {
			font.draw(batch, text.get(i), x + 24, y + 35 + scroll + 16 * (text.size() - i));
		}
		String s;

		if (cursorTime % 1.4 < .7) {
			s = currentText.substring(0, cursor - 1) + "|"/*"█"*/
					+ currentText.substring(cursor, currentText.length());
		} else {
			s = currentText;
		}

		font.draw(batch, s, x + 24, y + 35 + scroll); // TODO make this start at the corner of the window
		batch.end();
	}

	@Override
	public void close() {
// if (!menu) {
		GameScreen.OPEN_WINDOW = null;

		Gdx.input.setInputProcessor(new GameInput(screen.getGame(), screen.getCam(), screen.getPlayer(), screen));
// }
	}

	public void ClearInput() {
		currentText = "Boot>";/*terminal.getDevice().getNetwork().getIp() + ">";*/
		cursor = currentText.length();
	}

	public boolean isMenu() {
		return menu;
	}

	public TerminalInput getInput() {
		return input;
	}

	public List<String> getText() {
		return text;
	}

	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	public void setInput(TerminalInput input) {
		this.input = input;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public List<String> getHistory() {
		return history;
	}

	public void setHistory(List<String> history) {
		this.history = history;
	}

	public int getScroll() {
		return scroll;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	public String getCurrentText() {
		return currentText;
	}

	public void setCurrentText(String currentText) {
		this.currentText = currentText;
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public Sprite getClose() {
		return close;
	}

	public void setClose(Sprite close) {
		this.close = close;
	}

	public float getCursorTime() {
		return cursorTime;
	}

	public void setCursorTime(float cursorTime) {
		this.cursorTime = cursorTime;
	}
}
