package gui.windows.device;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import gui.input.GameInput;
import gui.screens.GameScreen;
import networks.devices.Device;

public class DeviceScene {
    private final Stage stage;
    private final Group canvas;

    private Terminal terminal0;
    private Terminal terminal1;
    private Terminal terminal2;
    private Terminal terminal3;
    private Desktop desktop;
    //    private Login login;
//    private Web web;
    private Info info;
    private TextEdit textEdit;

    private final Device device;
    private GameScreen screen;
    private Shutdown shutdown;

    public DeviceScene(GameScreen screen, Device d) {
        this.screen = screen;
        stage = new Stage();
        device = d;

        canvas = new Group();
        stage.addActor(canvas);
        canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        desktop.getDesktop().toBack(); // makes sure no windows can go behind it
        stage.draw();

        // TODO disabled until this bug is fixed
        // if (Gdx.input.isKeyPressed(Keys.TAB)) {
        // screen.getGame().setScreen(screen.getMap());
        // }
    }

    public void open() {
        Gdx.input.setInputProcessor(stage);
        stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        if (desktop == null) {
            terminal0 = new Terminal(this);
            terminal1 = new Terminal(this);
            terminal2 = new Terminal(this);
            terminal3 = new Terminal(this);
//            login = new Login();
//            web = new Web();
            info = new Info(this);
            shutdown = new Shutdown(this);
            textEdit = new TextEdit(this);

            desktop = new Desktop(this); // has to be last, this creates the DesktopAppIcons
        }

        desktop.open(); // default screen when you open the server, I may change it to a login or startup screen
    }

    public void close() {
        canvas.clear();
        screen.setDeviceScene(null);
        Gdx.input.setInputProcessor(new GameInput(screen.getGame(), (OrthographicCamera) screen.getCam()));
    }

    public void setScreen(GameScreen screen) {
        this.screen = screen;
    }

    public Stage getStage() {
        return stage;
    }

    public Group getCanvas() {
        return canvas;
    }

    public Device getDevice() {
        return device;
    }

    public GameScreen getScreen() {
        return screen;
    }

    public Info getInfo() {
        return info;
    }

    public Shutdown getShutdown() {
        return shutdown;
    }

    public Terminal getTerminal0() {
        return terminal0;
    }

    public Terminal getTerminal1() {
        return terminal1;
    }

    public Terminal getTerminal2() {
        return terminal2;
    }

    public Terminal getTerminal3() {
        return terminal3;
    }

    public TextEdit getTextEdit() {
        return textEdit;
    }
}
