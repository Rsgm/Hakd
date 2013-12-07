package hakd.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public final class Assets {
    public static TextureAtlas nearestTextures;
    public static TextureAtlas linearTextures;

    public static Skin skin;

    public static BitmapFont font;
    public static BitmapFont consoleFont;
    public static Color fontColor;
    public static Color consoleFontColor;

    public static HashMap<Shader, ShaderProgram> shaders;

    public static void load() {
        nearestTextures = new TextureAtlas(Gdx.files.internal("hakd/gui/resources/nTextures.txt"));
        linearTextures = new TextureAtlas(Gdx.files.internal("hakd/gui/resources/lTextures.txt"));

        // I used black because it is a texture that will not change
        nearestTextures.findRegion("black").getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        skin = new Skin(Gdx.files.internal("hakd/gui/resources/skins/uiskin.json"));

        font = skin.getFont("default-font");
        consoleFont = skin.getFont("console-font");
        fontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        consoleFontColor = new Color(0.0f, 0.7f, 0.0f, 1.0f);
        font.setColor(fontColor);
        consoleFont.setColor(consoleFontColor);

        font.setScale(1f);
        consoleFont.setScale(.6f);

        skin.getFont("console-font-0").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        skin.getAtlas().findRegion("default").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        generateShaders();
    }

    private static void generateShaders() {
        shaders = new HashMap<Shader, ShaderProgram>();

        for (Shader s : Shader.values()) {
            if (s != Shader.GDX_DEFAULT) {
                FileHandle vert = Gdx.files.internal("hakd/gui/resources/shaders/" + s.name().toLowerCase() + ".vert");
                FileHandle frag = Gdx.files.internal("hakd/gui/resources/shaders/" + s.name().toLowerCase() + ".frag");
                ShaderProgram shader = new ShaderProgram(vert, frag);
                shaders.put(s, shader);

                if (shader.getLog().length() != 0) {
                    Gdx.app.log("Shader Log", shader.getLog());
                }
            }
        }

        shaders.put(Shader.GDX_DEFAULT, SpriteBatch.createDefaultShader());
    }

    public enum Shader {
        GDX_DEFAULT, DEFAULT, TERRITORY;
    }

}
