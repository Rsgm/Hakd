package hakd.networking;

import hakd.networking.devices.Dns;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

public class Connection {

	private int								speed;
	private final Network[]					network		= new Network[2];
	private String ip;

//	private// --------constructor--------
	public Connection(Network net0, Network net1, Protocol protocol) {
		network[0] = net0;
		network[1] = net1;


		if (network[0].getSpeed() >= network[1].getSpeed()) {
			speed = network[0].getSpeed();
		} else {
			speed = network[1].getSpeed();
		}
	}

	// --------methods-------- // like sending data and secure data
	public void close() { // TODO removes this connection and updates the pane
		for(Dns d:NetworkController.getPublicDns()}
			if(d.)){
				getConnections.remove(this);
	}

	// --------enumerations--------
	public enum Protocol { // protocol(port)
		FTP(21), SSH(22), SMTP(25), WHOIS(43), DNS(53), HTTP(80), HTTPS(443), STEAM(1725), XBOX(3074), MYSQL(3306), RDP(3389), WOW(3724), UPUP(5000),
		IRC(6667), TORRENT(6881), LAMBDA(27015), COD(28960), LEET(31337);
		private int	value;

		private Protocol(int value) {
			this.value = value;
		}
	};

	// --------getters/setters--------

}
