package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class HakdScreen implements Screen {
    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();

    Game game;

    OrthographicCamera cam;
    SpriteBatch batch = new SpriteBatch();

    Rectangle viewport;

    public HakdScreen(Game game) {
	this.game = game;

	cam = new OrthographicCamera();
	batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

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

    public OrthographicCamera getCam() {
	return cam;
    }

    public void setCam(OrthographicCamera cam) {
	this.cam = cam;
    }
}
