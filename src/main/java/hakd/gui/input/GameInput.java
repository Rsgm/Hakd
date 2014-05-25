package hakd.gui.input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import hakd.game.Hakd;
import hakd.gui.HakdSprite;
import hakd.gui.Room;
import hakd.gui.screens.GameScreen;
import hakd.networks.devices.Device;
import hakd.other.Util;

public class GameInput implements InputProcessor {
    private final Hakd game;
    private final OrthographicCamera cam;
    private final GameScreen screen;
    private int lastMouseX;
    private int lastMouseY;

    public GameInput(Game game, OrthographicCamera cam) {
        this.game = (Hakd) game;
        this.cam = cam;
        this.screen = (GameScreen) game.getScreen();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.TAB) {
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
        Ray ray = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());

        HakdSprite sprite = null;
        for (Room.DeviceTile tile : screen.getRoom().getDeviceTileMap().values()) {
            if (tile.getTile().getBoundingRectangle().contains(ray.origin.x, ray.origin.y)) {
                sprite = tile.getTile();
                break;
            }
        }

        if (sprite != null && sprite.getObject() != null) {
            screen.setDeviceScene(((Device) sprite.getObject()).getDeviceScene());
            screen.getDeviceScene().open();
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float height = screen.getRoom().getFloorLayer().getHeight();
        float width = screen.getRoom().getFloorLayer().getWidth();

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

        Ray ray = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
        Vector2 vector2 = Util.orthoToIso(ray.origin.x, ray.origin.y);
        System.out.println(ray.origin.toString() + "  -  " + vector2.toString() + "  -  " + Util.isoToOrtho(vector2.x, vector2.y).toString());

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        float zoom;
        if ((cam.zoom > 0.25 && amount < 0) || (cam.zoom < 5 && amount > 0)) {
            zoom = cam.zoom + amount / 20f;
            cam.zoom = Math.round(zoom * 100) / 100f; // round the zoom to the
            // hundredths place
        }
//        System.out.println(cam.zoom);
        return true;
    }
}
