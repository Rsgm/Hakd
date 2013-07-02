package hakd.gui.windows;

import hakd.gui.Assets;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.HakdScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Settings { // temp scene 2d test window
    HakdScreen screen;

    Stage stage;

    public Settings(HakdScreen screen) {
	this.screen = screen;

	stage = new Stage();
	Table t = new Table();
	t.setFillParent(true);
	stage.addActor(t);
	Skin s = new Skin(Assets.nearestTextures);
	t.add();

	TextButtonStyle style = new TextButtonStyle();
	style.down = new TextureRegionDrawable(
		Assets.linearTextures.findRegion("loadingbackground"));
	style.font = Assets.consoleFont;

	TextButton button1 = new TextButton("Button 1", style);
	t.add(button1);

	TextButton button2 = new TextButton("Button 2", style);
	t.add(button2);

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
