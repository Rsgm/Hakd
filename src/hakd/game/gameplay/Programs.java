package hakd.game.gameplay;

import hakd.gui.Terminal;

import java.io.File;
import java.util.ArrayList;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Programs {
	private static ArrayList<String>	scripts	= new ArrayList<String>();
	private static Globals				globals	= JsePlatform.standardGlobals();
	private static Terminal				terminal;

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

	public static void reloadScripts() {
		scripts.clear();
		scripts.addAll(listFilesForFolder(new File("Lua/")));
	}

	private static ArrayList<String> listFilesForFolder(File folder) {
		String parents = folder.getPath();
		ArrayList<String> urls = new ArrayList<String>();

		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(parents + "\\" + fileEntry.getName());
				urls.add(parents + "\\" + fileEntry.getName());
			}
		}
		return urls;
	}

	public static Terminal getTerminal() {
		return terminal;
	}

	public static void setTerminal(Terminal terminal) {
		Programs.terminal = terminal;
	}
}
