package hakd.gui.windows.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import hakd.gui.Assets;

public class EmptyDeviceDialog extends Dialog {
    public EmptyDeviceDialog() {
        super("Empty Device", Assets.skin);
        Skin skin = Assets.skin;

        text("This is a place to put a server.");
        text("You can buy a new server from the online store.");
        button(new TextButton("Close", skin));
    }
}
