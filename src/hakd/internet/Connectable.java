package hakd.internet;

import hakd.internet.NetworkController.Protocol;
import hakd.networks.devices.Device;

public interface Connectable {

    // connects to a specified host on the specified port
    public boolean Connect(Device client, String program, int port,
	    NetworkController.Protocol protocol);

    // disconnects from the network
    public boolean Disconnect(Device client, String program, int port);

    // add/bind/register a port to a server to a program
    public boolean addPorts(Device device, String program, int port,
	    Protocol protocol);

    // remove a given port or set of port with a given port, server, or program
    public boolean removePort(int port);

    // add a log of the event
    public void log(Device client, String program, int port, Protocol protocol);
}
