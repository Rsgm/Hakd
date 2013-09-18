package hakd.gui.input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import hakd.game.Hakd;
import hakd.game.gameplay.Player;
import hakd.gui.screens.GameScreen;

public final class GameInput implements InputProcessor {
	private final Hakd game;

	private final OrthographicCamera cam;

	private final Player player;
	private final GameScreen screen;

	private int lastMouseX;
	private int lastMouseY;

	public GameInput(Game game, OrthographicCamera cam, Player player) {
		this.game = (Hakd) game;
		this.cam = cam;
		this.player = player;
		this.screen = (GameScreen) game.getScreen();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.TAB) {
			game.setScreen(screen.getMap());
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
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
		float height = screen.getRoom().getFloor().getHeight();
		float width = screen.getRoom().getFloor().getWidth();

		float deltaX = (lastMouseX - screenX) / (Gdx.graphics.getWidth() / width) * cam.zoom;
		float deltaY = (screenY - lastMouseY) / (Gdx.graphics.getHeight() / height) * cam.zoom;

		System.out.println(deltaX + "	" + deltaY + "	" + cam.position.x + "	" + cam.position.y);

		cam.translate(deltaX, deltaY);

		lastMouseX = screenX;
		lastMouseY = screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		lastMouseX = screenX;
		lastMouseY = screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		float zoom;
		if((cam.zoom > 0.25 && amount < 0) || (cam.zoom < 5 && amount > 0)) {
			zoom = cam.zoom + amount / 20f;
			cam.zoom = Math.round(zoom * 100) / 100f; // round the zoom to the
			// hundredths place
		}
		System.out.println(cam.zoom);
		return true;
	}
}
