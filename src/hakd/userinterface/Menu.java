package hakd.userinterface;

import hakd.Hakd;
import hakd.gameplay.PlayerController;
import hakd.network.Network;
import hakd.userinterface.threads.RunMenu;

import java.util.Scanner;
import java.util.Vector;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class Menu {

	private static int recall = 0;
	private static Vector<String> lines = new Vector<String>(1, 1);

	public static void startMenu() {
		Controller.root.getChildren().add(Controller.menuInterface);
		Controller.menuInterface.setStyle("pane-background");

		AnchorPane.setBottomAnchor(Controller.menuInterface, 0.0);
		AnchorPane.setLeftAnchor(Controller.menuInterface, 0.0);
		AnchorPane.setRightAnchor(Controller.menuInterface, 0.0);
		AnchorPane.setTopAnchor(Controller.menuInterface, 0.0);

//menu terminal
		Controller.menuInterface.getChildren()
				.addAll(Controller.menuTerminal, Controller.menuInput);
		Controller.menuTerminal.setEditable(false);
		Controller.menuInput.setEditable(true);
		Controller.menuTerminal.setMinHeight(25.0);
		Controller.menuTerminal.getStyleClass().add("terminal-text");
		Controller.menuInput.getStyleClass().add("terminal-text");
		Controller.menuInput.setId("terminal-input");
		Controller.menuInput.requestFocus();

		AnchorPane.setBottomAnchor(Controller.menuTerminal, 20.0);
		AnchorPane.setLeftAnchor(Controller.menuTerminal, 0.0);
		AnchorPane.setRightAnchor(Controller.menuTerminal, 0.0);
		AnchorPane.setTopAnchor(Controller.menuTerminal, 0.0);
		AnchorPane.setBottomAnchor(Controller.menuInput, 0.0);
		AnchorPane.setLeftAnchor(Controller.menuInput, 0.0);
		AnchorPane.setRightAnchor(Controller.menuInput, 0.0);

// event handlers
		Controller.menuInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent input) {
				if (input.getCode() == KeyCode.ENTER) {
					lines.add(Controller.menuInput.getText());
					Controller.menuTerminal
							.setText(Controller.menuTerminal.getText() + "\n" + Controller.menuInput
									.getText());
					menuCommands(Controller.menuInput.getText());
					Controller.menuInput.setText("");
					Controller.menuTerminal.end();
					Controller.menuInput.end();
					recall = lines.size();
				} else if (input.getCode() == KeyCode.DOWN && recall < lines.size() - 1) {
					recall++;
					Controller.menuInput.setText(lines.get(recall));
					Controller.menuInput.end();
				} else if (input.getCode() == KeyCode.UP && recall > 0) {
					recall--;
					Controller.menuInput.setText(lines.get(recall));
					Controller.menuInput.end();
				} else if (input.getCode() == KeyCode.ESCAPE) {
					Controller.menuInput.setText("");
					recall = lines.size();
				}
			}
		});
		Controller.menuTerminal.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				Controller.menuInput.requestFocus();
				Controller.menuInput.end();
			}
		});

//game
		new RunMenu();
	}

	static void menuCommands(String input) {
		System.out.println(input);

		if (input.equals("help")) { //TODO add more stuff to do outside of the game
			Controller.menuTerminal
					.setText(Controller.menuTerminal.getText() + "\nCommands:\nhelp - shows this list\nquit - exits this game" + "\nstart [player name] - starts the game" + "\nnmap - scans the computers on the network for open ports and other stuff" + "\nsettings - opens settings window");
		} else if (input.equals("quit")) {
			Hakd.quitGame(null);
		} else if (input.matches("^start\\s+.+")) {
			//gameStartup(); // the loading screen

			Scanner scanner = new Scanner(input);
			scanner.findInLine("^start\\s+(.+)"); // "^start\\s+(\\S+)\\s+(.+)"

			Network.getNetworks().add(new Network(0));
			PlayerController.setPlayerName(scanner.match().group(1));
			scanner.close();
			NormalGame.startGame();
			/* else if (scanner.match().group(1).equals("ctf")) { CTFGame.startGame();
			 * scanner.close(); } */

		} else if (input.equals("settings")) {
			Controller.settingsWindow();
		} else if (input.equals("nmap")) {
			//run nmap
		} else if (input.equals("")) {
			Controller.menuTerminal.setText(Controller.menuTerminal.getText() + "\n" + input);
		} else {
			Controller.menuTerminal
					.setText(Controller.menuTerminal.getText() + "\n'" + input + "' is not a recognized command, try using the 'help' command");
		}
	}

	void gameStartup() { // TODO this wont display
		try {
			Controller.menuTerminal.setText(Controller.menuTerminal.getText() + "\nLoading...");
			Controller.menuTerminal.setText(Controller.menuTerminal.getText() + "\nFinished");
			Thread.sleep(1500 + ((int) Math.random() * 3500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return;
	}
}
