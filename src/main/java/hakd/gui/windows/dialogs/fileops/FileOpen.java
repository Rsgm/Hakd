package hakd.gui.windows.dialogs.fileops;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hakd.game.Hakd;
import hakd.gui.windows.device.FileHandler;
import hakd.networks.devices.Device;
import hakd.other.File;

public class FileOpen extends Dialog {
    Skin skin = Hakd.assets.get("skins/uiskin.json", Skin.class);

    public FileOpen(final Device device, final FileHandler fileHandler) {
        super("Open", Hakd.assets.get("skins/uiskin.json", Skin.class));

        final Tree fileTree = new Tree(skin);
        fileTree.getSelection().setMultiple(false);
        fileTree.add(AddFilesToTree(device.getRoot()));

        text("Select a file to open");
        getContentTable().row();
        getContentTable().add(new ScrollPane(fileTree, skin)).size(300, 200).fill();

        Button open = new TextButton("Open", skin);
        Button cancel = new TextButton("Cancel", skin);
        button(open);
        button(cancel);

        open.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (fileHandler.isModified()) {
                    FileClose closeDialog = new FileClose(device, fileHandler);
                    closeDialog.show(getStage());
                }

                if (fileTree.getSelection().isEmpty() || ((File) fileTree.getSelection().first().getObject()).isDirectory()) {
                    return;
                }

                fileHandler.openFile((File) fileTree.getSelection().first().getObject());
            }
        });
    }

    private Tree.Node AddFilesToTree(File file) {
        Tree.Node parentNode = new Tree.Node(new Label(file.getName(), skin));
        parentNode.setObject(file);

        if (file.isDirectory()) {
            for (File f : file.getFileMap().values()) {
                if (f.isDirectory()) {
                    parentNode.add(AddFilesToTree(f));
                } else {
                    Tree.Node n = new Tree.Node(new Label(f.getName(), skin));
                    n.setObject(f);
                    parentNode.add(n);
                }
            }
        } else {
            Tree.Node n = new Tree.Node(new Label(file.getName(), skin));
            n.setObject(file);
            parentNode.add(n);
        }
        return parentNode;
    }
}
