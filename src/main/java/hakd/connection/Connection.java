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

        host.log("Connection - " + client.getIp(), "Using " + clientPort.getProgram() + " through portNumber" + clientPort.getPortNumber() + " using " + clientPort.getProtocol() + "\n" + clientPort.getProgram() + ":" + clientPort.getPortNumber() + "->" + clientPort.getProtocol());
        client.log("Connection - " + host.getIp(), "Using " + hostPort.getProgram() + " through portNumber" + hostPort.getPortNumber() + " using " + hostPort.getProtocol() + "\n" + hostPort.getProgram() + ":" + hostPort.getPortNumber() + "->" + hostPort.getProtocol());
    }

    public void close() {
        host.log("Disconnection - " + client.getIp(), "Using " + clientPort.getProgram() + " through portNumber" + clientPort.getPortNumber() + " using " + clientPort.getProtocol() + "\n" + clientPort.getProgram() + ":" + clientPort.getPortNumber() + "->" + clientPort.getProtocol());
        client.log("Disconnection - " + host.getIp(), "Using " + hostPort.getProgram() + " through portNumber" + hostPort.getPortNumber() + " using " + hostPort.getProtocol() + "\n" + hostPort.getProgram() + ":" + hostPort.getPortNumber() + "->" + hostPort.getProtocol());

        host.getConnections().remove(client.getIp());
        client.getConnections().remove(host.getIp());
        clientPort.disconnect();
        clientPort.setProtocol(null);
    }

    /*
     * ---Example Connection Log--- 
     * Log 243.15.66.24.log
	 * Connecting with half life 3 through portNumber 28190 using LAMBDA
	 * half life 3:28190->LAMBDA
	 * 11-21 18:56:20
	 */

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
