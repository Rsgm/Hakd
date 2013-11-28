package hakd.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import hakd.gui.Assets;
import hakd.gui.screens.TitleScreen;

public final class Hakd extends Game {
    private int width;
    private int height;
    private boolean fullscreen;
    private boolean vsync;

    private Preferences prefs;

    // private static Preferences save1; // save this for later

    @Override
    public void create() {
        Assets.load();

        prefs = Gdx.app.getPreferences("hakd-prefs");

        if (!prefs.getBoolean("played-before")) {
            newPrefs();
        }

        width = prefs.getInteger("width");
        height = prefs.getInteger("height");
        fullscreen = prefs.getBoolean("fullscreen");
        vsync = prefs.getBoolean("vsync");

        Gdx.graphics.setDisplayMode(width, height, fullscreen);
        Gdx.graphics.setVSync(vsync); // because no one needs to render 4000 frames per second, but then again it keeps the room warm

        Gdx.app.setLogLevel(prefs.getInteger("log-level")); // TODO save a copy of the console to a log file

        setScreen(new TitleScreen(this));
    }

    private void newPrefs() {
    /*
     * the directory for these can't be changes, on linux it is /home/[user
	 * name]/.prefs/, on windows it is users/[user name]/.prefs/, I have not
	 * tested mac yet
	 */

        prefs.putBoolean("played-before", false);
        prefs.putInteger("width", 1200);//800);
        prefs.putInteger("height", 800);
        prefs.putBoolean("fullscreen", false);
        prefs.putBoolean("vsync", true);
        prefs.putBoolean("sound", true);
        prefs.putInteger("log-level", Application.LOG_DEBUG);

        prefs.flush();
        // unfortunately this makes this game non-portable, kind of

        // TODO ask user if they want the game to save anything, including settings and game save. Maybe ask in the settings and default to no saving
    }
}
