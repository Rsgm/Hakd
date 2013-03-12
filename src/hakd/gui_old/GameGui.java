package hakd.gui_old;

import hakd.Hakd;
import hakd.gameplay.Commands;

import java.util.ArrayList;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class GameGui {
	private static int					region		= -1;						// -1 == not visible
	private static String				regionName;
	private static int					network		= -1;						// TODO these
	private static String				networkName;
	private static int					server		= -1;
	private static String				serverName;

	// network
	private static ArrayList<Circle>	circles		= new ArrayList<Circle>();	// tab ArrayList of circle ArrayLists?
// tab.getchildren.addall(tabs.get(1))
	private static ArrayList<Polygon>	polygons	= new ArrayList<Polygon>();
	private static Vector<Line>			lines		= new Vector<Line>();
	private static final int			radius		= 35;

	// --------methods--------
	static void startGame() {
		GuiController.root.getChildren().remove(GuiController.menuInterface);
		GuiController.menuInterface = null;
		GuiController.root.getChildren().add(GuiController.gameInterface);
		GuiController.gameInterface.setStyle("pane-background");

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
		GuiController.splitAnchor1.getChildren().add(GuiController.splitPane2);
		GuiController.splitPane2.getItems().addAll(GuiController.infoScreenTab, GuiController.mainScreen);
		GuiController.splitPane2.setOrientation(Orientation.HORIZONTAL);
		GuiController.splitPane1.setDividerPositions(0.78);
		GuiController.splitPane2.setDividerPositions(.23);

		AnchorPane.setBottomAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setLeftAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setRightAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setTopAnchor(GuiController.splitAnchor1, 0.0);
		AnchorPane.setBottomAnchor(GuiController.splitAnchor2, 0.0);
		AnchorPane.setLeftAnchor(GuiController.splitAnchor2, 0.0);
		AnchorPane.setRightAnchor(GuiController.splitAnchor2, 0.0);
		AnchorPane.setTopAnchor(GuiController.splitAnchor2, 0.0);
		AnchorPane.setBottomAnchor(GuiController.splitPane1, 0.0);
		AnchorPane.setLeftAnchor(GuiController.splitPane1, 0.0);
		AnchorPane.setRightAnchor(GuiController.splitPane1, 0.0);
		AnchorPane.setTopAnchor(GuiController.splitPane1, 27.0);
		AnchorPane.setBottomAnchor(GuiController.splitPane2, 0.0);
		AnchorPane.setLeftAnchor(GuiController.splitPane2, 0.0);
		AnchorPane.setRightAnchor(GuiController.splitPane2, 0.0);
		AnchorPane.setTopAnchor(GuiController.splitPane2, 0.0);

		// terminal
		GuiController.splitAnchor2.getChildren().addAll(GuiController.terminalDisplay, GuiController.terminalInput);
		GuiController.terminalDisplay.setEditable(false);
		GuiController.terminalDisplay.setMinHeight(25.0);
		GuiController.terminalDisplay.getStyleClass().add("terminal-text");
		GuiController.terminalInput.getStyleClass().add("terminal-text");
		GuiController.terminalInput.setId("terminal-input");
		GuiController.terminalInput.setFocusTraversable(true);
		GuiController.terminalInput.setText(GuiController.getIp());
		GuiController.terminalInput.requestFocus();

		AnchorPane.setBottomAnchor(GuiController.terminalDisplay, 20.0);
		AnchorPane.setLeftAnchor(GuiController.terminalDisplay, 0.0);
		AnchorPane.setRightAnchor(GuiController.terminalDisplay, 0.0);
		AnchorPane.setTopAnchor(GuiController.terminalDisplay, 0.0);
		AnchorPane.setBottomAnchor(GuiController.terminalInput, 0.0);
		AnchorPane.setLeftAnchor(GuiController.terminalInput, 0.0);
		AnchorPane.setRightAnchor(GuiController.terminalInput, 0.0);

		// main screen
		GuiController.mainScreen.getTabs().add(GuiController.worldTab);
		GuiController.mainScreen.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		GuiController.worldTab.setClosable(false);
		GuiController.worldTab.setContent(GuiController.worldScroll);
		GuiController.regionTab.setContent(GuiController.regionScroll);
		GuiController.regionScroll.setContent(GuiController.regionPane);
		GuiController.networkTab.setContent(GuiController.networkScroll);
		GuiController.networkScroll.setContent(GuiController.networkPane);
		GuiController.serverTab.setContent(GuiController.serverScroll);
		GuiController.serverScroll.setContent(GuiController.serverPane);

		GuiController.regionImage.add(new ImageView(new Image("/hakd/gui/icons/region0.png")));
		GuiController.regionImage.add(new ImageView(new Image("/hakd/gui/icons/region1.png")));
		GuiController.regionImage.add(new ImageView(new Image("/hakd/gui/icons/region2.png")));
		GuiController.regionImage.add(new ImageView(new Image("/hakd/gui/icons/region3.png")));
		GuiController.regionImage.add(new ImageView(new Image("/hakd/gui/icons/region4.png")));

		GuiController.regionImage.get(0).setLayoutX(15);
		GuiController.regionImage.get(0).setLayoutY(210);
		GuiController.regionImage.get(1).setLayoutX(350);
		GuiController.regionImage.get(1).setLayoutY(30);
		GuiController.regionImage.get(2).setLayoutX(600);
		GuiController.regionImage.get(2).setLayoutY(350);
		GuiController.regionImage.get(3).setLayoutX(10);
		GuiController.regionImage.get(3).setLayoutY(10);
		GuiController.regionImage.get(4).setLayoutX(250);
		GuiController.regionImage.get(4).setLayoutY(370);

		GuiController.regionImage.get(0).setVisible(true);
		for (int i = 1; i < GuiController.regionImage.size(); i++) { // skip the first image
			GuiController.regionImage.get(i).setVisible(false);
		}

		GuiController.worldScroll.setContent(GuiController.worldPane);
		GuiController.worldPane.getChildren().addAll(GuiController.regionImage);

		// info screen
		GuiController.infoScreenTab.getTabs().addAll(GuiController.infoTab, GuiController.resourceTab, GuiController.runningProgramsTab,
				GuiController.debugTab);
		GuiController.infoScreenTab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		GuiController.infoTab.setContent(GuiController.infoScroll);
		GuiController.resourceTab.setContent(GuiController.resourceScroll);
		GuiController.runningProgramsTab.setContent(GuiController.runningProgramsScroll);
		GuiController.debugTab.setContent(GuiController.debugScroll); // temporary
		GuiController.infoScroll.setContent(GuiController.infoGrid);
		GuiController.resourceScroll.setContent(GuiController.resourceGrid);
		GuiController.runningProgramsScroll.setContent(GuiController.runningProgramsGrid);
		GuiController.debugScroll.setContent(GuiController.debugGrid); // temporary
		// temporary debug settings
		GuiController.debugGrid.setHgap(10);
		GuiController.debugGrid.setVgap(5);

// event handlers
		GuiController.terminalInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent input) {
				Commands.inputHandler(input);
			}
		});

		GuiController.terminalDisplay.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				GuiController.terminalInput.requestFocus();
				GuiController.terminalInput.end();
			}
		});

		GuiController.regionImage.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				region = 0;
				changeRegionTab("North America");
			}
		});

		GuiController.regionImage.get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				region = 1;
				changeRegionTab("Europe");
			}
		});

		GuiController.regionImage.get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				region = 2;
				changeRegionTab("Asia");
			}
		});

		GuiController.regionImage.get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				region = 3;
				changeRegionTab("Companies");
			}
		});

		GuiController.regionImage.get(4).setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				region = 4;
				changeRegionTab("The Dark Net");
			}
		});

		GuiController.menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO open settings window
			}
		});

