package hakd.connection;

import hakd.networks.devices.Device;

import java.io.IOException;

public interface Connectable {

    /**
     * Connects to the host on the specified portNumber.
     */
    public boolean connect(Device client, Port clientPort, int port) throws IOException;

    /**
     * Disconnects from the network.
     */
    public void disconnect(Connection c);

    /**
     * Opens the given port on the device.
     */
    boolean openPort(Port port);

    /**
     * Closes the given port on the devide, and any connections using it.
     */
    public boolean closePort(int port);

    /**
     * Creates a log of the event
     */
    public void log(Device client, String program, int port, String protocol);

    /**
     * Connection request responses. These tell if a connection was successful
     * and what happened, if not. These are based off of real HTTP status codes:
     * https://en.wikipedia.org/wiki/List_of_HTTP_status_codes <br><br>
     * This will mostly be made up of 2xx, 4xx, and 5xx codes.
     */
    enum ConnectionStatus {
        /**
         * Standard successful connection response.
         */
        OK(200),

        /**
         * The request cannot be fulfilled due to bad syntax.
         */
        Bad_Request(400),

        /**
         * Similar to 403 Forbidden, but specifically for use when
         * authentication is required and has failed or has not yet been
         * provided.
         */
        Unauthorized(401),

        /**
         * The request was a valid request, but the server is refusing to
         * respond to it.
         */
        Forbidden(403),

        /**
         * The requested resource could not be found but may be available again
         * in the future.
         */
        Not_Found(404),

        /**
         * The server timed out waiting for the request.
         */
        Request_Timeout(408),

        /**
         * ...
         */
        Im_A_Teapot(418),

        /**
         * The user has sent too many requests in a given amount of time.
         */
        Too_Many_Requests(429),

        /**
         * Defined in the internet draft
         * "A New HTTP Status Code for Legally-restricted Resources". Intended
         * to be used when resource access is denied for legal reasons, e.g.
         * censorship or government-mandated blocked access. A reference to the
         * 1953 dystopian novel Fahrenheit 451, where books are outlawed.
         */
        Unavailable_For_Legal_Reasons(451),

        /**
         * A generic error message, given when no more specific message is
         * suitable.
         */
        Internal_Server_Error(500),

        /**
         * The server is currently unavailable (because it is overloaded or down
         * for maintenance). Generally, this is a temporary state.
         */
        Service_Unavailable(503),

        /**
         * Used when the server has a limited bandwidth overall or per
         * connection.
         */
        Bandwidth_Limit_Exceeded(509),

        /**
         * The client needs to authenticate to gain network access.
         */
        Network_Authentication_Required(511);

        final int code;

        ConnectionStatus(int code) {
            this.code = code;
        }
    }
}
