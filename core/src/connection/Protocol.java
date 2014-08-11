package connection;

/**
 * Protocols tell how to encode and decode a connection data stream. Protocols have default ports, but they can be used on other ports.
 */
public enum Protocol {
    /**
     * File transfer protocol.
     */
    FTP(21),

    /**
     * Secure shell.
     */
    SSH(22),

    /**
     * Simple mail transfer protocol.
     */
    SMTP(25),

    WHOIS(43),

    DNS(53),

    /**
     * Hyper text transfer protocol(webserver).
     */
    HTTP(80),

    /**
     * Hyper text transfer protocol(webserver).
     */
    HTTPS(443),

    STEAM(1725),

    // I may remove COD for this
    XBOX(3074),

    MYSQL(3306),

    // Should be terminal server, but IMO this is more common
    /**
     * Remote desktop.
     */
    RDP(3389),

    WOW(3724),

    /**
     * Universal plug and play
     */
    UPUP(5000),

    IRC(6667),

    TORRENT(6881),

    LAMBDA(27015),

    //Have seemingly live chat with children screeming into mics
    COD(28960),

    /**
     * Default backdoor
     */
    LEET(31337);

    public final int portNumber; // these are only default ports

    private Protocol(int portNumber) {
        this.portNumber = portNumber;
    }

    public static Protocol getProtocol(int port) {
        for (Protocol p : Protocol.values()) {
            if (p.portNumber == port) {
                return p;
            }
        }
        return HTTP;
    }

//    public static void ftp(int b) {
//
//    }

}
