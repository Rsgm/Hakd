package hakd.game.gameplay;

import hakd.gui.Terminal;

import java.util.ArrayList;

import org.luaj.vm2.LuaValue;

public class Programs {
	private static Terminal	terminal;

	public static boolean run(ArrayList<String> args) { // look up coroutines for large programs
		reloadScripts();
		LuaValue chunk = globals.loadFile(scripts.get(0));
		for (String url : scripts) {
			if (url.matches("\\S*" + args.get(0) + ".lua")) { // this matches the full file path
				chunk.invoke(LuaValue.valueOf(url));
				return true;
			}
		}
		terminal.addText("'" + args.get(0) + "' is not a recognized command");

		return false;
	}

	public static Terminal getTerminal() {
		return terminal;
	}

	public static void setTerminal(Terminal terminal) {
		Programs.terminal = terminal;
	}
}
