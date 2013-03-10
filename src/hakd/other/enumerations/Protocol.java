package hakd.other.enumerations;

public enum Protocol { // protocol(port)
	FTP(21), SSH(22), SMTP(25), WHOIS(43), DNS(53), HTTP(80), HTTPS(443), STEAM(1725), XBOX(3074), MYSQL(3306), RDP(3389), WOW(3724), UPUP(5000),
	IRC(6667), TORRENT(6881), LAMBDA(27015), COD(28960), LEET(31337);

	public int	port;	// these are only default ports

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
}