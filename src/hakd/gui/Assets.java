package hakd.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Assets {
	public static TextureAtlas nearestTextures;
	public static TextureAtlas linearTextures;

	public static Skin skin;

	public static BitmapFont font;
	public static BitmapFont consoleFont;
	public static Color fontColor;
	public static Color consoleFontColor;

	public static void load() {

		nearestTextures = new TextureAtlas(Gdx.files.internal("hakd/gui/resources/nTextures.txt"));
		linearTextures = new TextureAtlas(Gdx.files.internal("hakd/gui/resources/lTextures.txt"));

		// I used black because it is a texture that will not change
		nearestTextures.findRegion("black").getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		skin = new Skin(Gdx.files.internal("hakd/gui/resources/skins/uiskin.json"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("hakd/gui/resources/fonts/whitrabt.ttf"));
		font = generator.generateFont(16);
		consoleFont = generator.generateFont(16);
		generator.dispose();

		fontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		consoleFontColor = new Color(0.0f, 0.7f, 0.0f, 1.0f);

		font.setColor(fontColor);
		consoleFont.setColor(consoleFontColor);

		skin.getFont("console-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		skin.getAtlas().findRegion("default").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
