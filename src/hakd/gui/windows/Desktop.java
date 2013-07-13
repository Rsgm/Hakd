package hakd.gui.windows;

import hakd.gui.Assets;
import hakd.networks.devices.Device;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Desktop {
    private final Window window;

    private final Table table;
    private final Group desktop;

    private final Device device;

    private final ArrayList<Button> desktopApps;

    private boolean dragged = true;

    public Desktop(Device d, Window w) {
	device = d;
	window = w;

	Skin skin = Assets.skin;

	table = new com.badlogic.gdx.scenes.scene2d.ui.Window("Desktop", skin);
	desktop = new Group();
	desktopApps = new ArrayList<Button>();
	table.setFillParent(true);

	table.setTouchable(Touchable.childrenOnly); // creepy

	defaultApps();
	table.add(desktop).expand().fill();
    }

    public void open() {
	window.getCanvas().addActor(table);
    }

    public void close() {
	window.getCanvas().removeActor(table);
    }

    private void defaultApps() {
	final ImageButton terminal = new ImageButton(new TextureRegionDrawable(
		Assets.linearTextures.findRegion("console")));
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
		if (!dragged) {
		    window.getTerminal().open();
		} else {
		    dragged = false;
		}
	    }

	    @Override
	    public void touchDragged(InputEvent event, float x, float y,
		    int pointer) {
		super.touchDragged(event, x, y, pointer);
		terminal.setPosition(
			(int) ((Gdx.input.getX() + 5 - table.getX() - terminal
				.getWidth() / 2) / 20) * 20,
			(int) ((Gdx.graphics.getHeight() - Gdx.input.getY() - 0
				+ 5 - table.getY() - terminal.getHeight() / 2) / 20) * 20);
		dragged = true;
	    }
	});
	terminal.setSize(30, 30);
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

    public Device getDevice() {
	return device;
    }

    public ArrayList<Button> getDesktopApps() {
	return desktopApps;
    }
}
