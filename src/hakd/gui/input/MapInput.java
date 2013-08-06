package hakd.gui.input;

import hakd.gui.screens.MapScreen;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class MapInput extends CameraInputController {
    private final PerspectiveCamera cam;
    private final MapScreen screen;

    private final Vector3 x = new Vector3(1, 0, 0);
    private final Vector3 y = new Vector3(0, 1, 0);
    private final Vector3 z = new Vector3(0, 0, 1);

    public MapInput(MapScreen screen) {
	super(screen.getCam());
	this.screen = screen;
	this.cam = (PerspectiveCamera) screen.getCam();

	scrollFactor *= 5f;
	translateButton = 0;
	forwardButton = 0;
    }

    @Override
    public boolean keyUp(int keycode) {
	return true;
    }

    @Override
    public boolean keyDown(int keycode) {
	return true;
    }
}
