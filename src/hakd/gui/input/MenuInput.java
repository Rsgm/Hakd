package hakd.gui.input;

import hakd.game.Command;
import hakd.gui.windows.Terminal;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MenuInput implements InputProcessor {
	String						text;
	int							cursor;

	Terminal					terminal;

	ArrayList<String>			history;
	int							line	= 0;	// holds the position of the history

	private OrthographicCamera	cam;

	public MenuInput(Terminal terminal, ArrayList<String> history, OrthographicCamera cam) {
		this.terminal = terminal;
		this.history = history;

		ClearInput();
		cursor = text.length();

		this.cam = cam;
	}

	void ClearInput() {
		text = terminal.getDevice().getNetwork().getIp() + ">";
		cursor = text.length();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			history.add(text);
			terminal.addText(text);

			System.out.println(text); // print input
			new Command(text, terminal.getDevice());

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
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if ((character + "").matches("[\\w \\.\",]")) { // only matches \w, single-quotes, periods, commas, or spaces. it may need others
			text = text.substring(0, cursor) + character + text.substring(cursor, text.length());
			cursor++;
		}

		return true; // no reason not to return true
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) { // works now
		if ((cam.position.y >= Gdx.graphics.getHeight() / 2 + 10 || amount == -1)
				&& (cam.position.y <= -Gdx.graphics.getHeight() / 2 + 18 + 16 * terminal.getText().size() || amount == 1)) {
			cam.translate(0, -amount * 10);
			System.out.println(amount);
		}
		return true;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

}
