package hakd;

import hakd.game.Hakd;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public final class Main {

    public static void main(String[] args) {
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.resizable = false;
	config.title = "Hak'd";
	new LwjglApplication(new Hakd(), config);
    }
}
