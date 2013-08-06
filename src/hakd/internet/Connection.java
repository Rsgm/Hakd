package hakd.internet;

import hakd.internet.Internet.Protocol;
import hakd.networks.devices.Device;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public final class Connection {
    /*
     * connections are one way, and one connection to one port, for two way
     * connections just use two connections
     */

    private Device host;
    private Device client;
    private String hostIp;
    private String clientIp;

    // other info
    private int speed;

    // gui
    private final Model cylinder;
    private final ModelInstance instance;

    // --------constructor--------
    public Connection(Device host, Device client, Protocol protocol) {
	this.host = host;
	this.client = client;

	if (host.getNetwork().getSpeed() >= client.getNetwork().getSpeed()) { // slowest
									      // speed
									      // goes
	    speed = client.getNetwork().getSpeed();
	} else {
	    speed = host.getNetwork().getSpeed();
	}

	Vector3 v1 = host.getNetwork().getPosition().cpy();
	Vector3 v2 = client.getNetwork().getPosition().cpy();
	float distance = v1.dst(v2);

	ModelBuilder modelBuilder = new ModelBuilder();
	cylinder = modelBuilder.createCylinder(0.15f, distance, 0.15f, 5,
		new Material(ColorAttribute.createDiffuse(Color.GREEN)),
		Usage.Position | Usage.Normal);
	instance = new ModelInstance(cylinder);

	Vector3 v3 = v1.cpy().add(v2).scl(.5f);
	Vector3 v4 = v2.cpy().sub(v3).nor();
	Vector3 v5 = v4.cpy().nor().crs(Vector3.Y).nor();

	instance.transform.translate(v3);
	instance.transform.rotate(v5,
		-(float) Math.toDegrees(Math.acos(v4.dot(Vector3.Y))));
    }

    // --------methods-------- // TODO make methods for sending data and secure
    // data from host to client // does it need a data buffer?
    public void close() {
	host.getNetwork().getConnections().remove(this);
	client.getNetwork().getConnections().remove(this);
    }

    // --------getters/setters--------
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

    public String getHostIp() {
	return hostIp;
    }

    public void setHostIp(String hostIp) {
	this.hostIp = hostIp;
    }

    public String getClientIp() {
	return clientIp;
    }

    public void setClientIp(String clientIp) {
	this.clientIp = clientIp;
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
}
