package hakd;

import hakd.game.Hakd;
import hakd.websites.servlets.NetworkServlet;
import hakd.websites.servlets.StoreServlet;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public final class Main {
	private static Preferences	prefs;	;
	private static Preferences	save1;	// save this for later

	public static void main(String[] args) {

		// startServer(); // TODO test this with port forwarding

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
		prefs.putInteger("width", 1000);
		prefs.putInteger("height", 800);
		prefs.putBoolean("fullscreen", false);
		prefs.putBoolean("vsync", false);
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

	private static void startServer() { // start tomcat and servlets
		// TODO move this at some point to a controller class in the servlet folder so each game mode has its own, if needed. and so its not
// running during modes that don't need it // Game modes? I think I decided not to do that
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(80);
		Context context = tomcat.addContext("/", new File(".").getAbsolutePath());

		StoreServlet store = new StoreServlet();
		Tomcat.addServlet(context, "store", store);
		context.addServletMapping("/store/*", "store");

		NetworkServlet network = new NetworkServlet();
		Tomcat.addServlet(context, "network", network);
		context.addServletMapping("/network/*", "network");

		try {
			tomcat.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}
	}
}
