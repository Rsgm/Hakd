package hakd.game.pythonapi;

import hakd.gui.windows.server.Terminal;

public class PyDisplay {

    public static void write(Terminal t, String s) {
	t.addText(s);
    }

    public static void rewrite(Terminal t, String s, int lineFromBottom) {
	t.replaceText(s, lineFromBottom);
    }

}
