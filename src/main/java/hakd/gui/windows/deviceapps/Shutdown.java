package hakd.gui.windows.deviceapps;

public final class Shutdown extends SceneWindow {
    private final GameScene window;

    public Shutdown(GameScene scene) {
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
