package hakd.networking;

import hakd.gui.GameGui;
import hakd.network.Network;

import java.util.ArrayList;
import java.util.Vector;

import javafx.scene.shape.Line;

public class Connection {

	private static ArrayList<Connection>	connection	= new ArrayList<Connection>();
	private static Vector<Line>				lines		= GameGui.getLines();

	private int								speed;
	private final Network[]					network		= new Network[2];
	private final Line						line;

	// --------constructor--------
	Connection(Network net0, Network net1) {
		network[0] = net0;
		network[1] = net1;

		line = null;

		if (net0.getSpeed() >= net1.getSpeed()) {
			speed = net0.getSpeed();
		} else {
			speed = net1.getSpeed();
		}
	}

	// --------methods-------- // like sending data and secure data
	public void close() { // removes this connection and updates the
		lines.remove(line);
		connection.remove(this);
	}

	// --------getters/setters--------
	static ArrayList<Connection> getConnection() {
		return connection;
	}

	static void setConnection(ArrayList<Connection> connection) {
		Connection.connection = connection;
	}
}
