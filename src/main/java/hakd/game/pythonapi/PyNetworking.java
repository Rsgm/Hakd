package hakd.game.pythonapi;

import hakd.game.Internet;

public final class PyNetworking {
//
//    public static Device get_device(String ip) {
//        return GameScreen.internet.getDevice(ip);
//    }

    public static short[] ip_from_string(String ip) {
        return Internet.ipFromString(ip);
    }
}
