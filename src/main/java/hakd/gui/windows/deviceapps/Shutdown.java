package hakd.gui.windows.deviceapps;

public final class Shutdown implements ServerWindow {
    private final ServerWindowStage window;

    public Shutdown(ServerWindowStage w) {
        window = w;
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