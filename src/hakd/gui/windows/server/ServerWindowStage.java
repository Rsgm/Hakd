package hakd.gui.windows.server;

import hakd.gui.input.GameInput;
import hakd.gui.screens.GameScreen;
import hakd.gui.windows.WindowStage;
import hakd.networks.devices.Device;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ServerWindowStage implements WindowStage {
    private final Stage stage;
    private final Group canvas;

    private Terminal terminal;
    private Desktop desktop;
    private Map map;
    private Login login;
    private Web web;
    private Info info;

    private final Device device;
    private GameScreen screen;
    private Shutdown shutdown;

    public ServerWindowStage(Device d) {
	stage = new Stage();
	device = d;

	canvas = new Group();
	stage.addActor(canvas);
	canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
	stage.act(Gdx.graphics.getDeltaTime());
	desktop.getDesktop().toBack(); // makes sure no windows can go behind it
	stage.draw();
    }

    @Override
    public void open() {
	Gdx.input.setInputProcessor(stage);
	stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
		false);

	if (desktop == null) {
	    terminal = new Terminal(this);
	    map = new Map();
	    login = new Login();
	    web = new Web();
	    info = new Info(this);
	    shutdown = new Shutdown(this);

	    desktop = new Desktop(this); // has to be last, this creates the
					 // DesktopAppIcons
	}

	desktop.open(); // default screen when you open the
			// server, I may change it to a login or
			// startup screen
    }

    @Override
    public void close() {
	canvas.clear();
	screen.setOpenWindow(null);
	Gdx.input.setInputProcessor(new GameInput(screen.getGame(), screen
		.getCam(), screen.getPlayer(), screen));
    }

    @Override
    public void setScreen(GameScreen screen) {
	this.screen = screen;
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

    public void setTerminal(Terminal terminal) {
	this.terminal = terminal;
    }

    public void setDesktop(Desktop desktop) {
	this.desktop = desktop;
    }

    public void setMap(Map map) {
	this.map = map;
    }

    public void setLogin(Login login) {
	this.login = login;
    }

    public void setWeb(Web web) {
	this.web = web;
    }

    public Info getInfo() {
	return info;
    }

    public void setInfo(Info info) {
	this.info = info;
    }

    public Shutdown getShutdown() {
	return shutdown;
    }

    public void setShutdown(Shutdown shutdown) {
	this.shutdown = shutdown;
    }
}
