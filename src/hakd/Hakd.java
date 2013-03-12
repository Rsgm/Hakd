package hakd;

import hakd.gui.GuiDisplay;
import hakd.websites.servlets.NetworkServlet;
import hakd.websites.servlets.StoreServlet;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.lwjgl.opengl.Display;

public final class Hakd {
	public static boolean	running	= false;

	public static void main(String[] args) {
// startServer();
		// startLua(); // TODO set this to the lua method in programs

		GuiDisplay guiDisplay = new GuiDisplay();
		guiDisplay.start();// start the user interface, that also runs the game
	}

	private static void startLua() { // I don't think this is needed, it is all handled in the programs class
		String script = "lua/hello.lua";

		Globals globals = JsePlatform.standardGlobals();

		LuaValue chunk = globals.loadFile(script);

		chunk.call(LuaValue.valueOf(script));
	}

	public static void quitGame(String reason) {
		System.out.print("quitting");
		running = false;
		Display.destroy();

		if (reason == null) {
			System.exit(0);
		}
		if (reason != null) {
			System.exit(1);
		}
	}

	private static void startServer() { // start tomcat and servlets
		// TODO move this at some point to a controller class in the servlet folder so each game mode has its own, if needed. and so its not running
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