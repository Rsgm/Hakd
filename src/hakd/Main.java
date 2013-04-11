package hakd;

import hakd.game.Hakd;
import hakd.other.enumerations.Prefs;
import hakd.websites.servlets.NetworkServlet;
import hakd.websites.servlets.StoreServlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public final class Main {
	public static ArrayList<String>	prefs	= new ArrayList<String>();

	public static void main(String[] args) {
		loadPrefs();
		// startServer(); // TODO test this with port forwarding

		String w = prefs.get(Prefs.WIDTH.line);
		String h = prefs.get(Prefs.HEIGHT.line);
		new LwjglApplication(new Hakd(), "Hak'd", Integer.parseInt(w), Integer.parseInt(h), false);
	}

	private static void loadPrefs() {
		File f = new File("prefs.ini");
		prefs.clear();

		try {
			Scanner s = new Scanner(f);
			while (s.hasNext(".+=.+")) {
				s.skip(".+=");
				prefs.add(s.nextLine());
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (prefs.size() != Prefs.total) { // checks if all the settings are there, if not create a new file with defualts
			try {
				if (!f.exists()) {
					f.createNewFile();
				}

				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
				w.append("width=800");
				w.newLine();
				w.append("height=600");
				w.newLine();
				w.append("fullscreen=false");
				w.newLine();
				w.append("vsync=true");
				w.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			loadPrefs(); // check it again
		}
	}

	public static void quitGame(String reason) {
		System.out.print("quitting");

		if (reason == null) {
			System.exit(0);
		}
		if (reason != null) {
			System.exit(1);
		}
	}

	private static void startServer() { // start tomcat and servlets
		// TODO move this at some point to a controller class in the servlet folder so each game mode has its own, if needed. and so its not
// running
// during modes that don't need it
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
