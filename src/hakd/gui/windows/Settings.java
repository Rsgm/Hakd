package hakd.gui.windows;

import hakd.gui.Assets;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.HakdScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Settings { // temp scene 2d test window
    HakdScreen screen;

    Stage stage;

    TextField display;
    TextField input;

    public Settings(HakdScreen screen) { // this has gotten pretty messy, lets
					 // move it to separate classes, I think
					 // I am through with testing
	this.screen = screen;

	stage = new Stage();
	Table t = new Table();
	t.setFillParent(true);
	stage.addActor(t);

	t.setBackground(new TextureRegionDrawable(Assets.nearestTextures
		.findRegion("black")));
	t.setBounds(Gdx.graphics.getWidth() / 10,
		Gdx.graphics.getHeight() / 10, Gdx.graphics.getWidth()
			- Gdx.graphics.getWidth() * 2 / 10,
		Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 2 / 10);

	TextFieldStyle displayStyle = new TextFieldStyle();
	displayStyle.background = new TextureRegionDrawable(
		Assets.nearestTextures.findRegion("black"));
	displayStyle.font = Assets.consoleFont;
	displayStyle.fontColor = Assets.consoleFontColor;

	TextFieldStyle inputStyle = new TextFieldStyle();
	inputStyle.background = new TextureRegionDrawable(
		Assets.nearestTextures.findRegion("black"));
	inputStyle.font = Assets.consoleFont;
	inputStyle.fontColor = Assets.consoleFontColor;

	display = new TextField("", displayStyle);
	input = new TextField("", inputStyle);

	ScrollPane s = new ScrollPane(display);

	t.add(s).prefHeight(600f).prefWidth(400f);
	t.row();
	t.add(input);
    }

    public void render(OrthographicCamera cam, SpriteBatch batch, float delta) {
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
