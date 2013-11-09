package hakd.connection;

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
