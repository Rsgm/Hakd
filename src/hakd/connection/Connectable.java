package hakd.connection;

import hakd.connection.Connection.ConnectionStatus;
import hakd.game.Internet.Protocol;
import hakd.networks.devices.Device;

public interface Connectable {

    /**
     * connects to a specified host on the specified portNumber
     */
    public ConnectionStatus Connect(Device client, Port port);

    /**
     * disconnects from the network
     */
    public boolean Disconnect(Connection c);

    /**
     * Creates a new portNumber with specified information.
     */
    public boolean openPort(String program, int portNumber, Protocol protocol);

    /**
     * A variant of openPort(String, int, Protocol) in which it uses an already
     * made port.
     */
    boolean openPort(Port port);

    /** */
    public boolean closePort(Port port);

    /**
     * Creates a log of the event
     */
    public void log(Device client, String program, int port, Protocol protocol);
}
