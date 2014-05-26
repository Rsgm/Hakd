package hakd.game.pythonapi.menu;

import com.badlogic.gdx.Gdx;
import hakd.game.Hakd;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.MenuScreen;

public class PyMenu {

    public static void setScreen(final MenuScreen currentScreen, final String name) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                currentScreen.getGame().setScreen(new GameScreen(((Hakd) currentScreen.getGame()), name));
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
