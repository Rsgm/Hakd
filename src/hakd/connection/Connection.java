package hakd.connection;

import hakd.networks.devices.Device;

/**
 * Connections are one way paths for data to travel through. This will control
 * all data transfers since it has speed and the routeDevices to take.
 */
@SuppressWarnings("unchecked")
public final class Connection {
	private Device server;
	private Device client;
	private Port clientPort;

	// other info
	private int speed;

	// gui

	public Connection(Device host, Device client, Port clientPort) {
		this.server = host;
		this.client = client;
		this.clientPort = clientPort;

	}

	public boolean close() {
		boolean test = server.getConnections().remove(this) || client.getConnections().remove(this);
		System.out.println("Connection closed:" + test);

		return test;
	}

	// TODO make methods for sending data and secure
	// data from server to client // does it need a data buffer?
	public Packet sendData(Byte[] data) {

		return null;
	}

	public Device getServer() {
		return server;
	}

	public void setServer(Device server) {
		this.server = server;
	}

	public Device getClient() {
		return client;
	}

	public void setClient(Device client) {
		this.client = client;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Port getClientPort() {
		return clientPort;
	}

	public void setClientPort(Port clientPort) {
		this.clientPort = clientPort;
	}
}
