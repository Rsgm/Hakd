package hakd.gui.windows.server;

import hakd.gui.Assets;
import hakd.networks.devices.Device;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public final class Info implements ServerWindow {
    private final ServerWindowStage window;
    private final Device device;
    private final Window infoWindow;
    private final ScrollPane scroll;
    private final Table table;

    public Info(ServerWindowStage w) {
	window = w;
	device = w.getDevice();

	Skin skin = Assets.skin;

	infoWindow = new Window(device.getType() + " info", skin);
	infoWindow.setSize(200, 300);
	table = new Table(skin);
	scroll = new ScrollPane(table);
	infoWindow.add(scroll);
    }

    @Override
    public void open() {
	add();
	window.getCanvas().addActor(infoWindow);
    }

    @Override
    public void close() {
	table.clear();
	window.getCanvas().removeActor(infoWindow);
    }

    private void add() {
	table.add("Brand:");
	// table.add(device.getBrand().toString());
	table.row();
	table.add("Model:");
	// table.add(device.getModel().toString());
	table.row();
	table.add("Device Type:");
	table.add(device.getType().toString());
	table.row();
	table.add("Device level:");
	table.add(device.getLevel() + "");
	table.row();
	table.add("CPU Sockets:");
	table.add(device.getCpuSockets() + "");
	table.row();
	table.add("Gpu Slots:");
	table.add(device.getGpuSlots() + "");
	table.row();
	table.add("Memory Slots:");
	table.add(device.getMemorySlots() + "");
	table.row();
	table.add("Storage Slots:");
	table.add(device.getStorageSlots() + "");
	table.row();
	table.add("Total Memory:");
	table.add(device.getTotalMemory() + "");
	table.row();
	table.add("Total Storage:");
	table.add(device.getTotalStorage() + "");
	table.row();
	table.add("Open Ports:");
	table.add(device.getPorts().toString() + "");
	table.row();
    }
}
