package hakd.internet;

import hakd.networks.devices.Device;

public interface Connectable {

	// connects to a specified host on the specified port
	public boolean Connect(Device client, String program, int port);

	// disconnects from the network
	public boolean Disconnect(String program, int port);

	// add/bind/register a port to a server to a program
	public boolean addPorts(String program, int port);

	// remove a given port or set of port with a given port, server, or program
	public boolean removePort(String program, int port);

	// add a log of the event
	public void log(Device client, String program, int port);
}
