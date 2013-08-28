package hakd.internet;

/** Contains data being sent. */
public class Packet {
    private byte[] data;

    public Packet() {

    }

    public enum ByteCode {
	temo(0x0), p(0x1);

	byte code;

	ByteCode(int code) {
	    this.code = (byte) code;
	}
    }
}
