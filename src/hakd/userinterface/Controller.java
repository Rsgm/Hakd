package hakd.userinterface;

import hakd.Hakd;

import java.util.Vector;

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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Controller extends Application {

	static String ip;
	static AnchorPane root = new AnchorPane();

	//------menu
	static AnchorPane menuInterface = new AnchorPane();

// terminal
	public static TextArea menuTerminal = new TextArea("Hak'd\nBy Ryam Mirman");
	public static TextField menuInput = new TextField();

	//------game
	public static AnchorPane gameInterface = new AnchorPane();

// main screen
	public static TabPane mainScreen = new TabPane();
	public static Tab world = new Tab("World");
	public static Tab regionTab = new Tab("Region");
	public static Tab network = new Tab("Network");
	public static Tab computer = new Tab("Computer");
	public static ScrollPane worldPane = new ScrollPane();
	public static AnchorPane worldView = new AnchorPane();
	public static ScrollPane regionPane = new ScrollPane();
	public static Vector<AnchorPane> regionView = new Vector<AnchorPane>(1, 1);
	public static ScrollPane networkPane = new ScrollPane();
	public static AnchorPane networkView = new AnchorPane();
	public static ScrollPane computerPane = new ScrollPane();
	public static AnchorPane computerView = new AnchorPane();
	public static Vector<ImageView> regionImage = new Vector<ImageView>();

// info screen
	public static TabPane infoScreen = new TabPane();
	public static Tab info = new Tab("Info");
	public static Tab resources = new Tab("Resources");
	public static Tab runningPrograms = new Tab("Running Programs");
	public static AnchorPane infoPane = new AnchorPane();
	public static AnchorPane resourcesPane = new AnchorPane();
	public static AnchorPane runningProgramsPane = new AnchorPane();

// menu
	public static ToolBar menuBar = new ToolBar(); // add some more icons and stuff
	public static Label time = new Label("10:54 PM"); //add a setter/getter
	public static MenuButton menuButton1 = new MenuButton("Controller");
	public static MenuItem menuItem1 = new MenuItem("settings");

// split panes
	public static SplitPane splitPane1 = new SplitPane();
	public static SplitPane splitPane2 = new SplitPane();
	public static AnchorPane splitAnchor1 = new AnchorPane();
	public static AnchorPane splitAnchor2 = new AnchorPane();

// terminal
	public static TextArea terminalDisplay = new TextArea("Loading . . .\nReady");
	public static TextField terminalInput = new TextField(ip);

// networks
	public static Vector<Line> connection = new Vector<Line>(1, 1);

// settings menu
	static AnchorPane settings = new AnchorPane();

	public static void run(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage gameStage) throws Exception {
		gameStage.setTitle("Hak'd");
		gameStage.setScene(new Scene(root, /* 500 */1024, /* 400 */768));
		root.getStylesheets().add(Controller.class.getResource("UI design.css").toExternalForm());
		gameStage.show();
		Menu.startMenu();

		gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				Hakd.quitGame(null);
			}
		});
	}

	static void settingsWindow() {
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

	public synchronized static String getIp() {
		return ip;
	}

	public synchronized static void setIp(String ip) {
		Controller.ip = ip;
	}

	public static void setTime(String time) {
		Controller.time.setText(time);
	}
}
