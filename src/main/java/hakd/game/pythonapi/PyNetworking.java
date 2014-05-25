package hakd.game.pythonapi;

import hakd.game.Internet;
import hakd.gui.windows.device.Terminal;

public class PyNetworking {
    private final Terminal terminal;

    public PyNetworking(Terminal terminal) {
        this.terminal = terminal;
    }

//
//    public static Device get_device(String ip) {
//        return GameScreen.internet.getDevice(ip);
//    }

    public short[] ip_from_string(String ip) {
        return Internet.ipFromString(ip);
    }
}
