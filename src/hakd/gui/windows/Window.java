package hakd.gui.windows;

import hakd.gui.screens.GameScreen;
import hakd.networks.devices.Device;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class Window {
    private final Stage stage;
    private final Stack canvas;

    private final Terminal terminal;
    private final Desktop desktop;
    private final Map map;
    private final Login login;
    private final Web web;

    private final Device device;
    private GameScreen screen;

    public Window(Device d) {
	stage = new Stage();
	device = d;

	canvas = new Stack();
	stage.addActor(canvas);
	canvas.setFillParent(true);

	terminal = new Terminal(device, this);
	desktop = new Desktop(device, this);
	map = new Map();
	login = new Login();
	web = new Web();
    }

    public void render() {
	stage.act(Gdx.graphics.getDeltaTime());
	stage.draw();
    }

    public void open(GameScreen screen) {
	Gdx.input.setInputProcessor(stage);
	stage.setViewport(500, 400, false);

	this.screen = screen;
	screen.setOpenWindow(this);

	desktop.open(); // default screen when you open the
			// server, I may change it to a login or
			// startup screen
    }

    public void close() {
	stage.clear();
	screen.setOpenWindow(null);
    }

    public Stage getStage() {
	return stage;
    }

    public Stack getCanvas() {
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
}
