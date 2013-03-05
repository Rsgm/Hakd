package hakd.internet;

import hakd.networks.devices.Device;
import hakd.networks.devices.Dns;

import java.util.ArrayList;

public class Connection { // connections are one way, and one connection to one port // for two way connections just use two connections

	private Device	host;
	private Device	client;
	private String	hostIp;
	private String	clientIp;

	// other info
	private int		speed;

// private// --------constructor--------
	public Connection(Device host, Device client, Protocol protocol) {
		this.host = host;
		this.client = client;

		if (host.getNetwork().getSpeed() >= client.getNetwork().getSpeed()) { // slowest speed goes
			speed = client.getNetwork().getSpeed();
		} else {
			speed = host.getNetwork().getSpeed();
		}

	}

	// --------methods-------- // like sending data and secure data
	public void close() { // TODO removes this connection and updates the pane
		ArrayList<Dns> dnsList = NetworkController.getPublicDns();

		for (Dns d : dnsList) {
			if (d.getHosts().contains(this)) {
				d.getHosts().remove(this);
			}
		}
	}

	// --------enumerations--------
	public enum Protocol { // protocol(port)
		FTP(21), SSH(22), SMTP(25), WHOIS(43), DNS(53), HTTP(80), HTTPS(443), STEAM(1725), XBOX(3074), MYSQL(3306), RDP(3389), WOW(3724), UPUP(5000),
		IRC(6667), TORRENT(6881), LAMBDA(27015), COD(28960), LEET(31337);
		int	port;

		private Protocol(int port) {
			this.port = port;
		}

		public static Protocol getProtocol(int port) {
			for (Protocol p : Protocol.values()) {
				if (p.port == port) {
					return p;
				}
			}
			return HTTP;
		}
		// --------getters/setters--------
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
}
