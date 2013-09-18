package hakd.internet;

import ai.pathfinder.Node;
import ai.pathfinder.Pathfinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import hakd.networks.devices.Device;
import hakd.other.RouterNode;
import org.python.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Connections are one way paths for data to travel through. This will control
 * all data transfers since it has speed and the routeDevices to take.
 */
@SuppressWarnings("unchecked")
public final class Connection {
	private Device host;
	private Device client;
	private Port clientPort;
	private Connection siblingConnection;

	private List<RouterNode> route;

	// other info
	private int speed;

	// gui
	private final Model cylinder;
	private final ModelInstance instance;

	public Connection(Device host, Device client, Port clientPort) {
		this.host = host;
		this.client = client;
		this.clientPort = clientPort;

		RouterNode hostN = host.getParentRouter().getNode();
		RouterNode clientN = client.getParentRouter().getNode();

		if(siblingConnection != null && !siblingConnection.getRoute().isEmpty()) {
			route.addAll(siblingConnection.getRoute());
			Lists.reverse(route);
			speed = siblingConnection.getSpeed();
		} else {
			Pathfinder p = new Pathfinder((ArrayList<Node>) host.getNetwork().getInternet().getRouterNodes());
			route = p.aStar(hostN, clientN);

			// sets the connection speed, slowest speed goes
			int[] speeds = new int[route.size()];
			for(RouterNode r : route) {
				speeds[route.indexOf(r)] = r.getRouter().getSpeed();
			}
			Arrays.sort(speeds);
			speed = speeds[0];
		}

		Vector3 v1 = host.getNetwork().getPosition().cpy();
		Vector3 v2 = client.getNetwork().getPosition().cpy();
		float distance = v1.dst(v2);

		ModelBuilder modelBuilder = new ModelBuilder();

		switch(host.getNetwork().getType()) {
			case BACKBONE:
				cylinder = modelBuilder.createCylinder(1.2f, distance, 1.2f, 5, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.Normal);
				break;
			case ISP:
				cylinder = modelBuilder.createCylinder(.6f, distance, .6f, 5, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);
				break;
			default:
				cylinder = modelBuilder.createCylinder(0.3f, distance, 0.3f, 5, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal);
				break;
		}

		instance = new ModelInstance(cylinder);

		Vector3 v3 = v1.cpy().add(v2).scl(.5f);
		Vector3 v4 = v2.cpy().sub(v3).nor();
		Vector3 v5 = v4.cpy().nor().crs(Vector3.Y).nor();

		instance.transform.translate(v3);
		instance.transform.rotate(v5, -(float) Math.toDegrees(Math.acos(v4.dot(Vector3.Y))));
	}

	public boolean close() {
		boolean test = host.getConnections().remove(this) || client.getConnections().remove(this);
		System.out.println("Connection closed:" + test);

		if(siblingConnection != null) {
			siblingConnection.close();
		}
		return test;
	}

	// TODO make methods for sending data and secure
	// data from host to client // does it need a data buffer?
	public Packet sendData(Byte[] data) {

		return null;
	}

	/**
	 * Connection request responses. These tell if a connection was successful
	 * and what happened, if not. These are based off of real HTTP status codes:
	 * https://en.wikipedia.org/wiki/List_of_HTTP_status_codes <br>
	 * <br>
	 * This will mostly be made up of 2xx, 4xx, and 5xx codes.
	 */
	public enum ConnectionStatus {
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

		int code;

		ConnectionStatus(int code) {
			this.code = code;
		}
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

	public ModelInstance getInstance() {
		return instance;
	}

	public Port getClientPort() {
		return clientPort;
	}

	public void setClientPort(Port clientPort) {
		this.clientPort = clientPort;
	}

	public Model getCylinder() {
		return cylinder;
	}

	public Connection getSiblingConnection() {
		return siblingConnection;
	}

	public void setSiblingConnection(Connection siblingConnection) {
		this.siblingConnection = siblingConnection;
	}

	public List<RouterNode> getRoute() {
		return route;
	}

	public void setRoute(List<RouterNode> route) {
		this.route = route;
	}
}
