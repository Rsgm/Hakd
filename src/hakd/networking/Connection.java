package hakd.networking;

import hakd.networking.networks.devices.Device;
import hakd.networking.networks.devices.Dns;

import java.util.ArrayList;

public class Connection {
	private Device	sender;	// not the best names, but it beats master/slave in political correctness, though not geekiness
	private Device	reciever;
	private String	senderIp;
	private String	recieverIp;

	// other info
	private int		speed;

// private// --------constructor--------
	public Connection(Device sender, Device reciever, Protocol protocol) {
		this.sender = sender;
		this.reciever = reciever;

		if (sender.getNetwork().getSpeed() >= reciever.getNetwork().getSpeed()) { // slowest speed goes
			speed = reciever.getNetwork().getSpeed();
		} else {
			speed = sender.getNetwork().getSpeed();
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
	}

	// --------getters/setters--------
	public Device getSender() {
		return sender;
	}

	public void setSender(Device sender) {
		this.sender = sender;
	}

	public Device getReciever() {
		return reciever;
	}

	public void setReciever(Device reciever) {
		this.reciever = reciever;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public String getRecieverIp() {
		return recieverIp;
	}

	public void setRecieverIp(String recieverIp) {
		this.recieverIp = recieverIp;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	};
}
