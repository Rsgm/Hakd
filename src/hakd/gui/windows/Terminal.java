package hakd.gui.windows;

import hakd.gui.input.TerminalInput;
import hakd.gui.screens.HakdScreen;

import java.util.ArrayList;
import java.util.Scanner;

import org.luaj.vm2.Varargs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class Terminal implements Window { // TODO extend window class
	private boolean					menu;
	private TerminalInput			input;
	private int						lineSpacing	= 16;						// this is declared and has a getter/setter but never used...

	int								line		= 0;						// holds the position of the history
	ArrayList<String>				history		= new ArrayList<String>();	// holds previously used commands for easy access

	private final ArrayList<String>	text		= new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public Terminal(boolean isMenu, HakdScreen screen) {
		menu = isMenu;

		if (menu) {

		} else {
			
	}

	public void addText(String t) { // TODO support both \r and \n, if this does not work
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

	public void render(BitmapFont font, SpriteBatch batch, float time) {
		for (int i = 0; i < text.size(); i++) {
			font.draw(batch, text.get(i), 5, 16 + 16 * (text.size() - i));
		}
		String s;

		if (time % 1.4 > .7) {
			s = input.getText().substring(0, input.getCursor()) + "|" + input.getText().substring(input.getCursor(), input.getText().length());
		} else {
			s = input.getText().substring(0, input.getCursor()) + " " + input.getText().substring(input.getCursor(), input.getText().length());
		}

		font.draw(batch, s, 5, 16);
	}

	public boolean isMenu() {
		return menu;
	}

	public TerminalInput getInput() {
		return input;
	}

	public int getLineSpacing() {
		return lineSpacing;
	}

	public ArrayList<String> getText() {
		return text;
	}

	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	public void setInput(TerminalInput input) {
		this.input = input;
	}

	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
}
