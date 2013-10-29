package hakd.gui.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import hakd.gui.screens.MapScreen;

public final class MapInput extends CameraInputController {
    private final PerspectiveCamera cam;
    private final MapScreen screen;

    public MapInput(MapScreen screen) {
        super(screen.getCam());
        this.screen = screen;
        this.cam = (PerspectiveCamera) screen.getCam();

        scrollFactor *= 15f;
        translateButton = 0;
        forwardButton = 0;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.TAB) {
            screen.getGame().setScreen(screen.getGameScreen());
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }
}
