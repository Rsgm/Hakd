package hakd.game.pythonapi;

import hakd.networks.devices.Device;

public class Display {

    public static void write(Device d, String s) {
	d.getWindow().getTerminal().addText(s);
    }

}
