package hakd.gui;

import hakd.network.Network;
import hakd.networking.Dns_old;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class Windows { // no idea why I called this windows

	public static void computerTab() { // TODO no idea what to do with this yet

	}

	public static void debug(String ip) {
		GuiController.debugGrid.getChildren().clear();
		int id = Dns_old.findNetwork(ip);
		System.out.println(id);

		if(id == -1){ // if the network does not exist return
			return;
		}			
			Network network = Network.getNetworks().get(id);

			GuiController.debugGrid.add(new Label("ip"), 0, 0);
			GuiController.debugGrid.add(new Label(network.getIp()), 1, 0);
			GuiController.debugGrid.add(new Label("owner"), 0, 1);
			GuiController.debugGrid.add(new Label(network.getOwner()), 1, 1);
			GuiController.debugGrid.add(new Label("level"), 0, 2);
			GuiController.debugGrid.add(new Label(network.getLevel() + ""), 1, 2);
			GuiController.debugGrid.add(new Label("id"), 0, 3);
			GuiController.debugGrid.add(new Label(network.getNetworkId() + ""), 1, 3);
			GuiController.debugGrid.add(new Label("server limit"), 0, 4);
			GuiController.debugGrid.add(new Label(network.getServerLimit() + ""), 1, 4);
			GuiController.debugGrid.add(new Label("servers"), 0, 5);
			GuiController.debugGrid.add(new Label(network.getServers().size() + ""), 1, 5);
			GuiController.debugGrid.add(new Label("speed"), 0, 6);
			GuiController.debugGrid.add(new Label(network.getSpeed() + ""), 1, 6);
			GuiController.debugGrid.add(new Label("x coord"), 0, 7);
			GuiController.debugGrid.add(new Label(network.getxCoordinate() + ""), 1, 7);
			GuiController.debugGrid.add(new Label("y coord"), 0, 8);
			GuiController.debugGrid.add(new Label(network.getyCoordinate() + ""), 1, 8);
			GuiController.debugGrid.add(new Label("# of ports"), 0, 9);
			GuiController.debugGrid.add(new Label(network.getPorts().size() + ""), 1, 9);

			for(int i=10; i<network.getPorts().size(); i=+3){
				GuiController.debugGrid.add(new Label(network.getPorts().get(i)), 0, i);
				GuiController.debugGrid.add(new Label(network.getPorts().get(i+1)), 1, i);
				GuiController.debugGrid.add(new Label(network.getPorts().get(i+2)), 2, i);
			}
		}

	public static void networkTab(Network network) { // moved to gameGui, I think
		GuiController.mainScreen.getTabs().add(2, GuiController.networkTab);
		GuiController.networkTab.setContent(GuiController.networkPane);

		ArrayList<Label> labels = new ArrayList<Label>();
		for (int i = 0; i > network.getServers().size(); i++) {
			Label l = new Label();
			l.setLayoutX(20);
			l.setLayoutY(20 * i);
			//l.setText(network.getIp() + "/" + i + "		" + network.getServers().get(i).getMotherboard().size());
			l.setTextFill(Paint.valueOf("black"));
			l.setStyle("terminal-text");
			labels.add(l);
		}
		GuiController.networkPane.getChildren().addAll(labels);
	}
}