// game code
		// new RunGame(); // all this is is a thread thaat does nothing right now, the game is being run by javafx

		Hakd.running = true;
		new UpdateThread();
	}

// gui updates
	private static void changeRegionTab(String name) { // updates the tab(opens it, closes it, renames it), only accessible by the tab handlers
		regionName = name;
		if (region == -1) {
			GuiController.mainScreen.getTabs().remove(GuiController.regionTab);
		} else {
			if (GuiController.regionTab.getTabPane() != GuiController.mainScreen) {
				GuiController.mainScreen.getTabs().add(1, GuiController.regionTab);
			}
			GuiController.mainScreen.getSelectionModel().select(GuiController.regionTab);
			GuiController.regionTab.setContent(GuiController.regionScroll);
			GuiController.regionTab.setText(regionName);
		}
	}

	public static void updateNetworkTab() {

	}

	public static void updateServerTab() {

	}

	public static void updateRegion() { // updates the nodes in the tab by removing all then adding all
		GuiController.regionPane.getChildren().clear();
		GuiController.regionPane.getChildren().addAll(circles);
		GuiController.regionPane.getChildren().addAll(polygons);
		GuiController.regionPane.getChildren().addAll(lines);
	}

	public static void updateServer() {

	}

	public static void updateNetwork() {

	}

	// --------getters/setters--------
	public static ArrayList<Circle> getCircles() {
		return circles;
	}

	public static void setCircles(ArrayList<Circle> circles) {
		GameGui.circles = circles;
	}

	public static ArrayList<Polygon> getPolygons() {
		return polygons;
	}

	public static void setPolygons(ArrayList<Polygon> polygons) {
		GameGui.polygons = polygons;
	}

	public static Vector<Line> getLines() {
		return lines;
	}

	public static void setLines(Vector<Line> lines) {
		GameGui.lines = lines;
	}

	public static int getRadius() {
		return radius;
	}

}