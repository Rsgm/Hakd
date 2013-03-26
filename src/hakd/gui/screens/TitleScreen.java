package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TitleScreen extends HakdScreen {

	private BitmapFont		font;
	private BitmapFont		titleFont;
	private BitmapFont		creditFont;

	private boolean			clicked		= false;
	private String			login		= "Login: ";
	private final String	userName	= "user' OR 'x'='x";
	private String			pass		= "Password: ";

	private float			time		= 0;
	private int				counter		= 0;					// used to check where in the
	private int				random;

	Texture					loadingTexture;
	TextureRegion[]			loadingFrames;
	Animation				loading;

	public TitleScreen(Game game) { // simple title
		super(game);
	}

	@Override
	public void show() {
// game.setScreen(new MenuScreen(game));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("src/hakd/gui/resources/fonts/whitrabt.ttf"));
		font = generator.generateFont(16);
		titleFont = generator.generateFont(22);
		creditFont = generator.generateFont(14);
		generator.dispose();

		random = (int) (Math.random() * 24 + 8);

		loadingTexture = new Texture(Gdx.files.internal("graphics/loading.png"));
		TextureRegion[][] tmp = TextureRegion.split(loadingTexture, loadingTexture.getWidth() / 6, loadingTexture.getWidth() / 5);
		loadingFrames = new TextureRegion[6 * 5];
		int index = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				loadingFrames[index++] = tmp[i][j];
			}
		}
		loading = new Animation(0.025f, loadingFrames);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		batch.begin();
		font.setColor(0.0f, 0.7f, 0.0f, 1.0f);
		titleFont.setColor(0.0f, 0.7f, 0.0f, 1.0f);
		creditFont.setColor(0.0f, 1f, 0.0f, 0.12f);
		titleFont.draw(batch, "Hak'd", Gdx.graphics.getWidth() / 2.4f, Gdx.graphics.getHeight() / 1.5f);
		creditFont.draw(batch, "by Ryan Mirman", Gdx.graphics.getWidth() / 2.4f, Gdx.graphics.getHeight() / 1.5f - 20);

		if (Gdx.input.justTouched() || clicked) {
			clicked = true;
			time += delta;
			if (time % 1 > Math.random() * 0.8) {
				time = 0;
				if (counter < userName.length()) {
					login += userName.charAt(counter);
				} else if (counter < userName.length() + random) {
					pass += "*";
				} else {

				}
				counter++;
			}
			System.out.println(time);

			font.draw(batch, login, Gdx.graphics.getWidth() / 2.4f, Gdx.graphics.getHeight() / 1.5f - 20 * 2 - 5);
			font.draw(batch, pass, Gdx.graphics.getWidth() / 2.4f, Gdx.graphics.getHeight() / 1.5f - 20 * 3 - 5);

		}
		batch.end();
	}

	@Override
	public void dispose() {
	}
}
