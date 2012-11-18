package hakd.userinterface;

import hakd.gameplay.Commands;
import hakd.network.Network;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class CTFGame {

	public static void startGame() {
		Controller.root.getChildren().remove(Controller.menuInterface);
		Controller.menuInterface = null;
		Controller.root.getChildren().add(Controller.gameInterface);

		AnchorPane.setBottomAnchor(Controller.gameInterface, 0.0);
		AnchorPane.setLeftAnchor(Controller.gameInterface, 0.0);
		AnchorPane.setRightAnchor(Controller.gameInterface, 0.0);
		AnchorPane.setTopAnchor(Controller.gameInterface, 0.0);

		// menu
		Controller.gameInterface.getChildren().add(Controller.menuBar);
		Controller.menuBar.getItems().addAll(Controller.time, new Separator(), Controller.menuButton1);
		Controller.menuButton1.getItems().add(Controller.menuItem1);
		Controller.menuBar.setMaxHeight(28);
		Controller.menuBar.setId("menu-bar");
		Controller.time.getStyleClass().add("menu");
		Controller.menuButton1.getStyleClass().add("menu");

		AnchorPane.setTopAnchor(Controller.menuBar, -4.0);
		AnchorPane.setLeftAnchor(Controller.menuBar, 0.0);
		AnchorPane.setRightAnchor(Controller.menuBar, 0.0);

		// split panes
		Controller.gameInterface.getChildren().add(Controller.splitPane1);
		Controller.splitPane1.getItems().addAll(Controller.splitAnchor1, Controller.splitAnchor2);
		Controller.splitPane1.setOrientation(Orientation.VERTICAL);
		Controller.splitPane1.setDividerPositions(0.78);

		AnchorPane.setBottomAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setLeftAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setRightAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setTopAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setBottomAnchor(Controller.splitPane1, 0.0);
		AnchorPane.setLeftAnchor(Controller.splitPane1, 0.0);
		AnchorPane.setRightAnchor(Controller.splitPane1, 0.0);
		AnchorPane.setTopAnchor(Controller.splitPane1, 27.0);

		// terminal
		Controller.splitAnchor2.getChildren().addAll(Controller.terminalDisplay, Controller.terminalInput);
		Controller.terminalDisplay.setEditable(false);
		Controller.terminalDisplay.setMinHeight(25.0);
		Controller.terminalDisplay.getStyleClass().add("terminal-text");
		Controller.terminalInput.getStyleClass().add("terminal-text");
		Controller.terminalInput.setId("terminal-input");
		Controller.terminalInput.setFocusTraversable(true);
		Controller.terminalInput.requestFocus();
		Controller.terminalInput.setText(Controller.ip);

		AnchorPane.setBottomAnchor(Controller.terminalDisplay, 22.0);
		AnchorPane.setLeftAnchor(Controller.terminalDisplay, 0.0);
		AnchorPane.setRightAnchor(Controller.terminalDisplay, 0.0);
		AnchorPane.setTopAnchor(Controller.terminalDisplay, 0.0);
		AnchorPane.setBottomAnchor(Controller.terminalInput, 0.0);
		AnchorPane.setLeftAnchor(Controller.terminalInput, 0.0);
		AnchorPane.setRightAnchor(Controller.terminalInput, 0.0);

		// main screen
		//Network.getNetwork().add(new Network(0))
		
		// Controller.splitAnchor2.
		
		

		// event handlers
		Controller.terminalInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent textInput) {
				try{
					if(textInput.getText().charAt(0) == 13){
						Controller.terminalDisplay.setText(Controller.terminalDisplay.getText() + "\n" + Controller.terminalInput.getText());
						System.out.println(Controller.terminalInput.getText());
						Commands.normalCommands(Controller.terminalInput.getText());
						Controller.terminalInput.setText(Controller.ip);
						Controller.terminalDisplay.end();
						Controller.terminalInput.end();
					}
				}

				catch (Exception ex) {
				}
			}
		});
	}

}
