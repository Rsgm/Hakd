package hakd.userinterface;

import hakd.gameplay.Commands;
import hakd.network.Network;
import hakd.userinterface.threads.RunGame;
import hakd.userinterface.threads.RunMenu;

import java.util.Vector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class NormalGame {
	private static int recall = 0;
	private static Vector<String> lines = new Vector<String>(0, 1);

	static void startGame() {
		Controller.root.getChildren().remove(Controller.menuInterface);
		Controller.menuInterface = null;
		RunMenu.setRunning(false);
		Controller.root.getChildren().add(Controller.gameInterface);
		Controller.gameInterface.setStyle("pane-background");

		AnchorPane.setBottomAnchor(Controller.gameInterface, 0.0);
		AnchorPane.setLeftAnchor(Controller.gameInterface, 0.0);
		AnchorPane.setRightAnchor(Controller.gameInterface, 0.0);
		AnchorPane.setTopAnchor(Controller.gameInterface, 0.0);

// menu
		Controller.gameInterface.getChildren().add(Controller.menuBar);
		Controller.menuBar.getItems().addAll(Controller.time, new Separator(),
				Controller.menuButton1);
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
		Controller.splitAnchor1.getChildren().add(Controller.splitPane2);
		Controller.splitPane2.getItems().addAll(Controller.infoScreen, Controller.mainScreen);
		Controller.splitPane2.setOrientation(Orientation.HORIZONTAL);
		Controller.splitPane1.setDividerPositions(0.78);
		Controller.splitPane2.setDividerPositions(.23);

		AnchorPane.setBottomAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setLeftAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setRightAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setTopAnchor(Controller.splitAnchor1, 0.0);
		AnchorPane.setBottomAnchor(Controller.splitAnchor2, 0.0);
		AnchorPane.setLeftAnchor(Controller.splitAnchor2, 0.0);
		AnchorPane.setRightAnchor(Controller.splitAnchor2, 0.0);
		AnchorPane.setTopAnchor(Controller.splitAnchor2, 0.0);
		AnchorPane.setBottomAnchor(Controller.splitPane1, 0.0);
		AnchorPane.setLeftAnchor(Controller.splitPane1, 0.0);
		AnchorPane.setRightAnchor(Controller.splitPane1, 0.0);
		AnchorPane.setTopAnchor(Controller.splitPane1, 27.0);
		AnchorPane.setBottomAnchor(Controller.splitPane2, 0.0);
		AnchorPane.setLeftAnchor(Controller.splitPane2, 0.0);
		AnchorPane.setRightAnchor(Controller.splitPane2, 0.0);
		AnchorPane.setTopAnchor(Controller.splitPane2, 0.0);

// terminal
		Controller.splitAnchor2.getChildren().addAll(Controller.terminalDisplay,
				Controller.terminalInput);
		Controller.terminalDisplay.setEditable(false);
		Controller.terminalDisplay.setMinHeight(25.0);
		Controller.terminalDisplay.getStyleClass().add("terminal-text");
		Controller.terminalInput.getStyleClass().add("terminal-text");
		Controller.terminalInput.setId("terminal-input");
		Controller.terminalInput.setFocusTraversable(true);
		Controller.terminalInput.setText(Controller.ip);
		Controller.terminalInput.requestFocus();

		AnchorPane.setBottomAnchor(Controller.terminalDisplay, 20.0);
		AnchorPane.setLeftAnchor(Controller.terminalDisplay, 0.0);
		AnchorPane.setRightAnchor(Controller.terminalDisplay, 0.0);
		AnchorPane.setTopAnchor(Controller.terminalDisplay, 0.0);
		AnchorPane.setBottomAnchor(Controller.terminalInput, 0.0);
		AnchorPane.setLeftAnchor(Controller.terminalInput, 0.0);
		AnchorPane.setRightAnchor(Controller.terminalInput, 0.0);

// main screen
		Controller.mainScreen.getTabs().add(Controller.world);
		Controller.mainScreen.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		Controller.world.setClosable(false);
		Controller.world.setContent(Controller.worldPane);

		for (int i = 0; i < 5; i++) {
			Controller.regionView.add(new AnchorPane());
		}

		Controller.regionImage.add(new ImageView(new Image(
				"/com/github/rmirman/hakd/userinterface/icons/region0.png")));
		Controller.regionImage.add(new ImageView(new Image(
				"/com/github/rmirman/hakd/userinterface/icons/region1.png")));
		Controller.regionImage.add(new ImageView(new Image(
				"/com/github/rmirman/hakd/userinterface/icons/region2.png")));
		Controller.regionImage.add(new ImageView(new Image(
				"/com/github/rmirman/hakd/userinterface/icons/region3.png")));
		Controller.regionImage.add(new ImageView(new Image(
				"/com/github/rmirman/hakd/userinterface/icons/region4.png")));

		Controller.regionImage.get(0).setLayoutX(15);
		Controller.regionImage.get(0).setLayoutY(210);
		Controller.regionImage.get(1).setLayoutX(350);
		Controller.regionImage.get(1).setLayoutY(30);
		Controller.regionImage.get(2).setLayoutX(600);
		Controller.regionImage.get(2).setLayoutY(350);
		Controller.regionImage.get(3).setLayoutX(10);
		Controller.regionImage.get(3).setLayoutY(10);
		Controller.regionImage.get(4).setLayoutX(250);
		Controller.regionImage.get(4).setLayoutY(370);

		Controller.regionImage.get(0).setVisible(true);
		for (int i = 1; i < Controller.regionImage.size(); i++) {
			Controller.regionImage.get(i).setVisible(false);
		}

		Controller.worldPane.setContent(Controller.worldView);
		Controller.worldView.getChildren().addAll(Controller.regionImage);

// info screen
		Controller.infoScreen.getTabs().addAll(Controller.info, Controller.resources,
				Controller.runningPrograms);
		Controller.infoScreen.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		Controller.info.setContent(Controller.infoPane);
		Controller.resources.setContent(Controller.resourcesPane);
		Controller.runningPrograms.setContent(Controller.runningProgramsPane);

// event handlers
		Controller.terminalInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent input) {
				Commands.inputHandler(input);
			}
		});

		Controller.terminalDisplay.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				Controller.terminalInput.requestFocus();
				Controller.terminalInput.end();
			}
		});

		Controller.regionImage.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (Controller.regionTab.getTabPane() != Controller.mainScreen) {
					Controller.mainScreen.getTabs().add(1, Controller.regionTab);
				}
				Controller.regionTab.setContent(Controller.regionPane);
				Controller.regionPane.setContent(Controller.regionView.get(0));
				Controller.mainScreen.getSelectionModel().select(Controller.regionTab);
				Controller.regionTab.setText("North America");
			}
		});

		Controller.regionImage.get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (Controller.regionTab.getTabPane() != Controller.mainScreen) {
					Controller.mainScreen.getTabs().add(1, Controller.regionTab);
				}
				Controller.regionTab.setContent(Controller.regionPane);
				Controller.regionPane.setContent(Controller.regionView.get(1));
				Controller.mainScreen.getSelectionModel().select(Controller.regionTab);
				Controller.regionTab.setText("Europe");
			}
		});

		Controller.regionImage.get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (Controller.regionTab.getTabPane() != Controller.mainScreen) {
					Controller.mainScreen.getTabs().add(1, Controller.regionTab);
				}
				Controller.regionTab.setContent(Controller.regionPane);
				Controller.regionPane.setContent(Controller.regionView.get(2));
				Controller.mainScreen.getSelectionModel().select(Controller.regionTab);
				Controller.regionTab.setText("Asia");
			}
		});

		Controller.regionImage.get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (Controller.regionTab.getTabPane() != Controller.mainScreen) {
					Controller.mainScreen.getTabs().add(1, Controller.regionTab);
				}
				Controller.regionTab.setContent(Controller.regionPane);
				Controller.regionPane.setContent(Controller.regionView.get(3));
				Controller.mainScreen.getSelectionModel().select(Controller.regionTab);
				Controller.regionTab.setText("Companies");
			}
		});

		Controller.regionImage.get(4).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (Controller.regionTab.getTabPane() != Controller.mainScreen) {
					Controller.mainScreen.getTabs().add(1, Controller.regionTab);
				}
				Controller.regionTab.setContent(Controller.regionPane);
				Controller.regionPane.setContent(Controller.regionView.get(4));
				Controller.mainScreen.getSelectionModel().select(Controller.regionTab);
				Controller.regionTab.setText("The Dark Net");
			}
		});

		Controller.menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO open settings window
			}
		});

// game
		Network.getNetworks().get(Network.getNetworks().size() - 1).populate();

		new RunGame();
	}
}
