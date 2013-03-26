package hakd.gui;

import hakd.game.InputHandler;
import hakd.game.gameplay.Command;

import java.util.Vector;

public class Terminal {
	private final boolean			menu;
	private final Vector<Command>	commandQueue	= new Vector<Command>();	// holds commands made, that are ready to be run
	private final InputHandler		input;

	public Terminal(boolean isMenu) {
		input = new InputHandler(isMenu);
		menu = isMenu;
	}

	public boolean isMenu() {
		return menu;
	}

	public Vector<Command> getCommandQueue() {
		return commandQueue;
	}

	public InputHandler getInput() {
		return input;
	}
}
