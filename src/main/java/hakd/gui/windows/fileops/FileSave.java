package hakd.gui.windows.fileops;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hakd.gui.Assets;
import hakd.gui.windows.deviceapps.FileHandler;
import hakd.networks.devices.Device;
import hakd.other.File;

public class FileSave extends Dialog {
    private final Skin skin = Assets.skin;

    public FileSave(Device device, final FileHandler fileHandler) {
        super("Save File As", Assets.skin);

        final Tree fileTree = new Tree(skin);
        final TextField fileName = new TextField("", skin); // holds the name, not the path
        Button save = new TextButton("Save", skin);
        Button cancel = new TextButton("Cancel", skin);

        fileTree.setMultiSelect(false);
        fileTree.add(AddFilesToTree(device.getRoot()));

        getButtonTable().add(fileName);
        button(save);
        button(cancel);

        text("Select a place to save the file");
        getContentTable().row();
        getContentTable().add(new ScrollPane(fileTree, skin)).height(200).expandX().fill();

        System.out.println(getContentTable().getWidth() + "   " + getContentTable().getHeight());

        fileTree.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (fileTree.getSelection().size == 0) {
                    return;
                }

                File file = (File) fileTree.getSelection().get(0).getObject();
                if (file != null && !file.isDirectory()) {
                    System.out.println(file.getPath());
                    fileName.setText(file.getName()); // there is an error with getting root dir from its path
                }
            }
        });
        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (fileTree.getSelection().size == 0 || fileName.getText().isEmpty()) {
                    FileSave.this.cancel();
                }

                Tree.Node selection = fileTree.getSelection().get(0);
                File dir;
                if (((File) selection.getObject()).isDirectory()) {
                    dir = (File) selection.getObject();
                } else {
                    dir = (File) selection.getParent().getObject();
                }

                fileHandler.saveAsFile(dir, fileName.getText());
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
                    Label label = new Label(f.getName(), skin);
                    Tree.Node n = new Tree.Node(label);
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
