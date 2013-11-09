package hakd.connection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import hakd.networks.devices.Device;

/**
 * Connections are one way paths for data to travel through. This will control
 * all data transfers since it has speed and the routeDevices to take.
 */
@SuppressWarnings("unchecked")
public final class Connection {
	private Device server;
	private Device client;
	private Port clientPort;

	// other info
	private int speed;

	// gui
	private final Model cylinder;
	private final ModelInstance instance;

	public Connection(Device host, Device client, Port clientPort) {
		this.server = host;
		this.client = client;
		this.clientPort = clientPort;


		Vector3 v1 = host.getNetwork().getSpherePosition().cpy();
		Vector3 v2 = client.getNetwork().getSpherePosition().cpy();
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
		boolean test = server.getConnections().remove(this) || client.getConnections().remove(this);
		System.out.println("Connection closed:" + test);

		return test;
	}

	// TODO make methods for sending data and secure
	// data from server to client // does it need a data buffer?
	public Packet sendData(Byte[] data) {

		return null;
	}

	public Device getServer() {
		return server;
	}

	public void setServer(Device server) {
		this.server = server;
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
}
