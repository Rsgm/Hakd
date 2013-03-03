package hakd.networking;

import hakd.gui.GameGui;
import hakd.network.Network;

import java.util.ArrayList;
import java.util.Vector;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class Dns {

	private static ArrayList<String>	dnsList				= new ArrayList<String>();
	private static ArrayList<String>	serviceProviders	= new ArrayList<String>();

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
		} while (dnsList.contains(ip));
		dnsList.add(ip);
		dnsList.add("");
		return ip;
	}

	public static boolean addUrl(String ip, String url) { // registers a url to an ip just so not everything is an ip // ip can only be a player's ip
// if they buy it
		if (url.matches("")) {
			if (!dnsList.contains(url) && dnsList.contains(ip)) {
				dnsList.set(dnsList.indexOf(ip) + 1, url);
				return true; // "you have successfully registered the url " + url + " for the ip " + ip;
			}
		}
		return false; // "Sorry, either that URL is already registered, or a bug)."
	}

	public static int findNetwork(String address) {
		if (!address.matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b")) { // ip regex
			address = dnsList.get(dnsList.indexOf(address) + 1);
		} else if (address.matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b") && dnsList.contains(address)) {
			return dnsList.indexOf(address) / 2;
		}
		return -1;
	}

	public static void addConnection(Network network, String address) {
		Vector<Line> lines = GameGui.getLines();
		int radius = GameGui.getRadius();
		double r, a, b, c, xTrig, yTrig, x1, y1, x2, y2; // triangle>ABC

		x1 = network.getxCoordinate();
		y1 = network.getyCoordinate();
		x2 = Network.getNetworks().get(findNetwork(address)).getxCoordinate();
		y2 = Network.getNetworks().get(findNetwork(address)).getyCoordinate();
		r = radius;

		a = 1; // line BC, point C is only (x2,y2+1)
		b = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 + 1 - y1) * (y2 + 1 - y1)); // line CA
		c = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)); // line AB

		Line line = new Line();
		if (x2 - x1 != 0) {
			xTrig = r * Math.sin(Math.acos((a * a - b * b + c * c) / (2 * a * c)));
			yTrig = r * Math.cos(Math.acos((a * a - b * b + c * c) / (2 * a * c)));
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
				line.setEndX(x2);
				line.setEndY(y2 + r);
			} else if (y2 - y1 < 0) {
				line.setEndX(x2);
				line.setEndY(y2 - r);
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
		lines.add(line);
		Connection.getConnection().add(new Connection(network, Network.getNetworks().get(findNetwork(address))));

		// GameGui.updateRegion();
		return;
	}

	// --------getters/setters--------
	public static ArrayList<String> getDnsList() {
		return dnsList;
	}

	public static void setDnsList(ArrayList<String> dnsList) {
		Dns.dnsList = dnsList;
	}

	public static ArrayList<String> getServiceProviders() {
		return serviceProviders;
	}

	public static void setServiceProviders(ArrayList<String> serviceProviders) {
		Dns.serviceProviders = serviceProviders;
	}
}
