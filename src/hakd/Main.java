package hakd;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hakd.game.Hakd;

public final class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.samples = 16;
        config.useGL20 = true;
        config.title = "Hak'd";
        new LwjglApplication(new Hakd(), config);
    }
}
