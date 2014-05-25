package hakd;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hakd.game.Hakd;

public class Main {
    public static final Hakd HAKD = new Hakd();

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.samples = 8; // can go to 16 or 32 I think
        config.title = "Hak'd";
        new LwjglApplication(HAKD, config);
    }
}
