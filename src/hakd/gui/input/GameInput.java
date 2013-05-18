package hakd.gui.input;

import hakd.game.Hakd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameInput implements InputProcessor {
	private final Hakd			game;

	private OrthographicCamera	cam;

	private int					lastMouseX;
	private int					lastMouseY;

	public GameInput(Game game, OrthographicCamera cam) {
		this.game = (Hakd) game;
		this.cam = cam;
	}

	@Override
	public boolean keyDown(int keycode) {
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
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float deltaX = (lastMouseX - screenX) / (game.getWidth() / 10f) * cam.zoom;
		float deltaY = (screenY - lastMouseY) / (game.getHeight() / 10f) * cam.zoom;
		// I am also surprised that the *cam.zoom worked without flaw when I tried it

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
		if (cam.zoom >= 0.12 || amount < 0) {
			cam.zoom += -amount / 10f;
		}// else if(cam.zoom<){

		// }
		System.out.println(cam.zoom);
		return true;
	}
}
