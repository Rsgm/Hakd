package hakd.gui.input;

import hakd.game.gameplay.Command;
import hakd.gui.Terminal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class MenuInputHandler extends TerminalInput {

	public MenuInputHandler(Terminal terminal) {
		super(terminal);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			history.add(text);
			terminal.addText(text);

			System.out.println(text); // print input
			Command c = new Command(text, terminal.getPlayer());
			c.run();

			line = history.size();
			ClearInput(); // reset the terminal text
		} else if (keycode == Keys.DOWN && line < history.size() - 1) {
			line++;
			text = history.get(line);
			cursor = text.length();
		} else if (keycode == Keys.UP && line > 0) {
			line--;
			text = history.get(line);
			cursor = text.length();
		} else if (keycode == Keys.LEFT && cursor > 0 && text.charAt(cursor - 1) != ">".charAt(0)) {
			cursor--;
		} else if (keycode == Keys.RIGHT && cursor < text.length()) {
			cursor++;
		} else if (keycode == Keys.ESCAPE) {
			line = history.size();
			ClearInput();
		} else if (keycode == Keys.BACKSPACE && text.charAt(cursor - 1) != ">".charAt(0)) {
			text = text.substring(0, cursor - 1) + text.substring(cursor, text.length());
			cursor--;
		} else if (keycode == Keys.FORWARD_DEL && cursor < text.length()) {
			text = text.substring(0, cursor) + text.substring(cursor + 1, text.length());
		} else if (keycode == Keys.HOME) {
			cursor = text.indexOf(">") + 1;
		} else if (keycode == Keys.END) {
			cursor = text.length();
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		if ((character + "").matches("[\\w ]")) { // only matches \w or spaces
			text = text.substring(0, cursor) + character + text.substring(cursor, text.length());
			cursor++;
		} else if (character == Keys.ENTER) {
			history.add(text);
			terminal.addText(text);

			System.out.println(text); // print input
// Command c = new Command(text, terminal.getPlayer());
// c.run();

			line = history.size();
			ClearInput(); // reset the terminal text
		}
		return true; // no reason not to return true
	}

	@Override
	public boolean scrolled(int amount) { // works now
		if ((terminal.getCam().position.y >= Gdx.graphics.getHeight() / 2 + 10 || amount == -1)
				&& (terminal.getCam().position.y <= -Gdx.graphics.getHeight() / 2 + 18 + 16 * terminal.getText().size() || amount == 1)) {
			terminal.getCam().translate(0, -amount * 10);
			System.out.println(amount);
		}
		return true;
	}
}
