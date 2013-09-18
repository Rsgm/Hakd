package hakd.game.pythonapi;

import com.badlogic.gdx.Gdx;
import hakd.gui.windows.server.Terminal;

public class PyDisplay {

	public static void write(final Terminal t, final String s) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				t.addText(s);
			}
		});
	}

	public static void rewrite(Terminal t, String s, int lineFromBottom) {
		t.replaceText(s, lineFromBottom);
	}

}
