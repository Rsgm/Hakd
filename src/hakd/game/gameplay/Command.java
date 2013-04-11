package hakd.game.gameplay;

import hakd.gui.Terminal;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Command {
	private final String			input;
	private final Player			player;
	private final Terminal			terminal;

	// lua
	private final ArrayList<String>	scriptPaths	= new ArrayList<String>();
	private static Globals			globals		= JsePlatform.standardGlobals();

	public Command(String input, Player player) { // this may need to tell if a player issued it, so it won't write to the display
		this.input = input;
		this.player = player;
		this.terminal = player.getTerminal();

		reloadScripts();
		run();
	}

// runs the desired command on a separate thread I think, not that any program would be intensive at all, although while(true)...
	public void run() {
		Timer.post(new Task() {
			@Override
			public void run() {
				// TODO add scan(ports), cd(directory), cs(int server)/*change server*/, traceroute(address), clone server to server(maybe just hard
// drive to hard drive)

				Scanner scanner = new Scanner(input);
				ArrayList<String> args = new ArrayList<String>();
				boolean inQuotes = false; // credit to redditor BritPack for this, thank you

				if (input.matches(player.getCurrentNetwork().getIp() + ">.+")) {
					scanner.skip(".+>");
					scanner.useDelimiter("\\s+");
					
				    while(scanner.hasNext()) {
				        //Get the current token
				        String next = scanner.next();
				        //Are we inside quotes
				        if(inQuotes) {
				            //If so, add this string to the end of the last value
				            int offset = args.size()-1;
				            args.set(offset, args.get(offset) + " " + next);
				            //If it ends in a quotation then we exit quotes
				            if(next.endsWith("\"")) {
				                inQuotes = false;
				            }
				        } else {
				            //Add the string to the values
				            args.add(next);
				            //Are we moving into quotes?
				            if(next.startsWith("\"") && scanner.hasNext("\\S*\"")) {
				                inQuotes = true;
				            }
				        }
				    }
					

//					scanner.skip(".+>");
//					scanner.useDelimiter("\\s+");
//
//					for (int i = 0; i < input.length(); i++) {
//						if (scanner.hasNext()) {
//							args.add(scanner.next());
//						} else {
//							break;
//						}
//					}
//
//					// adds up the amount of quotes total
//					int quoteCount = 0;
//					for (String token : args) {
//						if (token.matches("^\".*")) {
//							quoteCount++;
//						} else if (token.matches(".*\"$") && quoteCount % 2 == 1) {
//							quoteCount++;
//						}
//					}
//
//					// fixes the strange argument outputs when using odd number of quotes
//					if (quoteCount % 2 != 0) {// I don't know this may be more of a feature than a bug(code injection)
//						return;
//					} // TODO, doesnt fix ["test" "test"], it returns as one arg, just re-write at some point
//
//					ArrayList<String> tempArray = new ArrayList<String>(); // used for the loop
//					tempArray.addAll(args);
//
//					// this has strange problems with strange text for example [hello test" "test], so TODO match input to "[\\w+\\s+[\".*\"]]+", idk
//					for (String token : tempArray) {
//						// tests for a quote at the beginning and that there is at least two quotes left
//						if (token.matches("^\".*") && quoteCount >= 2) {
//							int index = args.indexOf(token);
//							String s = token; // I could not think of a good name for this
//
//							// for every token without a quote add it to the sting at tokenIndex in args, if it has a quote, do the same but break
//							while (!args.get(index).matches(".*\"$")) {
//								if (index + 2 >= args.size()) {
//									break;
//								}
//								s += " " + args.remove(index + 1);
//								args.remove(index);
//							}
//							args.remove(token); // fixes the bug if there is but one space, because break is called before it can remove anything
//							s += " " + args.remove(index);
//							args.add(index, s);
//							quoteCount -= 2;
//						}
//					}
					System.out.println(args.toString());

// if (terminal.isMenu()) {
// args.set(0, "menu-" + args.get(0)); // TODO what to do?
// }

					runLua(args);
				}
				scanner.close();
			}
		});
	}

	// runs the corresponding lua function/file/program
	public boolean runLua(ArrayList<String> args) { // look up coroutines for large programs
		LuaValue[] vararray = new LuaValue[args.size() - 1];

		for (int i = 0; i < args.size() - 1; i++) {
			vararray[i] = LuaValue.valueOf(args.get(i + 1));
		}
		Varargs vargs = LuaValue.varargsOf(vararray);

		for (String url : scriptPaths) { // this is only running programs in lua/ but nothing deeper
			if (url.matches(".*" + args.get(0) + "\\.lua")) { // this matches the full file path
				LuaValue chunk = globals.loadFile(url); // I don't know why it is called chunk, it's just what the luaj examples showed
				terminal.addText(chunk.invoke(vargs));
				return true;
			}
		}
		terminal.addText("\"" + args.get(0) + "\" is not a recognized command");
		return false;
	}

	// reloads the path list for the lua files so you can add stuff during runtime if needed // TODO have an option to disable this
	public void reloadScripts() { // this is run on a seperate thread with the script, maybe it isn't that intensive
		scriptPaths.clear();
		scriptPaths.addAll(listFilesForFolder(new File("lua\\")));
	}

	private ArrayList<String> listFilesForFolder(File folder) {
		String parents = folder.getPath();
		ArrayList<String> urls = new ArrayList<String>();

		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
// System.out.println(parents + "\\" + fileEntry.getName());
				urls.add(parents + "\\" + fileEntry.getName());
			}
		}
		return urls;
	}
}

/*in run
				switch (args.get(0)) { // change all scanner groups to args
					default:
						terminal.addText("'"+ args.get(0)+ "' is not a recognized command");
						break;
					case "help":
						help();
						break;
					case "add":
						NetworkController.getNetworks().add(new Network(NetworkType.NPC));
						break;
					case "quit":
						Hakd.quitGame(null); // TODO this will give a null pointer
						break;
					case "test":
						System.out.println("Total networks - " + NetworkController.getNetworks().size());
						break;
					case "home":
						PlayerController.setCurrentNetwork(PlayerController.getHomeNetwork());
						PlayerController.setCurrentServer(PlayerController.getHomeNetwork();
						break;
					case "run": // runs a lua script program, can I do it without using the word "run"? yes, make all commands lua
						break;
					case "debug":
						hakd.gui.Windows.debug(args.get(1));
						break;}*/