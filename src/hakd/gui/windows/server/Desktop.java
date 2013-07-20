package hakd.gui.windows.server;

import hakd.gui.Assets;
import hakd.gui.windows.actors.DesktopAppIcon;
import hakd.networks.devices.Device;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Desktop {
    private final ServerWindowStage window;
    private final Group desktop;
    private final Device device;
    private final ArrayList<Button> desktopApps;

    private final Image background;

    public Desktop(Device d, ServerWindowStage w) {
	device = d;
	window = w;

	desktop = new Group();
	desktopApps = new ArrayList<Button>();

	background = new Image(Assets.linearTextures.findRegion("wallpaper"));
	background.setBounds(0, 0, Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());
	desktop.addActor(background);

	defaultApps();
    }

    public void open() {
	window.getCanvas().addActor(desktop);
    }

    public void close() {
	window.getCanvas().removeActor(desktop);
    }

    private void defaultApps() {
	ImageButton terminal = new DesktopAppIcon(new TextureRegionDrawable(
		Assets.linearTextures.findRegion("consoleUp")),
		new TextureRegionDrawable(Assets.linearTextures
			.findRegion("consoleDown")), window.getTerminal());
	desktopApps.add(terminal);
	desktop.addActor(terminal);
    }

    public ServerWindowStage getWindow() {
	return window;
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
