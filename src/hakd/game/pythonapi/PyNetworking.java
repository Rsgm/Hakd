package hakd.game.pythonapi;

import hakd.game.Internet;
import hakd.gui.screens.GameScreen;
import hakd.networks.devices.Device;

public final class PyNetworking {

	public static void newSocket() {

	}

	public static Device get_device(String ip) {
		return GameScreen.internet.getDevice(ip);
	}

	public static short[] ip_from_string(String ip) {
		return Internet.ipFromString(ip);
	}
}
