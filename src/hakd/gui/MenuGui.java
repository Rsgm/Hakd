package hakd.gui;

import hakd.Hakd;
import hakd.gameplay.PlayerController;
import hakd.gui.threads.RunMenu;
import hakd.network.Network;

import java.util.Scanner;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MenuGui {

	private static int				recall	= 0;
	private static ArrayList<String>	lines	= new ArrayList<String>();

	public static void startMenu() {
		GuiController.root.getChildren().add(GuiController.menuInterface);
		GuiController.menuInterface.setStyle("pane-background");

		AnchorPane.setBottomAnchor(GuiController.menuInterface, 0.0);
		AnchorPane.setLeftAnchor(GuiController.menuInterface, 0.0);
		AnchorPane.setRightAnchor(GuiController.menuInterface, 0.0);
		AnchorPane.setTopAnchor(GuiController.menuInterface, 0.0);

		// menu terminal
		GuiController.menuInterface.getChildren().addAll(GuiController.menuTerminal, GuiController.menuInput);
		GuiController.menuTerminal.setEditable(false);
		GuiController.menuInput.setEditable(true);
		GuiController.menuTerminal.setMinHeight(25.0);
		GuiController.menuTerminal.getStyleClass().add("terminal-text");
		GuiController.menuInput.getStyleClass().add("terminal-text");
		GuiController.menuInput.setId("terminal-input");
		GuiController.menuInput.requestFocus();

		AnchorPane.setBottomAnchor(GuiController.menuTerminal, 20.0);
		AnchorPane.setLeftAnchor(GuiController.menuTerminal, 0.0);
		AnchorPane.setRightAnchor(GuiController.menuTerminal, 0.0);
		AnchorPane.setTopAnchor(GuiController.menuTerminal, 0.0);
		AnchorPane.setBottomAnchor(GuiController.menuInput, 0.0);
		AnchorPane.setLeftAnchor(GuiController.menuInput, 0.0);
		AnchorPane.setRightAnchor(GuiController.menuInput, 0.0);

		// event handlers
		GuiController.menuInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent input) {
				if (input.getCode() == KeyCode.ENTER) {
					lines.add(GuiController.menuInput.getText());
					GuiController.menuTerminal.setText(GuiController.menuTerminal.getText() + "\n" + GuiController.menuInput.getText());
					menuCommands(GuiController.menuInput.getText());
					GuiController.menuInput.setText("");
					GuiController.menuTerminal.end();
					GuiController.menuInput.end();
					recall = lines.size();
				} else if (input.getCode() == KeyCode.DOWN && recall < lines.size() - 1) {
					recall++;
					GuiController.menuInput.setText(lines.get(recall));
					GuiController.menuInput.end();
				} else if (input.getCode() == KeyCode.UP && recall > 0) {
					recall--;
					GuiController.menuInput.setText(lines.get(recall));
					GuiController.menuInput.end();
				} else if (input.getCode() == KeyCode.ESCAPE) {
					GuiController.menuInput.setText("");
					recall = lines.size();
				}
				GuiController.menuInput.end();
			}
		});
		GuiController.menuTerminal.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				GuiController.menuInput.requestFocus();
				GuiController.menuInput.end();
			}
		});

		// game
		new RunMenu();
	}

	static void menuCommands(String input) {
		System.out.println(input);

		if (input.equals("help")) { // TODO add more stuff to do outside of the game
			GuiController.menuTerminal.setText(GuiController.menuTerminal.getText() + "\nCommands:\nhelp - shows this list\nquit - exits this game"
					+ "\nstart [player name] - starts the game" + "\nnmap - scans the computers on the network for open ports and other stuff"
					+ "\nsettings - opens settings window");
		} else if (input.equals("quit")) {
			Hakd.quitGame(null);
		} else if (input.matches("^start\\s+.+")) {
			// gameStartup(); // the loading screen // removed for slow loading while testing, will be much better as if it were booting an os
			Scanner scanner = new Scanner(input);
			scanner.findInLine("^start\\s+(.+)"); // "^start\\s+(\\S+)\\s+(.+)"

			Network network = new Network(0);
			Network.getNetworks().add(network);
			network.populate();
			PlayerController.setHomeNetwork(network);
			PlayerController.setCurrentNetwork(network);
			PlayerController.setCurrentServer(network.getServers().get(network.getServers().size()-1));
			PlayerController.setPlayerName(scanner.match().group(1));
			PlayerController.updateCurrentIp(); // current server is set in the network class if it is the home network

			scanner.close();
			GameGui.startGame();
		} else if (input.equals("settings")) {
			GuiController.settingsWindow();
		} else if (input.equals("nmap")) {
			// run nmap
		} else {
			GuiController.menuTerminal.setText(GuiController.menuTerminal.getText() + "\n'" + input
					+ "' is not a recognized command, try using the 'help' commad");
		}
	}

	void gameStartup() { // TODO this wont display
		try {
			GuiController.menuTerminal.setText(GuiController.menuTerminal.getText() + "\nLoading...");
			GuiController.menuTerminal.setText(GuiController.menuTerminal.getText() + "\nFinished");
			Thread.sleep(1500 + ((int) Math.random() * 3500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return;
	}
}
