package hakd.game.pythonapi;

import com.badlogic.gdx.Gdx;
import hakd.gui.windows.deviceapps.Terminal;

public final class PyDisplay {

    public void write(final Terminal t, final String s) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                t.addText(s);
            }
        });
    }

    public void over_write(final Terminal t, final String s, final int lineFromBottom) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                t.replaceText(s, lineFromBottom);
            }
        });
    }

    public void clear(final Terminal t) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                t.getDisplay().setText("root @ " + t.getDevice().getIp() + " : " + t.getDirectory().getPath() + "\n>");
            }
        });
    }

    public String input(Terminal terminal, String display) {
        return terminal.input(display);
    }

}
