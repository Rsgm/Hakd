package game.pythonapi.menu;

import com.badlogic.gdx.Gdx;
import game.Hakd;
import gui.screens.GameScreen;
import gui.screens.MenuScreen;

public class PyMenu {
    private final MenuScreen menuScreen;

    public PyMenu(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
    }

    public void setScreen(final String name) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                menuScreen.getGame().setScreen(new GameScreen(((Hakd) menuScreen.getGame()), name));
            }
        });
    }

    public void write(final String s) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                menuScreen.getTerminal().addText(s);
            }
        });
    }

    public void over_write(final String s, final int lineFromBottom) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                menuScreen.getTerminal().replaceText(s, lineFromBottom);
            }
        });
    }
}
