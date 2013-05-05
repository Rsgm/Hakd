package hakd.gui.windows;

import hakd.gui.input.MenuInput;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.Scanner;

import org.luaj.vm2.Varargs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Terminal { // TODO extend window class
	private boolean					menu;
	Device							device;
	private MenuInput			input;
	private int						lineSpacing	= 16;						// this is declared and has a getter/setter but never used...

	ArrayList<String>				history		= new ArrayList<String>();	// holds previously used commands for easy access

	private final ArrayList<String>	text		= new ArrayList<String>();

	public Terminal(boolean isMenu, Device d) {
		menu = isMenu;

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

	public MenuInput getInput() {
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

	public void setInput(MenuInput input) {
		this.input = input;
	}

	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
}
