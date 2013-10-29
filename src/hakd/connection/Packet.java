package hakd.connection;

/**
 * Contains data being sent.
 */
public final class Packet {
    private byte[] data;

    public Packet() {

    }

    public enum ByteCode {
        temo(0x0), p(0x1);

        final byte code;

        ByteCode(int code) {
            this.code = (byte) code;
        }
    }
}