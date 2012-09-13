package com.github.rmirman.hakd.ui;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class UiController extends Application{

	private static Circle[] networks = new Circle[100];
	
	private static Tab world = new Tab("World");
	private static Tab region = new Tab("Region");
	private static Tab network = new Tab("Network");
	private static Tab computer = new Tab("Computer");

	private static Tab info = new Tab("Info");
	private static Tab resources = new Tab("Resources");
	private static Tab runningPrograms = new Tab("Running Programs");
	
	
	public static void run(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

	primaryStage.setTitle("HAXIST"); // put this code in a class not a method and make them public maybe static
	AnchorPane root = new AnchorPane();
	primaryStage.setScene(new Scene(root, 1024, 768));
	root.getStylesheets().add(UiController.class.getResource("UI design.css").toExternalForm());

	// menu
	ToolBar menuBar = new ToolBar(); // add some more icons and stuff
	Label time = new Label("10:54 PM");
	MenuButton menuButton1 = new MenuButton("Menu");
	MenuItem menuItem1 = new MenuItem("settings");

	root.getChildren().add(menuBar);
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
	SplitPane splitPane1 = new SplitPane();
	SplitPane splitPane2 = new SplitPane();
	AnchorPane splitAnchor1 = new AnchorPane();
	AnchorPane splitAnchor2 = new AnchorPane();
	AnchorPane splitAnchor3 = new AnchorPane();
	AnchorPane splitAnchor4 = new AnchorPane();

	root.getChildren().add(splitPane1);
	splitPane1.getItems().addAll(splitAnchor1,splitAnchor2);
	splitPane1.setOrientation(Orientation.VERTICAL);
	splitAnchor1.getChildren().add(splitPane2);
	splitPane2.getItems().addAll(splitAnchor3, splitAnchor4);
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
	AnchorPane.setBottomAnchor(splitAnchor3, 0.0);
	AnchorPane.setLeftAnchor(splitAnchor3, 0.0);
	AnchorPane.setRightAnchor(splitAnchor3, 0.0);
	AnchorPane.setTopAnchor(splitAnchor3, 0.0);
	AnchorPane.setBottomAnchor(splitAnchor4, 0.0);
	AnchorPane.setLeftAnchor(splitAnchor4, 0.0);
	AnchorPane.setRightAnchor(splitAnchor4, 0.0);
	AnchorPane.setTopAnchor(splitAnchor4, 0.0);
	AnchorPane.setBottomAnchor(splitPane1, 0.0);
	AnchorPane.setLeftAnchor(splitPane1, 0.0);
	AnchorPane.setRightAnchor(splitPane1, 0.0);
	AnchorPane.setTopAnchor(splitPane1, 27.0);
	AnchorPane.setBottomAnchor(splitPane2, 0.0);
	AnchorPane.setLeftAnchor(splitPane2, 0.0);
	AnchorPane.setRightAnchor(splitPane2, 0.0);
	AnchorPane.setTopAnchor(splitPane2, 0.0);

	// terminal
	final TextArea terminalDisplay = new TextArea("Starting...\ndone");
	final TextField terminalInput = new TextField("C:\\Windows\\System32>");

	splitAnchor2.getChildren().addAll(terminalDisplay, terminalInput);
	terminalDisplay.setEditable(false);
	terminalDisplay.setMinHeight(25.0);
	terminalDisplay.getStyleClass().add("terminal-text");
	terminalInput.getStyleClass().add("terminal-text");
	terminalInput.setId("terminal-input");

	AnchorPane.setBottomAnchor(terminalDisplay, 22.0);
	AnchorPane.setLeftAnchor(terminalDisplay, 0.0);
	AnchorPane.setRightAnchor(terminalDisplay, 0.0);
	AnchorPane.setTopAnchor(terminalDisplay, 0.0);
	AnchorPane.setBottomAnchor(terminalInput, 0.0);
	AnchorPane.setLeftAnchor(terminalInput, 0.0);
	AnchorPane.setRightAnchor(terminalInput, 0.0);

	// main screen
	TabPane mainScreen = new TabPane();

	splitAnchor4.getChildren().add(mainScreen);
	mainScreen.getTabs().addAll(world, region, network, computer);
	mainScreen.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
	world.setClosable(false);

	AnchorPane.setBottomAnchor(mainScreen, 0.0);
	AnchorPane.setLeftAnchor(mainScreen, 0.0);
	AnchorPane.setRightAnchor(mainScreen, 0.0);
	AnchorPane.setTopAnchor(mainScreen, 0.0);

	// info screen
	TabPane infoScreen = new TabPane();

	splitAnchor3.getChildren().add(infoScreen);
	infoScreen.getTabs().addAll(info, resources, runningPrograms);
	infoScreen.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

	AnchorPane.setBottomAnchor(infoScreen, 0.0);
	AnchorPane.setLeftAnchor(infoScreen, 0.0);
	AnchorPane.setRightAnchor(infoScreen, 0.0);
	AnchorPane.setTopAnchor(infoScreen, 0.0);

	world.setContent(networks[1]);

	primaryStage.show();

	// event handles

	terminalInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
		public void handle(KeyEvent textInput) {
			try{
				if(textInput.getText().charAt(0) == 13){
					if(terminalInput.getText().equals("C:\\Windows\\System32>help")){
						System.out.print("jfkwjflkg" + terminalInput.getText() + "'");
						terminalDisplay.setText("Cannot Divide by 0");
					}
					terminalDisplay.setText(terminalDisplay.getText() + "\n" + terminalInput.getText());
					terminalDisplay.end();
					System.out.println(terminalInput.getText());
					terminalInput.setText("C:\\Windows\\System32>");
					terminalInput.end();
				}
				else{
					//System.out.println(terminalInput.getText() + textInput.getText());
				}
			}
			catch (Exception ex) {
			}
		}
	});

	// game
	//	System.out.println("test");
	}

	public static void addServer(){
		networks[1] = new Circle();
		networks[1].setLayoutX(60);
		networks[1].setLayoutY(200);
		networks[1].setFill(Paint.valueOf("#bfbfbf"));
		
	}

}
