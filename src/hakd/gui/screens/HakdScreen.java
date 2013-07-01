package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class HakdScreen implements Screen {
    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();

    Game game;

    OrthographicCamera cam;
    SpriteBatch batch = new SpriteBatch();
    TextureAtlas nearestTextures = new TextureAtlas(
	    "hakd/gui/resources/nTextures.txt");
    TextureAtlas linearTextures = new TextureAtlas(
	    "hakd/gui/resources/lTextures.txt");

    Rectangle viewport;

    Color fontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    // or read from, and write to, a preference or .ini file

    public HakdScreen(Game game) {
	this.game = game;

	cam = new OrthographicCamera();
	batch = new SpriteBatch();

	// I used black because it is a texture that will not change
	nearestTextures.findRegion("black").getTexture()
		.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);// update
									    // camera
	cam.update();
	cam.apply(Gdx.gl10);
    }

    @Override
    public void resize(int width, int height) {
	// TODO Auto-generated method stub
    }

    @Override
    public void show() {
	// TODO Auto-generated method stub

    }

    @Override
    public void hide() {
	dispose();
    }

    @Override
    public void pause() {
	// TODO Auto-generated method stub

    }

    @Override
    public void resume() {
	// TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
	Gdx.input.setInputProcessor(null);
	batch.dispose();
    }

    public int getWidth() {
	return width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    public int getHeight() {
	return height;
    }

    public void setHeight(int height) {
	this.height = height;
    }

    public Game getGame() {
	return game;
    }

    public void setGame(Game game) {
	this.game = game;
    }

    public SpriteBatch getBatch() {
	return batch;
    }

    public void setBatch(SpriteBatch batch) {
	this.batch = batch;
    }

    public TextureAtlas getTextures() {
	return nearestTextures;
    }

    public void setTextures(TextureAtlas textures) {
	this.nearestTextures = textures;
    }

    public Color getFontColor() {
	return fontColor;
    }

    public void setFontColor(Color fontColor) {
	this.fontColor = fontColor;
    }

    public OrthographicCamera getCam() {
	return cam;
    }

    public void setCam(OrthographicCamera cam) {
	this.cam = cam;
    }
}
