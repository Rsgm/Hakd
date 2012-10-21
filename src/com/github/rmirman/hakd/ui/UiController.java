package com.github.rmirman.hakd.ui;

import java.util.Scanner;
import java.util.Vector;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.github.rmirman.hakd.RunGame;
import com.github.rmirman.hakd.StartGame;
import com.github.rmirman.hakd.gameplay.Commands;
import com.github.rmirman.hakd.gameplay.PlayerController;
import com.github.rmirman.hakd.network.Network;

public class UiController extends Application{

	private static String ip;
	private static AnchorPane root = new AnchorPane();

	//------menu user interface
	private static AnchorPane menuInterface = new AnchorPane();

	// terminal
	public static TextField menuInput = new TextField();
	public static TextArea menuDisplay = new TextArea("Hak'd\nBy Ryam Mirman");

	//------game user interface
	public static AnchorPane gameInterface = new AnchorPane();

	// main screen
	public static TabPane mainScreen = new TabPane();
	public static Tab world = new Tab("World");
	public static Tab regionTab = new Tab("Region");
	public static Tab network = new Tab("Network");
	public static Tab computer = new Tab("Computer");
	public static AnchorPane worldView = new AnchorPane();
	public static Vector<AnchorPane> regionView = new Vector<AnchorPane>(1, 1);
	public static AnchorPane networkView = new AnchorPane();
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
	public static Label time = new Label("10:54 PM");
	public static MenuButton menuButton1 = new MenuButton("Menu");
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
	private AnchorPane settings = new AnchorPane();


	public static void run(String[] args){
		launch(args);
	}

