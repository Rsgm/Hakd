package hakd.gui.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import hakd.gui.screens.MapScreen;

public final class MapInput implements InputProcessor {
    private final OrthographicCamera cam;
    private final MapScreen screen;
    private int lastMouseX;
    private int lastMouseY;

    public MapInput(MapScreen screen) {
        super();
        this.screen = screen;
        this.cam = (OrthographicCamera) screen.getCam();

    }


    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.TAB) {
            screen.getGame().setScreen(screen.getGameScreen());
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float width = screen.getWidth();
        float height = screen.getHeight();

        float deltaX = (lastMouseX - screenX) / (Gdx.graphics.getWidth() / width) * cam.zoom;
        float deltaY = (screenY - lastMouseY) / (Gdx.graphics.getHeight() / height) * cam.zoom;

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
        if ((cam.zoom > 0.5 && amount < 0) || (cam.zoom < 10 && amount > 0)) {
            zoom = cam.zoom + amount / 5f;
            cam.zoom = Math.round(zoom * 100) / 100f; // round the zoom to the hundredths place
        }
        return true;
    }
}
