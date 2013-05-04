package hakd.gui.input;

import hakd.gui.windows.Terminal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class TerminalInput implements InputProcessor {
	String		text;
	int			cursor;

	Terminal	terminal;

	public TerminalInput(Terminal terminal) {
		this.terminal = terminal;
		Gdx.input.setInputProcessor(this);

		ClearInput();
		cursor = text.length();
	}

	void ClearInput() {
		text = terminal.getPlayer().getCurrentNetwork().getIp() + ">";
		cursor = text.length();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
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
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
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
