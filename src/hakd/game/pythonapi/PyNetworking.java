package hakd.game.pythonapi;

import hakd.gui.screens.GameScreen;
import hakd.internet.Internet;
import hakd.networks.devices.Device;

public class PyNetworking {

    public static void newSocket() {

    }

    public static Device get_device(short[] ip) {
	return GameScreen.internet.findDevice(ip);
    }

    public static short[] ip_from_string(String ip) {
	return Internet.ipFromString(ip);
    }
}
