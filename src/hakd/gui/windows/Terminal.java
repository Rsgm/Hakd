package hakd.gui.windows;

import hakd.gui.input.TerminalInput;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.luaj.vm2.Varargs;

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
	private TerminalInput		input;												// ?

	private List<String>		history		= new ArrayList<String>();				// holds previously used commands for easy access
	private final List<String>	text		= new ArrayList<String>();

	private Color				fontColor	= new Color(0.0f, 0.7f, 0.0f, 1.0f);
	private float				time		= 0;
	private BitmapFont			font;

	private Sprite				border;
	private Sprite				display;
	private Sprite				close;
	private int					x;
	private int					y;

	private int					scroll;

	public Terminal(boolean isMenu, Device d) {
		menu = isMenu;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("src/hakd/gui/resources/fonts/whitrabt.ttf"));
		font = generator.generateFont(16);
		generator.dispose();

		font.setColor(fontColor);

		if (menu) {

		} else {
			device = d;
		}

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

	public void addText(Varargs v) {
		for (int i = 1; i < v.narg() + 1; i++) { // starts at 1, not 0
// String s = v.arg(i).tojstring();
// if (s.matches("[(,)")) {
// s.replaceAll("[(,)]", "");
// }

			addText(v.arg(i).tojstring());
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

		time = 0;

		input = new TerminalInput(this);
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(Camera cam, SpriteBatch batch, float delta) {
		time += delta;

		batch.begin();
		border.draw(batch);
		display.draw(batch);
		close.draw(batch);

		for (int i = 0; i < text.size(); i++) {
			font.draw(batch, text.get(i), x + 24, y + 35 + scroll + 16 * (text.size() - i));
		}
		String s;

		if (time % 1.4 > .7) {
			s = input.getText().substring(0, input.getCursor()) + "█" + input.getText().substring(input.getCursor(), input.getText().length());
		} else {
			s = input.getText().substring(0, input.getCursor()) + " " + input.getText().substring(input.getCursor(), input.getText().length());
		}

		font.draw(batch, s, x + 24, y + 35 + scroll); // TODO make this start at the corner of the window
		batch.end();
	}

	@Override
	public void close() {
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
}
