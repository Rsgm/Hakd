package hakd.gui.input;

import hakd.game.Command;
import hakd.gui.windows.Terminal;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TerminalInput implements InputProcessor {
	private Terminal		terminal;

	private List<String>	history;

	public TerminalInput(Terminal terminal) {
		this.terminal = terminal;
		this.history = terminal.getHistory();

		terminal.ClearInput();
	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.ENTER) {
			history.add(terminal.getCurrentText());
			terminal.addText(terminal.getCurrentText());

			System.out.println(terminal.getCurrentText()); // print input
			new Command(terminal.getCurrentText(), terminal.getDevice());

			terminal.setLine(history.size());
			terminal.ClearInput(); // reset the terminal text
			terminal.setCursorTime(0);
		} else if (keycode == Keys.DOWN && terminal.getLine() < history.size() - 1) {
			terminal.setLine(terminal.getLine() + 1);
			terminal.setCurrentText(history.get(terminal.getLine()));
			terminal.setCursor(terminal.getCurrentText().length());
			terminal.setCursorTime(0);
		} else if (keycode == Keys.UP && terminal.getLine() > 0) {
			terminal.setLine(terminal.getLine() - 1);
			terminal.setCurrentText(history.get(terminal.getLine()));
			terminal.setCursor(terminal.getCurrentText().length());
			terminal.setCursorTime(0);
		} else if (keycode == Keys.LEFT && terminal.getCursor() > 0 && terminal.getCurrentText().charAt(terminal.getCursor() - 2) != ">".charAt(0)) {
			terminal.setCursor(terminal.getCursor() - 1);
			terminal.setCursorTime(0);
		} else if (keycode == Keys.RIGHT && terminal.getCursor() < terminal.getCurrentText().length()) {
			terminal.setCursor(terminal.getCursor() + 1);
			terminal.setCursorTime(0);
		} else if (keycode == Keys.ESCAPE) {
			terminal.close();
		} else if (keycode == Keys.BACKSPACE && terminal.getCurrentText().charAt(terminal.getCursor() - 1) != ">".charAt(0)) {
			terminal.setCurrentText(terminal.getCurrentText().substring(0, terminal.getCursor() - 1)
					+ terminal.getCurrentText().substring(terminal.getCursor(), terminal.getCurrentText().length()));
			terminal.setCursor(terminal.getCursor() - 1);
			terminal.setCursorTime(0);
		} else if (keycode == Keys.FORWARD_DEL && terminal.getCursor() < terminal.getCurrentText().length()) {
			terminal.setCurrentText(terminal.getCurrentText().substring(0, terminal.getCursor())
					+ terminal.getCurrentText().substring(terminal.getCursor() + 1, terminal.getCurrentText().length()));
		} else if (keycode == Keys.HOME) {
			terminal.setCursor(terminal.getCurrentText().indexOf(">") + 1);
			terminal.setCursorTime(0);
		} else if (keycode == Keys.END) {
			terminal.setCursor(terminal.getCurrentText().length());
			terminal.setCursorTime(0);
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
			terminal.setCurrentText(terminal.getCurrentText().substring(0, terminal.getCursor()) + character
					+ terminal.getCurrentText().substring(terminal.getCursor(), terminal.getCurrentText().length()));
			terminal.setCursor(terminal.getCursor() + 1);
		}

		return true; // no reason not to return true
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Sprite close = terminal.getClose();

		float xNear = close.getX();
		float xFar = xNear + close.getWidth();
		float yNear = Gdx.graphics.getHeight() - close.getY();
		float yFar = Gdx.graphics.getHeight() - close.getY() - close.getHeight();

		if (screenX >= xNear && screenX <= xFar && screenY <= yNear && screenY >= yFar) {
			terminal.close();
		}
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
	public boolean scrolled(int amount) { // TODO change these values
		int height = Gdx.graphics.getHeight();
		System.out.println(amount + "	" + height + "	" + terminal.getScroll());

		if ((terminal.getScroll() >= height / 2 + 10 || amount == -1)
				&& (terminal.getScroll() <= (-height) / 2 + 18 + 16 * terminal.getText().size() || amount == 1)) {
			terminal.setScroll(terminal.getScroll() + amount * 10);
			// maybe move the text originY up/down based on the scroll
		}
		return true;
	}
}
