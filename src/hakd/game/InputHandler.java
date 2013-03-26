package hakd.game;

import hakd.game.gameplay.Command;

import java.util.ArrayList;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
	private final boolean			menu;

	private String					text	= "";
	private int						cursor;

	private int						line	= 0;						// holds the position of the history
	private final ArrayList<String>	history	= new ArrayList<String>();	// holds previously used commands for easy access

	public InputHandler(boolean isMenu) {
		menu = isMenu;
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			history.add(text);
			whatever.addText();

			System.out.println(text); // print input
			commandQueue.add(new Command(text)); // add command to queue for next game loop

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
		}
		return true;
	}

	private void ClearInput() {
		if (menu) {
			text = "Boot>";
		} else {
// TODO text = player.getIp() + whatever
		}

	}

	@Override
	public boolean keyTyped(char character) {
		if (cursor == text.length()) {
			text += character;
			cursor = text.length();
		} else {
			text = text.substring(0, cursor) + character + text.substring(cursor, text.length());
		}
		return true; // no reason not to return true
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public Vector<Command> getCommandQueue() {
		return commandQueue;
	}

	public void setCommandQueue(Vector<Command> commandQueue) {
		this.commandQueue = commandQueue;
	}
}
