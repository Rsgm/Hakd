package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class HakdScreen implements Screen {
	int					width		= Gdx.graphics.getWidth();
	int					height		= Gdx.graphics.getHeight();

	Game				game;

	OrthographicCamera	cam;
	SpriteBatch			batch		= new SpriteBatch();
	TextureAtlas		textures	= new TextureAtlas("src/hakd/gui/resources/textures.txt");

	Rectangle			viewport;

	Color				fontColor	= new Color(1.0f, 1.0f, 1.0f, 1.0f);

	// or read from, and write to, a preference or .ini file

	public HakdScreen(Game game) {
		this.game = game;

		cam = new OrthographicCamera(1, 1);

		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);// update camera
		cam.update();
		cam.apply(Gdx.gl10);

		// set viewport
		Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

		// clear previous frame
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		if (width >= height) {
			viewport = new Rectangle(0, -width / 3, width, width);
		} else {
			viewport = new Rectangle(-height / 3, 0, height, height);
		}
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
		return textures;
	}

	public void setTextures(TextureAtlas textures) {
		this.textures = textures;
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
