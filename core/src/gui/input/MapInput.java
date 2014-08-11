package gui.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.collision.Ray;
import game.Noise;
import gui.screens.MapScreen;

public class MapInput implements InputProcessor {
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
        if (keycode == Keys.CONTROL_LEFT) {
            Ray ray = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            final double value = Noise.getValue(screen.getCurrentBackground(), ray.origin.x, ray.origin.y);
            System.out.println(value);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.TAB) {
            screen.getGame().setScreen(screen.getGameScreen());
        } else if (keycode == Keys.NUM_1) {
            screen.setCurrentBackground(Noise.NoiseType.TERRAIN);
        } else if (keycode == Keys.NUM_2) {
            screen.setCurrentBackground(Noise.NoiseType.DENSITY);
        } else if (keycode == Keys.NUM_3) {
            screen.setCurrentBackground(Noise.NoiseType.POLITICS);
        } else if (keycode == Keys.NUM_4) {
            screen.setCurrentBackground(Noise.NoiseType.ETHICS);
        } else if (keycode == Keys.NUM_5) {
            screen.setCurrentBackground(Noise.NoiseType.COUNTRY);
        } else if (keycode == Keys.NUM_6) {
            screen.setCurrentBackground(Noise.NoiseType.INCOME);
        } else if (keycode == Keys.NUM_7) {
            screen.setCurrentBackground(Noise.NoiseType.CRIME);
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

        if (Math.abs(lastMouseX - screenX) >= 20 || Math.abs(lastMouseY - screenY) >= 20) {
            lastMouseX = screenX;
            lastMouseY = screenY;
        }

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
        if ((cam.zoom > 1 && amount < 0) || (cam.zoom < 1000/*10*/ && amount > 0)) {
            zoom = cam.zoom + amount / 1f;
            cam.zoom = Math.round(zoom * 100) / 100f; // round the zoom to the hundredths place
        }
        return true;
    }
}
