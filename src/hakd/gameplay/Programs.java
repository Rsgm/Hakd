package hakd.gameplay;

import java.io.File;
import java.util.ArrayList;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Programs {

	private static ArrayList<String>	scripts	= new ArrayList<String>();
	static Globals					globals	= JsePlatform.standardGlobals();

	public static void runProgram(String program) {
		reloadScripts();
		LuaValue chunk = globals.loadFile(scripts.get(0));
		for (String url : scripts) {
			if (url.matches("\\S*" + program + ".lua")) {
				chunk.call(LuaValue.valueOf(url));
				break;
			}
		}
	}

	public static void reloadScripts() {
		scripts.clear();
		scripts.addAll(listFilesForFolder(new File("lua/")));
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
}
