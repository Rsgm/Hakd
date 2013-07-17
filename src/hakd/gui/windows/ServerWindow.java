package hakd.gui.windows;

import hakd.gui.screens.GameScreen;
import hakd.networks.devices.Device;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ServerWindow implements WindowStage {
    private final Stage stage;
    private final Group canvas;

    private final Terminal terminal;
    private final Desktop desktop;
    private final Map map;
    private final Login login;
    private final Web web;

    private final Device device;
    private GameScreen screen;

    public ServerWindow(Device d) {
	stage = new Stage();
	device = d;

	canvas = new Group();
	stage.addActor(canvas);
	canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	terminal = new Terminal(device, this);
	desktop = new Desktop(device, this);
	map = new Map();
	login = new Login();
	web = new Web();
    }

    @Override
    public void render() {
	stage.act(Gdx.graphics.getDeltaTime());
	desktop.getTable().toBack();
	stage.draw();
    }

    @Override
    public void open() {
	Gdx.input.setInputProcessor(stage);
	stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
		false);

	desktop.open(); // default screen when you open the
			// server, I may change it to a login or
			// startup screen
    }

    @Override
    public void close() {
	stage.clear();
	screen.setOpenWindow(null);
    }

    public Stage getStage() {
	return stage;
    }

    public Group getCanvas() {
	return canvas;
    }

    public Terminal getTerminal() {
	return terminal;
    }

    public Desktop getDesktop() {
	return desktop;
    }

    public Map getMap() {
	return map;
    }

    public Login getLogin() {
	return login;
    }

    public Web getWeb() {
	return web;
    }

    public Device getDevice() {
	return device;
    }

    public GameScreen getScreen() {
	return screen;
    }

    public void setScreen(GameScreen screen) {
	this.screen = screen;
    }
}
