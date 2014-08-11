package gui.windows.dialogs.fileops;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import game.Hakd;
import gui.windows.device.FileHandler;
import networks.devices.Device;
import other.Util;

public class FileClose extends Dialog {


    public FileClose(final Device device, final FileHandler fileHandler) {
        super("Close", Hakd.assets.get("skins/uiskin.json", Skin.class));

        Skin skin = Hakd.assets.get("skins/uiskin.json", Skin.class);

        text("This file has been modified.\nAre you sure you want to quit?");

        Button save = new TextButton("Save", skin);
        final Button close = new TextButton("Close Without Saving", skin);
        Button cancel = new TextButton("Cancel", skin);

        button(save);
        button(close);
        button(cancel);

        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (fileHandler.getFile() != null) {
                    fileHandler.saveAsFile(fileHandler.getFile().getParentDirectory(), fileHandler.getFile().getName());
                    fileHandler.close();
                } else {
                    FileSave save = new FileSave(device, fileHandler);
                    save.show(getStage());
                }
            }
        });
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                fileHandler.close();
            }
        });
    }

    public enum Action {
        SAVE, CLOSE, CANCEL
    }

}