	public void start(Stage gameStage) throws Exception { // start menu
		gameStage.setTitle("Hak'd");
		gameStage.setScene(new Scene(root, /*500*/1024, /*400*/768));
		root.getStylesheets().add(UiController.class.getResource("UI design.css").toExternalForm());
		root.getChildren().add(menuInterface);

		AnchorPane.setBottomAnchor(menuInterface, 0.0);
		AnchorPane.setLeftAnchor(menuInterface, 0.0);
		AnchorPane.setRightAnchor(menuInterface, 0.0);
		AnchorPane.setTopAnchor(menuInterface, 0.0);

		// menu terminal
		menuInterface.getChildren().addAll(menuDisplay, menuInput);
		menuDisplay.setEditable(false);
		menuInput.setEditable(true);
		menuDisplay.setMinHeight(25.0);
		menuDisplay.getStyleClass().add("terminal-text");
		menuInput.getStyleClass().add("terminal-text");
		menuInput.setId("terminal-input");
		menuInput.setFocusTraversable(true);
		menuInput.requestFocus();

		AnchorPane.setBottomAnchor(menuDisplay, 22.0);
		AnchorPane.setLeftAnchor(menuDisplay, 0.0);
		AnchorPane.setRightAnchor(menuDisplay, 0.0);
		AnchorPane.setTopAnchor(menuDisplay, 0.0);
		AnchorPane.setBottomAnchor(menuInput, 0.0);
		AnchorPane.setLeftAnchor(menuInput, 0.0);
		AnchorPane.setRightAnchor(menuInput, 0.0);

		gameStage.show();

		// event handlers
		menuInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent textInput) {
				try{
					if(textInput.getText().charAt(0) == 13){
						menuDisplay.setText(menuDisplay.getText() + "\n" + menuInput.getText());
						menuCommands(menuInput.getText());
						menuInput.setText("");
						menuDisplay.end();
						menuInput.end();
					}
				}

				catch (Exception ex) {
				}
			}
		});		

		gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent arg0) {
				StartGame.quitGame(null);
			}
		});

		//game
		new RunMenu();
	}

	private void startGame(){
		root.getChildren().remove(menuInterface);
		menuInterface = null;
		root.getChildren().add(gameInterface);

		AnchorPane.setBottomAnchor(gameInterface, 0.0);
		AnchorPane.setLeftAnchor(gameInterface, 0.0);
		AnchorPane.setRightAnchor(gameInterface, 0.0);
		AnchorPane.setTopAnchor(gameInterface, 0.0);

		// menu
		gameInterface.getChildren().add(menuBar);
		menuBar.getItems().addAll(time, new Separator(), menuButton1);
		menuButton1.getItems().add(menuItem1);
		menuBar.setMaxHeight(28);
		menuBar.setId("menu-bar");
		time.getStyleClass().add("menu");
		menuButton1.getStyleClass().add("menu");

		AnchorPane.setTopAnchor(menuBar, -4.0);
		AnchorPane.setLeftAnchor(menuBar, 0.0);
		AnchorPane.setRightAnchor(menuBar, 0.0);

		// split panes
		gameInterface.getChildren().add(splitPane1);
		splitPane1.getItems().addAll(splitAnchor1,splitAnchor2);
		splitPane1.setOrientation(Orientation.VERTICAL);
		splitAnchor1.getChildren().add(splitPane2);
		splitPane2.getItems().addAll(infoScreen, mainScreen);
		splitPane2.setOrientation(Orientation.HORIZONTAL);
		splitPane1.setDividerPositions(0.78);
		splitPane2.setDividerPositions(.23);

		AnchorPane.setBottomAnchor(splitAnchor1, 0.0);
		AnchorPane.setLeftAnchor(splitAnchor1, 0.0);
		AnchorPane.setRightAnchor(splitAnchor1, 0.0);
		AnchorPane.setTopAnchor(splitAnchor1, 0.0);
		AnchorPane.setBottomAnchor(splitAnchor2, 0.0);
		AnchorPane.setLeftAnchor(splitAnchor2, 0.0);
		AnchorPane.setRightAnchor(splitAnchor2, 0.0);
		AnchorPane.setTopAnchor(splitAnchor2, 0.0);
		AnchorPane.setBottomAnchor(splitPane1, 0.0);
		AnchorPane.setLeftAnchor(splitPane1, 0.0);
		AnchorPane.setRightAnchor(splitPane1, 0.0);
		AnchorPane.setTopAnchor(splitPane1, 27.0);
		AnchorPane.setBottomAnchor(splitPane2, 0.0);
		AnchorPane.setLeftAnchor(splitPane2, 0.0);
		AnchorPane.setRightAnchor(splitPane2, 0.0);
		AnchorPane.setTopAnchor(splitPane2, 0.0);

		// terminal
		splitAnchor2.getChildren().addAll(terminalDisplay, terminalInput);
		terminalDisplay.setEditable(false);
		terminalDisplay.setMinHeight(25.0);
		terminalDisplay.getStyleClass().add("terminal-text");
		terminalInput.getStyleClass().add("terminal-text");
		terminalInput.setId("terminal-input");
		terminalInput.setFocusTraversable(true);
		terminalInput.requestFocus();
		terminalInput.setText(ip);

		AnchorPane.setBottomAnchor(terminalDisplay, 22.0);
		AnchorPane.setLeftAnchor(terminalDisplay, 0.0);
		AnchorPane.setRightAnchor(terminalDisplay, 0.0);
		AnchorPane.setTopAnchor(terminalDisplay, 0.0);
		AnchorPane.setBottomAnchor(terminalInput, 0.0);
		AnchorPane.setLeftAnchor(terminalInput, 0.0);
		AnchorPane.setRightAnchor(terminalInput, 0.0);

		// main screen
		mainScreen.getTabs().add(world);
		mainScreen.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		world.setClosable(false);
		world.setContent(worldView);

		//regionView.add(new AnchorPane());
		//regionView.add(new AnchorPane());
		//regionView.add(new AnchorPane());
		//regionView.add(new AnchorPane());
		//regionView.add(new AnchorPane());
		for(int i=0; i<5; i++){
			regionView.add(new AnchorPane());
		}

		regionImage.add(new ImageView(new Image("/com/github/rmirman/hakd/ui/icons/region0.png")));
		regionImage.add(new ImageView(new Image("/com/github/rmirman/hakd/ui/icons/region1.png")));
		regionImage.add(new ImageView(new Image("/com/github/rmirman/hakd/ui/icons/region2.png")));
		regionImage.add(new ImageView(new Image("/com/github/rmirman/hakd/ui/icons/region3.png")));
		regionImage.add(new ImageView(new Image("/com/github/rmirman/hakd/ui/icons/region4.png")));
		
		regionImage.get(0).setLayoutX(15);
		regionImage.get(0).setLayoutY(210);
		regionImage.get(1).setLayoutX(350);
		regionImage.get(1).setLayoutY(30);
		regionImage.get(2).setLayoutX(600);
		regionImage.get(2).setLayoutY(350);
		regionImage.get(3).setLayoutX(10);
		regionImage.get(3).setLayoutY(10);
		regionImage.get(4).setLayoutX(250);
		regionImage.get(4).setLayoutY(370);

		worldView.getChildren().addAll(regionImage); // can either use the vector variable or individual indexes in the vector

		// info screen
		infoScreen.getTabs().addAll(info, resources, runningPrograms);
		infoScreen.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		info.setContent(infoPane);
		resources.setContent(resourcesPane);
		runningPrograms.setContent(runningProgramsPane);

		// event handlers
		terminalInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent textInput) {
				try{
					if(textInput.getText().charAt(0) == 13){
						terminalDisplay.setText(terminalDisplay.getText() + "\n" + terminalInput.getText());
						System.out.println(terminalInput.getText());
						Commands.command(terminalInput.getText());
						terminalInput.setText(ip);
						terminalDisplay.end();
						terminalInput.end();
					}else{
						//System.out.println(terminalInput.getText() + textInput.getText());
					}
				}

				catch (Exception ex) {
				}
			}
		});

		regionImage.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(regionTab.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, regionTab);
				}
				regionTab.setContent(regionView.get(0));
				mainScreen.getSelectionModel().select(regionTab);
				regionTab.setText("North America");
			}
		});
		regionImage.get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(regionTab.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, regionTab);
				}
				regionTab.setContent(regionView.get(1));
				mainScreen.getSelectionModel().select(regionTab);
				regionTab.setText("Europe");
			}
		});
		regionImage.get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(regionTab.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, regionTab);
				}
				regionTab.setContent(regionView.get(2));
				mainScreen.getSelectionModel().select(regionTab);
				regionTab.setText("Asia");
			}
		});
		regionImage.get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(regionTab.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, regionTab);
				}
				regionTab.setContent(regionView.get(3));
				mainScreen.getSelectionModel().select(regionTab);
				regionTab.setText("Companies");
			}
		});
		regionImage.get(4).setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(regionTab.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, regionTab);
				}
				regionTab.setContent(regionView.get(4));
				mainScreen.getSelectionModel().select(regionTab);
				regionTab.setText("The Dark Net");
			}
		});

		// game
		Network.getNetwork().get(Network.getNetwork().size()-1).populate();
		
		new RunGame();
	}

	private void menuCommands(String input) {
		if(input.equals("help")){ //TODO add more stuff to do outside of the game
			menuDisplay.setText(menuDisplay.getText() +
					"\nCommands:\nhelp - shows this list\nquit - exits this game\nstart [player name] - starts the game" +
					"\nnmap - scans the computers on the network for open ports and other stuff" +
					"\nsettings - opens settings window");
		}else if(input.equals("quit")){
			StartGame.quitGame(null);
		}else if(input.matches("^(start)\\s+(.+)")){
			//gameStartup(); // the loading screen

			Scanner scanner = new Scanner(input);
			scanner.findInLine("^(start)\\s+(.+)");
			
			Network.getNetwork().add(new Network(0));
			PlayerController.setPlayerName(scanner.match().group(2));
			
			scanner.close();
			startGame();
		}else if(input.equals("settings")){
			settingsWindow();
		}else if(input.equals("nmap")){
			//run nmap
		}else{
			menuDisplay.setText(menuDisplay.getText() + "\n'" + input + "' is not a recognized command, try using the 'help' command");
		}
	}

	private void settingsWindow(){
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

	private void gameStartup(){ // TODO this wont display
		try{
			menuDisplay.setText(UiController.menuDisplay.getText() + "\nLoading...");
			menuDisplay.setText(UiController.menuDisplay.getText() + "\nFinished");
			Thread.sleep(1500+((int)Math.random()*3500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return;
	}


	public synchronized static String getIp() {
		return ip;
	}

	public synchronized static void setIp(String ip) {
		UiController.ip = ip;
	}
}
