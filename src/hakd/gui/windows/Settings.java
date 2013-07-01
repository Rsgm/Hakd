package hakd.gui.windows;

import hakd.gui.screens.GameScreen;
import hakd.gui.screens.HakdScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Settings { // temp scene 2d test window

    HakdScreen screen;

    Stage stage;

    public Settings(HakdScreen screen) {
	this.screen = screen;

	stage = new Stage();
	Table t = new Table();
	t.setFillParent(true);
	stage.addActor(t);
	Skin s = new Skin(screen.getTextures());
	t.add(new Image(s.getSprite("black")));
    }

    public void render(OrthographicCamera cam, SpriteBatch batch, float delta) {
	stage.act(Gdx.graphics.getDeltaTime());
	stage.draw();
    }

    public void open(TextureAtlas textures, GameScreen screen) {
	Gdx.input.setInputProcessor(stage);
	stage.setViewport(500, 400, false);
    }

    public void close() {
	// TODO Auto-generated method stub

    }
}
