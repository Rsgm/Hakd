package hakd.internet;

import hakd.networks.Network;

public interface ConnectableNetwork {

	// connects to a specified network on the specified port
	boolean Connect(Network network, String program, int port);

	// disconnects from the network
	boolean Disconnect(Network Network, String program, int port);

	// add/bind/register a port to a server to a program
	public boolean addPorts(int port, String program, int server);

	// remove a given port or set of port with a given port, server, or program
	public boolean removePort(int port, String program, int server);
}
