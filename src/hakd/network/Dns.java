package hakd.network;

import hakd.userinterface.Controller;

import java.util.Vector;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class Dns {

	private static Vector<String>	dnsList			= new Vector<String>(2, 1);
	private static Vector<String>	connection		= new Vector<String>(1, 1);
	private static Vector<Line>		connectionLine	= new Vector<Line>(1, 1);

	// --------methods--------
	public static String assignIp(int region) { // assigns an ip to an object that requests one, also checks it and adds it to the dns list
		String ip;
		do {
			switch (region) { // creates a realistic ip based on registered ipv4 irl //0 == random, 1 == usa, 2 == europe, 3 == asia
				case 0:
					ip =
							(int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
									+ (int) (Math.random() * 256);
					break;
				case 1:
					ip =
							(int) (Math.random() * 14 + 63) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
									+ (int) (Math.random() * 256);
					break;
				case 2:
					ip =
							(int) (Math.random() * 15 + 77) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
									+ (int) (Math.random() * 256);
					break;
				default:
					ip =
							(int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
									+ (int) (Math.random() * 256);
					break;
			}
		} while (dnsList.indexOf(ip) != -1);
		dnsList.add(ip);
		dnsList.add("");
		return ip;
	}

	public static boolean addUrl(String ip, String url) { // registers a url to an ip // ip can only be a player's ip if they buy it
		if (url.matches("")) {
			if (dnsList.indexOf(url) == -1 && dnsList.indexOf(ip) != -1) {
				dnsList.set(dnsList.indexOf(ip) + 1, url);
				return true; // "you have successfully registered the url " + url + " for the ip " + ip;
			}
		}
		return false; // "Sorry, either that URL is already registered, or a bug)."
	}

	public static int findNetwork(String address) {
		if (!address
				.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")) {
			address = dnsList.get(dnsList.indexOf(address) + 1);
		}
		if (dnsList.indexOf(address) != -1) {
			return dnsList.indexOf(address) / 2;
		}
		return -1;
	}

	public static void addConnection(Network network, String address2) {
		String address1 = network.getIp();
		double r, a, b, c, xTrig, yTrig, x1, y1, x2, y2; // triangle>ABC

		x1 = network.getxCoordinate();
		y1 = network.getyCoordinate();
		x2 = Network.getNetworks().get(findNetwork(address2)).getxCoordinate();
		y2 = Network.getNetworks().get(findNetwork(address2)).getyCoordinate();
		r = Network.getRadius();

		a = 1; // line BC, point C is only (x2,y2+1)
		b = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 + 1 - y1) * (y2 + 1 - y1)); // line CA
		c = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)); // line AB

		Line line = new Line();
		if (x2 - x1 != 0) {
			xTrig = r * Math.sin(Math.acos((a * a - b * b + c * c) / (2 * a * c)));
			yTrig = r * Math.cos(Math.acos((a * a - b * b + c * c) / (2 * a * c)));

			System.out.print(xTrig + "\n" + yTrig + "\n" + ((a * a - b * b + c * c) / (-2 * a * c)) + "\n");
			System.out.println(a + "  " + b + "  " + c);
			if (x2 - x1 > 0) {
				line.setStartX(x1 + xTrig);
				line.setStartY(y1 - yTrig);
				line.setEndX(x2 - xTrig);
				line.setEndY(y2 + yTrig);
			} else if (x2 - x1 < 0) {
				line.setStartX(x1 - xTrig);
				line.setStartY(y1 - yTrig);
				line.setEndX(x2 + xTrig);
				line.setEndY(y2 + yTrig);
			}
		} else {
			if (y2 - y1 > 0) {
				// line.setendx(x2);
				// line.setendy(y2+radius);
			} else if (y2 - y1 < 0) {
				// line.setendx(x2);
				// line.setendy(y2-radius);
			} else {
				return;
			}
			return;
		}
		if (Double.isNaN(yTrig) || Double.isNaN(xTrig)) {
			return;
		}

		line.setFill(Paint.valueOf("black"));
		line.setStrokeWidth(1.15);
		line.setOpacity(0.3);
		connectionLine.add(line);
		connection.add(address1);
		connection.add(address2);

		Controller.regionView.get(network.getRegion()).getChildren().add(line);
		return;
	}

	// --------getters/setters--------
	public static Vector<String> getDnsList() {
		return dnsList;
	}

	public static void setDnsList(Vector<String> dnsList) {
		Dns.dnsList = dnsList;
	}

	public static Vector<String> getConnection() {
		return connection;
	}
}
