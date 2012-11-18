package hakd;

import hakd.userinterface.Controller;
import hakd.web.servlets.NetworkServlet;
import hakd.web.servlets.StoreServlet;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;


public final class Hakd{

	public static void main(String[] args) {
		startServer();

		// start the user interface, that also runs the game
		Controller.run(args);
	}

	private static void startServer() { // start tomcat and servlets
		// TODO move this at some point to a controller class in the servlet folder so each game mode has its own, if needed. and so its not running during modes that dont need it
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

	public static void quitGame(String reason){
		if(reason == null){
			System.exit(0);
		}
		if(reason != null){
			System.exit(1);
		}
	}

	public static void runGame(){
		Controller.launch((String[])null);
	}
}