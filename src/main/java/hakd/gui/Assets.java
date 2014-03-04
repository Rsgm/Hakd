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
import com.badlogic.gdx.utils.Disposable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

public final class Assets {
    public static TextureAtlas nearestTextures;
    public static TextureAtlas linearTextures;

    public static Skin skin;

    public static BitmapFont textFont;
    public static BitmapFont consoleFont;
    public static Color fontColor;
    public static Color consoleFontColor;

    public static HashMap<Shader, ShaderProgram> shaders;

    public static void load() {
        nearestTextures = new TextureAtlas(Gdx.files.internal("nTextures.txt"));
        linearTextures = new TextureAtlas(Gdx.files.internal("lTextures.txt"));

        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        textFont = skin.getFont("text-font");
        fontColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        textFont.setColor(fontColor);
        textFont.setScale(.6f);

        consoleFont = skin.getFont("console-font");
        consoleFontColor = new Color(0.0f, 0.7f, 0.0f, 1.0f);
        consoleFont.setColor(consoleFontColor);
        consoleFont.setScale(.6f);

        // I used black because it is a texture that will not change
        nearestTextures.findRegion("black").getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        textFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        consoleFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        skin.getAtlas().findRegion("default").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        generateShaders();
    }

    private static void generateShaders() {
        shaders = new HashMap<Shader, ShaderProgram>();

        for (Shader s : Shader.values()) {
            if (s != Shader.GDX_DEFAULT) {
                FileHandle vert = Gdx.files.internal("shaders/" + s.name().toLowerCase() + ".vert");
                FileHandle frag = Gdx.files.internal("shaders/" + s.name().toLowerCase() + ".frag");
                ShaderProgram shader = new ShaderProgram(vert, frag);
                shaders.put(s, shader);

                if (shader.getLog().length() != 0) {
                    Gdx.app.log("Shader Log", shader.getLog());
                }
            }
        }

        shaders.put(Shader.GDX_DEFAULT, SpriteBatch.createDefaultShader());
    }

    public static void dispose() {
        Field[] Fields = Assets.class.getFields();
        for (Field f : Fields) {
            if (Arrays.asList(f.getType().getGenericInterfaces()).contains(Disposable.class)) {
                try {
                    ((Disposable) f.get(Disposable.class)).dispose();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        for (ShaderProgram s : shaders.values()) {
            s.dispose();
        }
    }

    public enum Shader {
        GDX_DEFAULT, DEFAULT, TERRITORY
    }
}
