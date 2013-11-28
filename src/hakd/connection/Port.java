package hakd.connection;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Ports connect to other ports and send data between each other. There may only be one port connected to another at a time.
 */
public final class Port {
    private String program;
    private int portNumber;
    private String protocol;

    private ConcurrentLinkedQueue<Integer> in;
    private final ConcurrentLinkedQueue<Integer> out;

    private boolean connected;
    private Port remotePort;

    public Port(String program, int portNumber) {
        this.program = program;
        this.portNumber = portNumber;

        out = new ConcurrentLinkedQueue<Integer>();
    }

    public Port(String program, int portNumber, String protocol) {
        this.program = program;
        this.portNumber = portNumber;
        this.protocol = protocol.toUpperCase();

        out = new ConcurrentLinkedQueue<Integer>();
    }

    public synchronized void connect(Port port) throws IOException {
        if (connected || port.isConnected()) {
            throw new IOException("Port is already in use.");
        }

        remotePort = port;
        port.remotePort = port;

        connected = true;
        remotePort.connected = true;

        in = port.getOut();
        remotePort.in = this.out;
    }

    /**
     * Reads data sent from the remote port.
     *
     * @return Returns the first byte in the data queue. <br> Returns -1 if the queue is empty.
     */
    public int read() {
        Integer b = in.poll();
        if (b == null) {
            return -1;
        } else {
            return b;
        }
    }

    /**
     * Sends data to the remote port. Only accepts bytes(0-255). If b is larger than 255, it will take the last 8 bits.
     */
    public void write(int b) {
        out.offer(b % 256);
        // TODO sleep(inverse of connection speed or something)
    }

    public void write(int[] b) {
        for (int i : b) {
            out.offer(i);
        }
    }

    /**
     * Disconnects from the remote port.
     */
    public void disconnect() {
        in.clear(); // clear the history or else any new connection can see your past sent data
        out.clear();

        connected = false;
        remotePort.connected = false;

        in = null;
        remotePort.in = null;

        remotePort.remotePort = null;
        remotePort = null;
    }

    public String getProgram() {
        return program;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isConnected() {
        return connected;
    }

    public ConcurrentLinkedQueue<Integer> getIn() {
        return in;
    }

    public ConcurrentLinkedQueue<Integer> getOut() {
        return out;
    }

    public Port getRemotePort() {
        return remotePort;
    }

    void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
