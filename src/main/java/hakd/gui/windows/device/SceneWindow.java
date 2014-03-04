package hakd.gui.windows.device;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import hakd.gui.Assets;
import hakd.networks.devices.Device;

public abstract class SceneWindow {
    protected final DeviceScene scene;
    protected final Window window;
    protected final Device device;
    protected final Skin skin = Assets.skin;

    protected SceneWindow(DeviceScene scene) {
        this.scene = scene;

        window = new Window("", skin);
        window.setSize(scene.getCanvas().getWidth() * .9f, scene.getCanvas().getHeight() * .9f);

        device = scene.getDevice();


        window.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // touch up will not work without this returning true
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (y >= window.getHeight() - 20) {
                    if (window.getX() < 0) {
                        window.setX(0);
                    }
                    if (window.getY() < 0) {
                        window.setY(0);
                    }
                    if (window.getX() + window.getWidth() > Gdx.graphics.getWidth()) {
                        window.setX(Gdx.graphics.getWidth() - window.getWidth());
                    }
                    if (window.getY() + window.getHeight() > Gdx.graphics.getHeight()) {
                        window.setY(Gdx.graphics.getHeight() - window.getHeight());
                    }
                }
            }
        });
    }

    public void open() {
        scene.getCanvas().addActor(window);
    }

    public void close() {
        scene.getCanvas().removeActor(window);
    }

    public DeviceScene getScene() {
        return scene;
    }

    public Window getWindow() {
        return window;
    }

    public Device getDevice() {
        return device;
    }
}
