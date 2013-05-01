package hakd.game.functions;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class MenuLib extends TwoArgFunction {
	/* This library has commands that should only be run on the menu terminal.*/

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue library = tableOf();
		library.set("NewGame", new Start());
		env.set("menu", library);
		return library;
	}

	static class Start extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue arg) {

			return null;
		}

	}

}
