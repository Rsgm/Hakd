package hakd.networks.devices;

import hakd.internet.NetworkController;
import hakd.networks.Network;

import java.util.ArrayList;

import other.enumerations.Regions;

public class Dns extends Device { // TODO make this an object not a static class, and let DNSs communicate a bit.
	ArrayList<Network>	hosts;

	public Dns(Boolean publicDns, Network network) {
		super(network);

	}

	// --------methods--------
	public String assignIp(Regions region) { // assigns an ip to an object that requests one, also checks it and adds it to the dns list
		String ip;
		boolean taken;

		do {
			ip = generateIp(region);
			taken = false;

			for (Network n : hosts) {
				if (n.getIp().equals(ip)) {
					taken = true;
				}
			}
		} while (taken == true); // better practice to do this rather than while(true); and break;
		NetworkController.addPublicNetwork(findNetwork(ip));
		return ip;
	}

	// used to create a realistic, random ip based on registered ipv4 IRL(or AFK if your from sweeden). used mostly with the assign ip method, but can
	// be useful for other things
	public String generateIp(Regions region) {
		switch (region) { //
			default:
				return (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
						+ (int) (Math.random() * 256);
			case COMPANIES:
				return (int) (Math.random() * 50 + 6) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
						+ (int) (Math.random() * 256);
			case NA:
				return (int) (Math.random() * 13 + 63) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
						+ (int) (Math.random() * 256);
			case EUROPE:
				return (int) (Math.random() * 15 + 77) + "." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256) + "."
						+ (int) (Math.random() * 256);
		}
	}

	// registers a url to an ip just so not everything is an ip // ip can only be a player's ip if they buy it
	public boolean addUrl(String ip, String address) {
		if (address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$") && NetworkController.getIp(address) == null) { // address regex
			findNetwork(ip).setAddress(address);
			return true; // "you have successfully registered the url " + url + " for the ip " + ip;
		}
		return false; // "Sorry, either that URL is already registered, or a bug)."
	}

	public Network findNetwork(String address) {
		if (!address.matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b")) { // ip regex
			address = NetworkController.getIp(address);
		}

		for (Network n : hosts) {
			if (n.getIp().equals(address)) {
				return n;
			}
		}
		return null;
	}

	public String getIp(String address) {
		for (Network n : hosts) {
			if (n.getAddress().equals(address)) {
				return n.getIp();
			}
		}
		return null;
	}

	int getRandomNumber() {
		return 4; // chosen by fair dice roll
					// guaranteed to be random
	}

	{ // old method
// public static void addConnection(Network network, String address) {
// Vector<Line> lines = GameGui.getLines();
// int radius = GameGui.getRadius();
// double r, a, b, c, xTrig, yTrig, x1, y1, x2, y2; // triangle>ABC
//
// x1 = network.getxCoordinate();
// y1 = network.getyCoordinate();
// x2 = Network.getNetworks().get(findNetwork(address)).getxCoordinate();
// y2 = Network.getNetworks().get(findNetwork(address)).getyCoordinate();
// r = radius;
//
// a = 1; // line BC, point C is only (x2,y2+1)
// b = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 + 1 - y1) * (y2 + 1 - y1)); // line CA
// c = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)); // line AB
//
// Line line = new Line();
// if (x2 - x1 != 0) {
// xTrig = r * Math.sin(Math.acos((a * a - b * b + c * c) / (2 * a * c)));
// yTrig = r * Math.cos(Math.acos((a * a - b * b + c * c) / (2 * a * c)));
// if (x2 - x1 > 0) {
// line.setStartX(x1 + xTrig);
// line.setStartY(y1 - yTrig);
// line.setEndX(x2 - xTrig);
// line.setEndY(y2 + yTrig);
// } else if (x2 - x1 < 0) {
// line.setStartX(x1 - xTrig);
// line.setStartY(y1 - yTrig);
// line.setEndX(x2 + xTrig);
// line.setEndY(y2 + yTrig);
// }
// } else {
// if (y2 - y1 > 0) {
// line.setEndX(x2);
// line.setEndY(y2 + r);
// } else if (y2 - y1 < 0) {
// line.setEndX(x2);
// line.setEndY(y2 - r);
// } else {
// return;
// }
// return;
// }
// if (Double.isNaN(yTrig) || Double.isNaN(xTrig)) {
// return;
// }
//
// line.setFill(Paint.valueOf("black"));
// line.setStrokeWidth(1.15);
// line.setOpacity(0.3);
// lines.add(line);
// Connection.getConnection().add(new Connection(network, Network.getNetworks().get(findNetwork(address))));
//
// // GameGui.updateRegion();
// return;
// }
	}

	// --------getters/setters--------
	public ArrayList<Network> getHosts() {
		return hosts;
	}

	public void setHosts(ArrayList<Network> hosts) {
		this.hosts = hosts;
	}
}
