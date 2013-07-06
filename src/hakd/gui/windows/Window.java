package hakd.gui.windows;

import hakd.gui.screens.GameScreen;
import hakd.gui.screens.HakdScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class Window { // temp scene 2d test window
    private final HakdScreen screen;

    private final Stage stage;
    private final Stack group;

    private final Terminal terminal;
    private final Desktop desktop;
    private final Map map;
    private final Login login;
    private final Web web;

    public Window(HakdScreen screen) {
	this.screen = screen;
	stage = new Stage();

	group = new Stack();
	stage.addActor(group);
	group.setFillParent(true);

	terminal = new Terminal();
	desktop = new Desktop();
	map = new Map();
	login = new Login();
	web = new Web();

	group.add(terminal.open());
    }

    public void render() {
	stage.act(Gdx.graphics.getDeltaTime());
	stage.draw();
    }

    public void open(GameScreen screen) {
	Gdx.input.setInputProcessor(stage);
	stage.setViewport(500, 400, false);
    }

    public void close() {
	// TODO Auto-generated method stub

    }
}
