package hakd.gui.windows;

import hakd.gui.input.GameInput;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.HakdScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Window { // these are little windows that pop up when you interact
		      // with stuff, even the settings
    HakdScreen screen;
    Sprite border;
    Sprite display;
    Sprite close;
    int x;
    int y;

    Stage stage;

    public Window() {
    }

    public void render(Camera cam, SpriteBatch batch, float delta) {
	batch.begin();
	border.draw(batch);
	display.draw(batch);
	close.draw(batch);
	batch.end();
    } // what to render and what to return?

    public void open(TextureAtlas textures, HakdScreen screen) {
	this.screen = screen;

	int width = screen.getWidth();
	int height = screen.getHeight();

	int w;
	int h;

	w = (int) (width * .8);
	h = (int) (height * .8);

	x = (width - w) / 2; // 0/2 for the menu
	y = (height - h) / 2;

	border = new Sprite(textures.findRegion("windowBorder"));
	border.setBounds(x, y, w, h);
	display = new Sprite(textures.findRegion("windowDisplay")); // this,
								    // being one
								    // pixel,
								    // saved you
								    // about 2KB
								    // of
								    // memory,
								    // you're
								    // welcome
	display.setBounds(x + 20, y + 20, w - 40, h - 40);

	close = new Sprite(textures.findRegion("close"));
	close.setPosition(width - x - 20, height - y - 20);
    }

    public void close() {
	GameScreen gS = (GameScreen) screen;
	GameScreen.OPEN_WINDOW = null;
	Gdx.input.setInputProcessor(new GameInput(gS.getGame(), gS.getCam(), gS
		.getPlayer(), gS));
    }
}
