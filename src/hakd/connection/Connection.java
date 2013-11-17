package hakd.connection;

import hakd.networks.devices.Device;

import java.net.Socket;

/**
 * Connections are one way paths for data to travel through. This will control
 * all data transfers since it has speed and the routeDevices to take.
 */
@SuppressWarnings("unchecked")
public final class Connection {
    private Device host;
    private Device client;
    private Port clientPort;

    private Socket clientSocket;
    private Socket hostSocket;

    // other info
    private int speed;

    // gui


    public Connection(Device host, Device client, Port clientPort, Socket sClient, Socket sHost) {
        this.host = host;
        this.client = client;
        this.clientPort = clientPort;

        clientSocket = sClient;
        hostSocket = sHost;
    }

    public void close() {
        host.getConnections().remove(client.getIp());
        client.getConnections().remove(host.getIp());
    }

    public Device getHost() {
        return host;
    }

    public void setHost(Device host) {
        this.host = host;
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

    public Socket getClientSocket() {
        return clientSocket;
    }

    public Socket getHostSocket() {
        return hostSocket;
    }
}
