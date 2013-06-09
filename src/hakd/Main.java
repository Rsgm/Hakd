package hakd;

import hakd.game.Hakd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public final class Main {
	private static Preferences	prefs;	;
	private static Preferences	save1;	// save this for later

	public static void main(String[] args) {
		new LwjglApplication(new Hakd(), "Hak'd", 800, 600, false);
		prefs = Gdx.app.getPreferences("hakd-prefs");

// if (!prefs.getBoolean("played-before")) {
		newPrefs();
// }

		int w = prefs.getInteger("width");
		int h = prefs.getInteger("height");
		boolean f = prefs.getBoolean("fullscreen");

		Gdx.graphics.setDisplayMode(w, h, f);
	}

	private static void newPrefs() {
		/* the directory for these can't be changes,
		 * on linux it is /home/[user name]/.prefs/,
		 * on windows it is users/[user name]/.prefs/,
		 * I have not tested mac yet	*/
		prefs.putBoolean("played-before", true);
		prefs.putInteger("width", 800);
		prefs.putInteger("height", 600);
		prefs.putBoolean("fullscreen", false);
		prefs.putBoolean("vsync", true);
		prefs.putBoolean("sound", true);

		prefs.flush(); // unfortunately this makes this game non-portable, kind of
	}

	public static void quitGame(String reason) {
		System.out.print("quitting");

		if (reason == null) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}
