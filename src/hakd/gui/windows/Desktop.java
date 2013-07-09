package hakd.gui.windows;

import hakd.gui.Assets;
import hakd.networks.devices.Device;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Desktop {
    private final Window window;

    private final Table table;
    private final Group desktop;
    private final Table menuBar;

    private final Device device;

    private final ArrayList<Button> desktopApps;

    public Desktop(Device d, Window w) {
	device = d;
	window = w;

	Skin skin = Assets.skin;

	table = new com.badlogic.gdx.scenes.scene2d.ui.Window("Terminal", skin);
	desktop = new Group();
	menuBar = new Table(skin);
	desktopApps = new ArrayList<Button>();
	defaultApps();

	table.add(desktop).expand().fill();
	table.row();
	table.add(menuBar).expandX().height(20).fill();
    }

    public void open() {
	window.getCanvas().add(table);
    }

    public void close() {
	window.getCanvas().removeActor(table);
    }

    private void defaultApps() {
	Skin skin = Assets.skin;

	final Button terminal = new Button(skin);
	terminal.addListener(new InputListener() {
	    @Override
	    public boolean touchDown(InputEvent event, float x, float y,
		    int pointer, int button) {
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y,
		    int pointer, int button) {
		super.touchUp(event, x, y, pointer, button);
		System.out.println("click");
	    }

	    @Override
	    public void touchDragged(InputEvent event, float x, float y,
		    int pointer) {
		super.touchDragged(event, x, y, pointer);
		terminal.setPosition(Gdx.input.getX() * .78f - 5 - table.getX()
			- terminal.getWidth() / 2, Gdx.graphics.getHeight()
			- Gdx.input.getY() * .835f - 100 - table.getY()
			- terminal.getHeight() / 2);
		System.out.println(Gdx.input.getX() + "	" + Gdx.input.getY());
	    }
	});
	terminal.setSize(20, 20);
	terminal.setPosition(20, 20);
	desktopApps.add(terminal);
	desktop.addActor(terminal);

    }

    public Window getWindow() {
	return window;
    }

    public Table getTable() {
	return table;
    }

    public Group getDesktop() {
	return desktop;
    }

    public Table getMenuBar() {
	return menuBar;
    }

    public Device getDevice() {
	return device;
    }

    public ArrayList<Button> getDesktopApps() {
	return desktopApps;
    }
}
