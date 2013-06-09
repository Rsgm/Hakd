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
	int					width			= Gdx.graphics.getWidth();
	int					height			= Gdx.graphics.getHeight();

	Game				game;

	OrthographicCamera	cam;
	SpriteBatch			batch			= new SpriteBatch();
	TextureAtlas		textures		= new TextureAtlas("src/hakd/gui/resources/textures.txt");

	Rectangle			viewport;
	static final int	VIRTUAL_WIDTH	= 800;
	static final int	VIRTUAL_HEIGHT	= 600;
	static final float	ASPECT_RATIO	= (float) VIRTUAL_WIDTH / (float) VIRTUAL_HEIGHT;

	Color				fontColor		= new Color(1.0f, 1.0f, 1.0f, 1.0f);

	// or read from, and write to, a preference or .ini file

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

	@Override
	public void resize(int width, int height) { // credit to soy_yuma of blog.acamara.es, though a bit modified
		// calculate new viewport
		float aspectRatio = (float) width / (float) height;
		float scale = 1f;

		if (aspectRatio > ASPECT_RATIO) {
			scale = (float) height / (float) VIRTUAL_HEIGHT;
		} else if (aspectRatio < ASPECT_RATIO) {
			scale = (float) width / (float) VIRTUAL_WIDTH;
		} else {
			scale = (float) width / (float) VIRTUAL_WIDTH;
		}

		float w;
		float h;
		if (width >= height) {
			w = VIRTUAL_WIDTH * scale;
			h = VIRTUAL_WIDTH * scale;
		} else {
			w = VIRTUAL_HEIGHT * scale;
			h = VIRTUAL_HEIGHT * scale;
		}

		System.out.println(w + "	" + h);

		viewport = new Rectangle(0/*crop.x*/, 0/*crop.y*/, w, h);
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
