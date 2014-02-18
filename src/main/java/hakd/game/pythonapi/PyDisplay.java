package hakd.game.pythonapi;

import com.badlogic.gdx.Gdx;
import hakd.gui.windows.deviceapps.Terminal;

public final class PyDisplay {
    private final Terminal terminal;

    public PyDisplay(Terminal terminal) {
        this.terminal = terminal;
    }

    public void write(final String s) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                terminal.addTextln(s);
            }
        });
    }

    public void over_write(final String s, final int lineFromBottom) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                terminal.replaceText(s, lineFromBottom);
            }
        });
    }

    public void clear() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                terminal.getDisplay().setText("root @ " + terminal.getDevice().getIp() + " : " + terminal.getDirectory().getPath() + "\n>");
            }
        });
    }
}
