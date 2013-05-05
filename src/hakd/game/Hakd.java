package hakd.game;

import hakd.gui.screens.TitleScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Hakd extends Game {
	private int		width;
	private int		height;

	private boolean	fullscreen;
	private boolean	vsync;

	@Override
	public void create() {
		Preferences prefs = Gdx.app.getPreferences("prefs");

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		fullscreen = prefs.getBoolean("fullscreen");
		vsync = prefs.getBoolean("vsync");

		Gdx.graphics.setDisplayMode(width, height, fullscreen);
		Gdx.graphics.setVSync(vsync); // because no one needs to render 4000 frames per second, but then again it keeps the room warm

		setScreen(new TitleScreen(this));
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

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public boolean isVsync() {
		return vsync;
	}

	public void setVsync(boolean vsync) {
		this.vsync = vsync;
	}
}
