package hakd.gui.windows.device;

public class Shutdown extends SceneWindow {
    private final DeviceScene window;

    public Shutdown(DeviceScene scene) {
        super(scene);
        window = scene;
    }

    @Override
    public void open() {
        close();
    }

    @Override
    public void close() {
        window.close();
    }

}
