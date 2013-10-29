package hakd.game.pythonapi.menu;

import com.badlogic.gdx.Gdx;
import hakd.gui.screens.HakdScreen;
import hakd.gui.screens.MenuScreen;

public final class PyMenu {

    public static void setScreen(final MenuScreen currentScreen, final HakdScreen newScreen) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                currentScreen.getGame().setScreen(newScreen);
            }
        });
    }

    public static void write(final MenuScreen screen, final String s) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                screen.addText(s);
            }
        });
    }

    public static void over_write(final MenuScreen screen, final String s, final int lineFromBottom) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                screen.replaceText(s, lineFromBottom);
            }
        });
    }
}
