package hakd.gui.windows.device;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hakd.gui.windows.dialogs.fileops.FileClose;
import hakd.gui.windows.dialogs.fileops.FileOpen;
import hakd.gui.windows.dialogs.fileops.FileSave;
import hakd.other.File;

public class TextEdit extends SceneWindow implements FileHandler {
    private final TextArea display;
    private final Slider scroll;
    private final Table buttonTable;

    private final Button open;
    private final Button save;
    private final Button close;

    private File file;

    public TextEdit(GameScene scene) {
        super(scene);
        window.setTitle("TextEdit");

        buttonTable = new Table(skin);
        display = new TextArea("", skin.get("text-edit", TextField.TextFieldStyle.class));
        scroll = new Slider(1, 1, 1, true, skin);

        open = new TextButton("Open", skin);
        save = new TextButton("Save", skin);
        close = new TextButton("Close", skin);

        buttonTable.add(open, save, close);

        window.add(buttonTable).left();
        window.row();
        window.add(display).expand().fill();
        window.add(scroll).expandY().fill();

        open.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                FileOpen open = new FileOpen(device, TextEdit.this);
                open.show(TextEdit.this.scene.getStage());
            }
        });
        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (file == null && isModified()) {
                    FileSave save = new FileSave(device, TextEdit.this);
                    save.show(TextEdit.this.scene.getStage());
                } else {
                    save();
                }

            }
        });
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (isModified()) {
                    FileClose close = new FileClose(device, TextEdit.this);
                    close.show(TextEdit.this.scene.getStage());
                } else {
                    close();
                }
            }
        });
        display.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                isModified();
                updateScroll();
                return true;
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                display.moveCursorLine((int) (scroll.getMaxValue() - scroll.getValue()));
                return true;
            }
        });
        scroll.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                display.moveCursorLine((int) (scroll.getMaxValue() - scroll.getValue()));
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                display.moveCursorLine((int) (scroll.getMaxValue() - scroll.getValue()));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                display.moveCursorLine((int) (scroll.getMaxValue() - scroll.getValue()));
            }
        });
    }

    private void updateScroll() {
        int max = display.getLines() - (int) (display.getLinesShowing() / 2f); // rounding should not matter too much
        max = max < 1 ? 1 : max;

        scroll.setRange(1, max);
        scroll.setStepSize(1);
        scroll.setValue(display.getCursorLine() < 1 ? 1 : display.getCursorLine());
    }

    @Override
    public void openFile(File file) {
        if (file != null) {
            this.file = file;
            display.setText(file.getData());
        }
    }

    @Override
    public boolean saveAsFile(File directory, String name) {
        file = directory.getFile(name);

        if (file == null) {
            return false;
        } else {
            file.setData(display.getText());
            return true;
        }
    }

    @Override
    public void save() {
        if (file == null) {
            return;
        }

        file.setData(display.getText());
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public boolean isModified() {
        if (file != null) {
            if (file.getData().equals(display.getText())) {
                window.setTitle("TextEdit " + file.getName());
                return false;
            } else {
                window.setTitle("TextEdit " + file.getName() + " *");
                return true;
            }
        } else {
            if (display.getText().isEmpty()) {
                window.setTitle("TextEdit ");
                return false;
            } else {
                window.setTitle("TextEdit " + " *");
                return true;
            }
        }
    }

    @Override
    public void close() {
        super.close();

        display.setText("");
        file = null;
    }
}
