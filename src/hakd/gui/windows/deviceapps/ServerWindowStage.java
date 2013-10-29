package hakd.gui.windows.deviceapps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import hakd.gui.input.GameInput;
import hakd.gui.screens.GameScreen;
import hakd.gui.windows.WindowStage;
import hakd.networks.devices.Device;

public final class ServerWindowStage implements WindowStage {
    private final Stage stage;
    private final Group canvas;

    private Terminal terminal0;
    private Terminal terminal1;
    private Terminal terminal2;
    private Terminal terminal3;
    private Desktop desktop;
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

        // TODO disabled until this bug is fixed
        // if (Gdx.input.isKeyPressed(Keys.TAB)) {
        // screen.getGame().setScreen(screen.getMap());
        // }
    }

    @Override
    public void open() {
        Gdx.input.setInputProcessor(stage);
        stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        if (desktop == null) {
            terminal0 = new Terminal(this);
            terminal1 = new Terminal(this);
            terminal2 = new Terminal(this);
            terminal3 = new Terminal(this);
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
        Gdx.input.setInputProcessor(new GameInput(screen.getGame(), (OrthographicCamera) screen.getCam(), screen.getPlayer()));
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

    public Desktop getDesktop() {
        return desktop;
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

    public void setDesktop(Desktop desktop) {
        this.desktop = desktop;
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

    public Terminal getTerminal0() {
        return terminal0;
    }

    public void setTerminal0(Terminal terminal0) {
        this.terminal0 = terminal0;
    }

    public Terminal getTerminal1() {
        return terminal1;
    }

    public void setTerminal1(Terminal terminal1) {
        this.terminal1 = terminal1;
    }

    public Terminal getTerminal2() {
        return terminal2;
    }

    public void setTerminal2(Terminal terminal2) {
        this.terminal2 = terminal2;
    }

    public Terminal getTerminal3() {
        return terminal3;
    }

    public void setTerminal3(Terminal terminal3) {
        this.terminal3 = terminal3;
    }
}
