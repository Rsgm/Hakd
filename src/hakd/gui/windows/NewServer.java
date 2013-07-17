package hakd.gui.windows;

import hakd.gui.Assets;
import hakd.gui.screens.GameScreen;
import hakd.networks.devices.Device;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class NewServer implements WindowStage {
    private final Device device;
    private GameScreen screen;
    private final Stage stage;

    private final Window window;

    public NewServer(Device d) {
	this.device = d;

	Skin skin = Assets.skin;

	stage = new Stage();
	window = new Window("Buy a new server", skin);
	stage.addActor(window);

	window.setSize(Gdx.graphics.getWidth() * 0.8f,
		Gdx.graphics.getHeight() * 0.8f);
	window.setPosition(stage.getWidth() - window.getWidth(),
		stage.getHeight() - stage.getHeight());

	TextButton server1 = new TextButton("Buy", skin);
	TextButton server2 = new TextButton("Buy", skin);
	TextButton server3 = new TextButton("Buy", skin);
	TextButton server4 = new TextButton("Buy", skin);

	window.add("Cpu:" + "\n" + "\n" + "\nStorage: 16GB");
	window.add(server1);
	window.row();
	window.add("test");
	window.add(server2);
	window.row();
	window.add();
	window.add(server3);
	window.add();
	window.add(server3);
    }

    @Override
    public void render() {
	stage.act(Gdx.graphics.getDeltaTime());
	stage.draw();
    }

    @Override
    public void open() {
	Gdx.input.setInputProcessor(stage);
	stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
		false);
    }

    @Override
    public void close() {
	stage.clear();
	screen.setOpenWindow(null);
    }

    @Override
    public void setScreen(GameScreen screen) {
	this.screen = screen;
    }
}
