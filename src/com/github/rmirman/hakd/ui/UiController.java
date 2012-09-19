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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import com.github.rmirman.hakd.RunGame;
import com.github.rmirman.hakd.gameplay.Commands;
import com.github.rmirman.hakd.network.Dns;
import com.github.rmirman.hakd.network.Network;

public class UiController extends Application{

	public static String ip;
	public static String mobo;

	//root
	public static AnchorPane root = new AnchorPane();

	// main screen
	public static TabPane mainScreen = new TabPane();
	public static Tab world = new Tab("World");
	public static Tab region = new Tab("Region");
	public static Tab network = new Tab("Network");
	public static Tab computer = new Tab("Computer");
	public static AnchorPane worldPane = new AnchorPane();
	public static AnchorPane region1Pane = new AnchorPane();
	public static AnchorPane region2Pane = new AnchorPane();
	public static AnchorPane region3Pane = new AnchorPane();
	public static AnchorPane region4Pane = new AnchorPane();
	public static AnchorPane region5Pane = new AnchorPane();
	public static AnchorPane networkPane = new AnchorPane();
	public static AnchorPane computerPane = new AnchorPane();
	ImageView region1 = new ImageView();
	ImageView region2 = new ImageView();
	ImageView region3 = new ImageView();
	ImageView region4 = new ImageView();
	ImageView region5 = new ImageView();


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
	public static Circle[] nCircle = new Circle[500];
	public static Line[] connection = new Line[500];


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
		world.setContent(worldPane);
		Image region1Icon = new Image("/com/github/rmirman/hakd/ui/icons/region1.png");
		Image region2Icon = new Image("/com/github/rmirman/hakd/ui/icons/region2.png");
		Image region3Icon = new Image("/com/github/rmirman/hakd/ui/icons/region3.png");
		Image region4Icon = new Image("/com/github/rmirman/hakd/ui/icons/region4.png");
		Image region5Icon = new Image("/com/github/rmirman/hakd/ui/icons/region5.png");
		region1.setImage(region1Icon);
		region2.setImage(region2Icon);
		region3.setImage(region3Icon);
		region4.setImage(region4Icon);
		region5.setImage(region5Icon);
		region1.setLayoutX(15);
		region1.setLayoutY(210);
		region2.setLayoutX(350);
		region2.setLayoutY(30);
		region3.setLayoutX(600);
		region3.setLayoutY(350);
		region4.setLayoutX(10);
		region4.setLayoutY(10);
		region5.setLayoutX(250);
		region5.setLayoutY(370);
		worldPane.getChildren().addAll(region1, region2, region3, region4, region5);


		// info screen
		infoScreen.getTabs().addAll(info, resources, runningPrograms);
		infoScreen.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		info.setContent(infoPane);
		resources.setContent(resourcesPane);
		runningPrograms.setContent(runningProgramsPane);

		primaryStage.show();


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
		region1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(region.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, region);
				}
				region.setContent(region1Pane);
				mainScreen.getSelectionModel().select(region);
				region.setText("North America");
			}
		});
		region2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(region.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, region);
				}
				region.setContent(region2Pane);
				mainScreen.getSelectionModel().select(region);
				region.setText("Europe");
			}
		});
		region3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(region.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, region);
				}
				region.setContent(region3Pane);
				mainScreen.getSelectionModel().select(region);
				region.setText("Asia");
			}
		});
		region4.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(region.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, region);
				}
				region.setContent(region4Pane);
				mainScreen.getSelectionModel().select(region);
				region.setText("Companies");
			}
		});
		region5.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if(region.getTabPane() != mainScreen){
					mainScreen.getTabs().add(1, region);
				}
				region.setContent(region5Pane);
				mainScreen.getSelectionModel().select(region);
				region.setText("The Dark Net");
			}
		});


		// game
		Dns dnsServer = new Dns();
		dnsServer.hashCode();
		Network.network[0] = new Network(0, "new player");
		Network.network[0].populate();
	//	Store store = new Store();
	//	store.hashCode();

		ip =  Network.network[0].getIp() + "¬0»";
		
		new RunGame();
	}

	public static void addNetwork(int region, int id){
		boolean taken = false;
		nCircle[id] = new Circle();
		switch(region){
		case 1:
			region1Pane.getChildren().add(nCircle[id]);
			break;
		case 2:
			region2Pane.getChildren().add(nCircle[id]);
			break;
		case 3:
			region3Pane.getChildren().add(nCircle[id]);
			break;
		case 4:
			region4Pane.getChildren().add(nCircle[id]);
			break;
		case 5:
			region5Pane.getChildren().add(nCircle[id]);
			break;
		default:
			region1Pane.getChildren().add(nCircle[id]);
			break;
		}
		do{
			System.out.println("1");
			nCircle[id].setLayoutX((int)(Math.random()*20)*30+14);
			nCircle[id].setLayoutY((int)(Math.random()*20)*30+14);
			System.out.println("2");
			for(int i=0; i<nCircle.length; i++){
				if(i == id){
					System.out.println("3.1");
					taken = false;
				}else if(nCircle[i] != null){
					if(nCircle[id].getLayoutX() != nCircle[i].getLayoutX()||nCircle[id].getLayoutY() != nCircle[i].getLayoutY()){
						System.out.println("3.5");
						taken = false;
					}else if(nCircle[id].getLayoutX() == nCircle[i].getLayoutX()&&nCircle[id].getLayoutY() == nCircle[i].getLayoutY()){
						System.out.println("4");
						taken = true;
						break;
					}
				}
			}
			System.out.println("5");
		}while(taken == true);
		System.out.println("6");

		nCircle[id].setRadius(12);
		switch(0){
		case 0:
			nCircle[id].getStyleClass().add("friendly-network");
			break;
		case 1:
			nCircle[id].getStyleClass().add("nuetral-network");
			break;
		case 2:
			nCircle[id].getStyleClass().add("enemy-network");
			break;
		default:
			nCircle[id].getStyleClass().add("nuetral-network");
			break;
		}
		Tooltip t = new Tooltip(Network.network[id].getIp());
		Tooltip.install(nCircle[id], t);

		Network.network[id].setxCoordinate((int) nCircle[id].getLayoutX());
		Network.network[id].setyCoordinate((int) nCircle[id].getLayoutY());

		nCircle[id].setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				//System.out.println("network" + Network.network[id].getIp());

			}
		});
	}

}
