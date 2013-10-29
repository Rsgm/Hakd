package hakd.game.pythonapi;

import com.badlogic.gdx.Gdx;
import hakd.gui.windows.deviceapps.Terminal;

public final class PyDisplay {

    public static void write(final Terminal t, final String s) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                t.addText(s);
            }
        });
    }

    public static void over_write(final Terminal t, final String s, final int lineFromBottom) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                t.replaceText(s, lineFromBottom);
            }
        });
    }

}
