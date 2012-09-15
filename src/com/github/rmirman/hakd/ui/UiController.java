package com.github.rmirman.hakd.ui;

import com.github.rmirman.hakd.gameplay.Commands;

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

	public static String ip = (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256) +"¬0»";
	
	//root
	public static AnchorPane root = new AnchorPane();

	// main screen
	public static TabPane mainScreen = new TabPane();
	public static Tab world = new Tab("World");
	public static Tab region = new Tab("Region");
	public static Tab network = new Tab("Network");
	public static Tab computer = new Tab("Computer");
	public static AnchorPane worldPane = new AnchorPane();
	public static AnchorPane regionPane = new AnchorPane();
	public static AnchorPane networkPane = new AnchorPane();
	public static AnchorPane computerPane = new AnchorPane();

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
	public static AnchorPane splitAnchor3 = new AnchorPane();
	public static AnchorPane splitAnchor4 = new AnchorPane();

	// terminal
	public static TextArea terminalDisplay = new TextArea("Starting...\ndone");
	public static TextField terminalInput = new TextField(ip);


	static AnchorPane aptest = new AnchorPane();

	public static void run(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hak'd"); // put this code in a class not a method and make them public maybe static
		primaryStage.setScene(new Scene(root, 1024, 768));
		root.getStylesheets().add(UiController.class.getResource("UI design.css").toExternalForm());

		// menu
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
		splitAnchor4.getChildren().add(mainScreen);
		mainScreen.getTabs().addAll(world, region, network, computer);
		mainScreen.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		world.setClosable(false);
		world.setContent(worldPane);
		region.setContent(regionPane);
		network.setContent(networkPane);
		computer.setContent(computerPane);

		AnchorPane.setBottomAnchor(mainScreen, 0.0);
		AnchorPane.setLeftAnchor(mainScreen, 0.0);
		AnchorPane.setRightAnchor(mainScreen, 0.0);
		AnchorPane.setTopAnchor(mainScreen, 0.0);

		// info screen
		splitAnchor3.getChildren().add(infoScreen);
		infoScreen.getTabs().addAll(info, resources, runningPrograms);
		infoScreen.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		info.setContent(infoPane);
		resources.setContent(resourcesPane);
		runningPrograms.setContent(runningProgramsPane);

		AnchorPane.setBottomAnchor(infoScreen, 0.0);
		AnchorPane.setLeftAnchor(infoScreen, 0.0);
		AnchorPane.setRightAnchor(infoScreen, 0.0);
		AnchorPane.setTopAnchor(infoScreen, 0.0);

		primaryStage.show();


		AnchorPane aptest = new AnchorPane();


		// event handles

		terminalInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent textInput) {
				try{
					if(textInput.getText().charAt(0) == 13){
						terminalDisplay.setText(terminalDisplay.getText() + "\n" + terminalInput.getText());
						Commands.command(terminalInput.getText());
						terminalDisplay.end();
						System.out.println(terminalInput.getText());
						terminalInput.setText(ip);
						terminalInput.end();
					}else{
						//System.out.println(terminalInput.getText() + textInput.getText());
					}
				}

				catch (Exception ex) {
				}
			}
		});

		// game
	}

	public static void addNetwork(){
		Circle[] networks = new Circle[2];
		networks[1] = new Circle();
		regionPane.getChildren().add(networks[1]);
		networks[1].setLayoutX(600);
		networks[1].setLayoutY(300);
		networks[1].setRadius(50.0);
		networks[1].setFill(Paint.valueOf("#bfbfbf"));
		networks[0] = new Circle();
		networks[0].setLayoutX(300);
		networks[0].setLayoutY(350);
		networks[0].setRadius(50.0);
		networks[0].setFill(Paint.valueOf("#000000"));
		regionPane.getChildren().add(networks[0]);
		System.out.println(region.getContent());
	}

}
