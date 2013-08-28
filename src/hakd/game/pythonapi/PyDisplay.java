package hakd.game.pythonapi;

import hakd.networks.devices.Device;

public class PyDisplay {

    public static void write(Device d, String s) {
	d.getWindow().getTerminal().addText(s);
    }

    public static void rewrite(Device d, String s, int lineFromBottom) {
	d.getWindow().getTerminal().replaceText(s, lineFromBottom);
    }

}
