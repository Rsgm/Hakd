package hakd.gui;

import hakd.gameplay.Commands;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class CTFGame {

	public static void startGame() {
		GuiController.root.getChildren().remove(GuiController.menuInterface);
		GuiController.menuInterface = null;
		GuiController.root.getChildren().add(GuiController.gameInterface);

		AnchorPane.setBottomAnchor(GuiController.gameInterface, 0.0);
		AnchorPane.setLeftAnchor(GuiController.gameInterface, 0.0);
		AnchorPane.setRightAnchor(GuiController.gameInterface, 0.0);
		AnchorPane.setTopAnchor(GuiController.gameInterface, 0.0);

		// menu
		GuiController.gameInterface.getChildren().add(GuiController.menuBar);
		GuiController.menuBar.getItems().addAll(GuiController.time, new Separator(), GuiController.menuButton1);
		GuiController.menuButton1.getItems().add(GuiController.menuItem1);
		GuiController.menuBar.setMaxHeight(28);
		GuiController.menuBar.setId("menu-bar");
		GuiController.time.getStyleClass().add("menu");
		GuiController.menuButton1.getStyleClass().add("menu");

		AnchorPane.setTopAnchor(GuiController.menuBar, -4.0);
		AnchorPane.setLeftAnchor(GuiController.menuBar, 0.0);
		AnchorPane.setRightAnchor(GuiController.menuBar, 0.0);

		// split panes
		GuiController.gameInterface.getChildren().add(GuiController.splitPane1);
		GuiController.splitPane1.getItems().addAll(GuiController.splitAnchor1, GuiController.splitAnchor2);
		GuiController.splitPane1.setOrientation(Orientation.VERTICAL);
		GuiController.splitPane1.setDividerPositions(0.78);

		AnchorPane.setBottomAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setLeftAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setRightAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setTopAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setBottomAnchor(GuiController.splitPane1, 0.0);
		AnchorPane.setLeftAnchor(GuiController.splitPane1, 0.0);
		AnchorPane.setRightAnchor(GuiController.splitPane1, 0.0);
		AnchorPane.setTopAnchor(GuiController.splitPane1, 27.0);

		// terminal
		GuiController.splitAnchor2.getChildren().addAll(GuiController.terminalDisplay, GuiController.terminalInput);
		GuiController.terminalDisplay.setEditable(false);
		GuiController.terminalDisplay.setMinHeight(25.0);
		GuiController.terminalDisplay.getStyleClass().add("terminal-text");
		GuiController.terminalInput.getStyleClass().add("terminal-text");
		GuiController.terminalInput.setId("terminal-input");
		GuiController.terminalInput.setFocusTraversable(true);
		GuiController.terminalInput.requestFocus();
		GuiController.terminalInput.setText(GuiController.getIp());

		AnchorPane.setBottomAnchor(GuiController.terminalDisplay, 22.0);
		AnchorPane.setLeftAnchor(GuiController.terminalDisplay, 0.0);
		AnchorPane.setRightAnchor(GuiController.terminalDisplay, 0.0);
		AnchorPane.setTopAnchor(GuiController.terminalDisplay, 0.0);
		AnchorPane.setBottomAnchor(GuiController.terminalInput, 0.0);
		AnchorPane.setLeftAnchor(GuiController.terminalInput, 0.0);
		AnchorPane.setRightAnchor(GuiController.terminalInput, 0.0);

		// main screen
		// Network.getNetwork().add(new Network(0))

		// GuiController.splitAnchor2.

		// event handlers
		GuiController.terminalInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent textInput) {
				try {
					if (textInput.getText().charAt(0) == 13) {
						GuiController.terminalDisplay.setText(GuiController.terminalDisplay.getText() + "\n" + GuiController.terminalInput.getText());
						System.out.println(GuiController.terminalInput.getText());
						Commands.ctfCommands(GuiController.terminalInput.getText());
						GuiController.terminalInput.setText(GuiController.getIp());
						GuiController.terminalDisplay.end();
						GuiController.terminalInput.end();
					}
				}

				catch (Exception ex) {
				}
			}
		});
	}

}
