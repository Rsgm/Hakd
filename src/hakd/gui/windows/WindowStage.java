package hakd.gui.windows;

import hakd.gui.screens.GameScreen;

public interface WindowStage {
	public void render();

	public void open();

	public void close();

	public void setScreen(GameScreen screen);
}
