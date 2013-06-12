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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

public class HakdScreen implements Screen {
	int					width			= Gdx.graphics.getWidth();
	int					height			= Gdx.graphics.getHeight();

	Game				game;

	OrthographicCamera	cam;
	SpriteBatch			batch			= new SpriteBatch();
	TextureAtlas		textures		= new TextureAtlas("hakd/gui/resources/textures.txt");

	Rectangle			viewport;

	Color				fontColor		= new Color(1.0f, 1.0f, 1.0f, 1.0f);
	// or read from, and write to, a preference or .ini file

	static final int	VIRTUAL_WIDTH	= 25;
	static final int	VIRTUAL_HEIGHT	= 21;
	static final float	ASPECT_RATIO	= (float) VIRTUAL_WIDTH / (float) VIRTUAL_HEIGHT;

	public HakdScreen(Game game) {
		this.game = game;

		cam = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

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

	@SuppressWarnings("deprecation")
	@Override
	public void resize(int width, int height) { // some how this actually works, I spent way too long on this
		Vector2 newVirtualRes = new Vector2(0f, 0f);
		Vector2 crop = new Vector2(width, height);

		newVirtualRes.set(Scaling.fill.apply(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, width, height));

		crop.sub(newVirtualRes);
		crop.mul(.5f); // not sure why this is deprecated

		viewport = new Rectangle(crop.x, crop.y, newVirtualRes.x, newVirtualRes.y);

		this.width = width;
		this.height = height;
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
