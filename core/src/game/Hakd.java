package game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gui.screens.MenuScreen;
import other.HakdFileHandleResolver;
import other.Util;

public class Hakd extends Game {
    public static Hakd HAKD;

    private Preferences prefs;
    // private static Preferences save1; // save this for later // Use kryo

    private GamePlay gamePlay;

    public static final AssetManager assets = new AssetManager(new HakdFileHandleResolver());

    public Hakd() {
        HAKD = this;
    }

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("hakd-prefs");

        if (!prefs.getBoolean("played-before")) {
            newPrefs();
        }

        int width = prefs.getInteger("width");
        int height = prefs.getInteger("height");
        boolean fullscreen = prefs.getBoolean("fullscreen");
        boolean vsync = prefs.getBoolean("vsync");

        Gdx.graphics.setDisplayMode(width, height, fullscreen);
        Gdx.graphics.setVSync(vsync); // because no one needs to render 4000 frames per second, but then again it keeps the room warm

        Gdx.app.setLogLevel(prefs.getInteger("log-level")); // TODO save a copy of the console to a log file

        loadAssets();

        setScreen(new MenuScreen(this));
    }

    private void loadAssets() {
        assets.load("nTextures.txt", TextureAtlas.class);
        assets.load("lTextures.txt", TextureAtlas.class);
        assets.load("skins/uiskin.json", Skin.class);
        assets.load("skins/font/Fonts_7.fnt", BitmapFont.class); // text font
        assets.load("skins/font/Fonts_0.fnt", BitmapFont.class); // console font
        assets.finishLoading();

        BitmapFont textFont = assets.get("skins/font/Fonts_7.fnt", BitmapFont.class);
        textFont.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        textFont.setScale(.6f);

        BitmapFont consoleFont = assets.get("skins/font/Fonts_0.fnt", BitmapFont.class);
        consoleFont.setColor(new Color(0.0f, 0.7f, 0.0f, 1.0f));
        consoleFont.setScale(.6f);

        // set texture filters
        assets.get("nTextures.txt", TextureAtlas.class).findRegion("black").getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        consoleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        assets.get("skins/uiskin.json", Skin.class).getAtlas().findRegion("default").getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void newPrefs() {
    /*
	 * the directory for these can't be changes, on linux it is /home/[user
	 * name]/.prefs/, on windows it is users/[user name]/.prefs/, I have not
	 * tested mac yet
	 */

        prefs.putBoolean("played-before", false);
        prefs.putInteger("width", 800);
        prefs.putInteger("height", 600);
        prefs.putBoolean("fullscreen", false);
        prefs.putBoolean("vsync", true);
        prefs.putBoolean("sound", true);
        prefs.putInteger("log-level", Application.LOG_DEBUG);

//        prefs.flush();
        // unfortunately this makes this game non-portable, kind of

        // TODO ask user if they want the game to save anything, including settings and game save. Maybe ask in the settings and default to no saving
    }

    @Override
    public void dispose() {
        super.dispose();

        if (gamePlay != null) {
            gamePlay.dispose();
        }

        assets.dispose();
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(GamePlay gamePlay) {
        this.gamePlay = gamePlay;
    }
}
