package hakd.gui;

import hakd.Hakd;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiController extends Application {
	// just for simplicity and to make it easier to implement new game modes these are all public with no getters and setters

	private static String					ip;
	static AnchorPane				root				= new AnchorPane();

	// ------menu
	static AnchorPane				menuInterface		= new AnchorPane();

	// terminal
	public static TextArea			menuTerminal		= new TextArea("Hak'd\nBy Ryam Mirman");
	public static TextField			menuInput			= new TextField();

	// ------game
	public static AnchorPane		gameInterface		= new AnchorPane();

	// main screen
	public static TabPane			mainScreen			= new TabPane();
	public static Tab				worldTab				= new Tab("World");
	public static Tab				regionTab			= new Tab();
	public static Tab				networkTab				= new Tab();
	public static Tab				serverTab				= new Tab();
	public static ScrollPane		worldScroll			= new ScrollPane();
	public static ScrollPane		regionScroll			= new ScrollPane();
	public static ScrollPane		networkScroll			= new ScrollPane();
	public static ScrollPane		serverScroll			= new ScrollPane();
	public static AnchorPane		worldPane			= new AnchorPane();
	public static AnchorPane		regionPane			= new AnchorPane();
	public static AnchorPane		networkPane			= new AnchorPane();
	public static AnchorPane		serverPane			= new AnchorPane();
	public static ArrayList<ImageView>	regionImage			= new ArrayList<ImageView>();

	// info screen
	public static TabPane			infoScreenTab			= new TabPane();
	public static Tab				infoTab				= new Tab("Info");
	public static Tab				resourceTab			= new Tab("Resources");
	public static Tab				runningProgramsTab		= new Tab("Running Programs");
	public static Tab				debugTab				= new Tab("debug");
	public static ScrollPane		infoScroll			= new ScrollPane();
	public static ScrollPane		resourceScroll		= new ScrollPane();
	public static ScrollPane		runningProgramsScroll	= new ScrollPane();
	public static ScrollPane		debugScroll = new ScrollPane();
	public static GridPane		infoGrid			= new GridPane();
	public static GridPane		resourceGrid		= new GridPane();
	public static GridPane		runningProgramsGrid	= new GridPane();
	public static GridPane		debugGrid = new GridPane();


	// menu
	public static ToolBar			menuBar				= new ToolBar();							// add some more icons and stuff
	public static Label				time				= new Label("10:54 PM");					// add a setter/getter
	public static MenuButton		menuButton1			= new MenuButton("GuiController");
	public static MenuItem			menuItem1			= new MenuItem("settings");

	// split panes
	public static SplitPane			splitPane1			= new SplitPane();
	public static SplitPane			splitPane2			= new SplitPane();
	public static AnchorPane		splitAnchor1		= new AnchorPane();
	public static AnchorPane		splitAnchor2		= new AnchorPane();

	// terminal
	public static TextArea			terminalDisplay		= new TextArea("Loading . . .\nReady");
	public static TextField			terminalInput		= new TextField(ip);

	// networks
	public static ArrayList<Line>		connection			= new ArrayList<Line>();

	// settings menu
	private static AnchorPane		settings			= new AnchorPane();

	public static void run(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage gameStage) throws Exception {
		gameStage.setTitle("Hak'd");
		gameStage.setScene(new Scene(root, /* 500 */1024, /* 400 */768));
		root.getStylesheets().add(GuiController.class.getResource("UI design.css").toExternalForm()); // maybe move this to gamegui to allow for different looks to the players OS
		gameStage.show();
		MenuGui.startMenu();

		gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				Hakd.quitGame(null);
			}
		});
	}

	public static void settingsWindow() {
		root.getChildren().add(settings);

		AnchorPane.setBottomAnchor(settings, 10.0);
		AnchorPane.setLeftAnchor(settings, 50.0);
		AnchorPane.setRightAnchor(settings, 50.0);
		AnchorPane.setTopAnchor(settings, 30.0);

		Rectangle settings1 = new Rectangle();
		settings1.setWidth(settings.getWidth());
		settings1.setHeight(settings.getHeight());
		settings.getChildren().add(settings1);
		settings1.setId("settings");

		AnchorPane.setBottomAnchor(settings1, 0.0);
		AnchorPane.setLeftAnchor(settings1, 0.0);
		AnchorPane.setRightAnchor(settings1, 0.0);
		AnchorPane.setTopAnchor(settings1, 0.0);
	}

	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		GuiController.ip = ip;
	}
}
