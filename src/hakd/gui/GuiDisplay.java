package hakd.gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GuiDisplay { // this should have a better name but display is taken

	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		while (!Display.isCloseRequested()) {
			// render OpenGL here
			Display.update();
		}

		Display.destroy();
	}

}
