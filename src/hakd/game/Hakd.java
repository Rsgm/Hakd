package hakd.game;

import hakd.Main;
import hakd.gui.screens.TitleScreen;
import hakd.other.enumerations.Prefs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Hakd extends Game {
	private int		width;
	private int		height;

	private boolean	fullscreen;
	private boolean	vsync;

	@Override
	public void create() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		fullscreen = Main.prefs.get(Prefs.FULLSCREEN.line).equals("true");
		vsync = Main.prefs.get(Prefs.VSYNC.line).equals("true");

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
