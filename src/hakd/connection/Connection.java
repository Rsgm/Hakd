package hakd.connection;

import hakd.networks.devices.Device;

/**
 * Connections are one way paths for data to travel through. This will control
 * all data transfers since it has speed and the routeDevices to take.
 */
public final class Connection {
    private Device host;
    private Device client;

    private Port clientPort;
    private Port hostPort;

    // other info
    private int speed;

    // gui


    public Connection(Device host, Device client, Port clientPort, Port hostPort) {
        this.host = host;
        this.client = client;

        this.clientPort = clientPort;
        this.hostPort = hostPort;

        clientPort.setProtocol(hostPort.getProtocol());
    }

    public void close() {
        host.getConnections().remove(client.getIp());
        client.getConnections().remove(host.getIp());
        clientPort.disconnect();
        clientPort.setProtocol(null);
    }

    public Device getHost() {
        return host;
    }

    public Device getClient() {
        return client;
    }

    public int getSpeed() {
        return speed;
    }

    public Port getClientPort() {
        return clientPort;
    }

    public Port getHostPort() {
        return hostPort;
    }
}
