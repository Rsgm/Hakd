package hakd.gui.windows.deviceapps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hakd.game.Command;
import hakd.gui.Assets;
import hakd.networks.devices.Device;
import hakd.other.File;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Terminal implements ServerWindow {
    private final ServerWindowStage window;

    private final Table table;

    private final TextField input;
    private final Label display;
    private final ScrollPane scroll;

    private final List<String> history; // command history
    private int line = 0; // holds the position of the history
    private final Device device;
    private Command command;
    private File directory;
    private final Queue<Character> userInputBuffer;
    private String userInputString;

    public Terminal(ServerWindowStage w) {
        window = w;
        device = window.getDevice();
        directory = device.getHome();

        Skin skin = Assets.skin;
        history = new ArrayList<String>();

        table = new com.badlogic.gdx.scenes.scene2d.ui.Window("Terminal", skin);
        table.setSize(window.getCanvas().getWidth() * .9f, window.getCanvas().getHeight() * .9f);

        ImageButton close = new ImageButton(new TextureRegionDrawable(Assets.linearTextures.findRegion("close")));
        close.setPosition(table.getWidth() - close.getWidth(), table.getHeight() - close.getHeight() - 20);

        input = new TextField("", skin.get("console", TextFieldStyle.class));
        display = new Label("", skin.get("console", LabelStyle.class));
        scroll = new ScrollPane(display, skin);

        display.setWrap(false); // this being true would mess up text line insertion
        display.setAlignment(10, Align.left);
        display.setText("Terminal [Version 0." + ((int) (Math.random() * 100)) / 10 + "]" + "\nroot @ " + device.getIp() + "\nMemory: " + device.getMemoryCapacity() + "MB\nStorage: " + device.getStorageCapacity() + "GB");

        userInputBuffer = new ConcurrentLinkedQueue<Character>();

        table.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // touch up will not work without this returning true
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (y >= table.getHeight() - 20) {
                    if (table.getX() < 0) {
                        table.setX(0);
                    }
                    if (table.getY() < 0) {
                        table.setY(0);
                    }
                    if (table.getX() + table.getWidth() > Gdx.graphics.getWidth()) {
                        table.setX(Gdx.graphics.getWidth() - table.getWidth());
                    }
                    if (table.getY() + table.getHeight() > Gdx.graphics.getHeight()) {
                        table.setY(Gdx.graphics.getHeight() - table.getHeight());
                    }
                }
            }
        });

        close.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                close();
            }
        });

        input.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER && command == null) {
                    display.setText(display.getText() + "\n\nroot @ " + device.getIp() + "\n>" + input.getText());
                    history.add(input.getText());
                    command = new Command(input.getText(), device, Terminal.this);

                    line = history.size();
                    input.setText("");
                } else if (keycode == Keys.C && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && command != null) {
                    addText("Program Stopped");
                    command.stop();
                } else if (keycode == Keys.TAB && command == null) {
                    tab(); // this was getting way too long
                } else if (keycode == Keys.DOWN && line < history.size() - 1) {
                    line++;
                    input.setText(history.get(line));
                    input.setCursorPosition(input.getText().length());
                } else if (keycode == Keys.UP && line > 0) {
                    line--;
                    input.setText(history.get(line));
                    input.setCursorPosition(input.getText().length());
                } else if (command != null) {
                    if (keycode == Keys.ENTER) {
                        userInputString = input.getText();
                        input.setText("");
                    }
                    userInputBuffer.offer(event.getCharacter());
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER || (keycode == Keys.C && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && command != null)) {
                    scroll.setScrollY(scroll.getMaxY());
                }
                return super.keyUp(event, keycode);
            }
        });

        table.add(scroll).expand().fill();
        table.row();
        table.add(input).left().fillX();

        table.addActor(close);
    }

    public void addText(String text) {
        display.setText(display.getText() + "\n   " + text);
        scroll.setScrollY(scroll.getMaxY());
        // I doubt that set text is thread safe, although only two threads, that
        // I know of, can access it; the main and command threads.
    }

    public void replaceText(String newText, int lineFromBottom) {
        String t = display.getText().toString();
        int n = t.length();
        int m = n;
        lineFromBottom++;

        if (lineFromBottom < 1) {
            return;
        }

        for (int i = 0; i < lineFromBottom; i++) {
            m = n;
            n = t.lastIndexOf("\n", n - 2);
        }

        String s = t.substring(0, n);
        s += "\n   " + newText;
        s += t.substring(m, t.length());

        display.setText(s);
        scroll.setScrollY(scroll.getMaxY());
    }

    /**
     * Tab completion.
     */
    private void tab() {
        File tempDirectory = directory;
        List<String> parameters = new ArrayList<String>();
        int currentParameter = 0;
        int cursorPosition = input.getCursorPosition();

        // parse input into parameters of text and space
        if (!input.getText().isEmpty()) {
            boolean isSpace = input.getText().charAt(0) == ' ';
            String tempString = "";
            for (int i = 0; i < input.getText().length(); i++) {
                if (isSpace != (input.getText().charAt(i) == ' ')) {
                    parameters.add(tempString);
                    tempString = "";
                    isSpace = !isSpace;
                }

                tempString += input.getText().charAt(i);
            }
            parameters.add(tempString);

            for (String p : parameters) {
                if (cursorPosition <= p.length() && p.matches("\\S+")) {
                    currentParameter = parameters.indexOf(p);
                    break;
                }

                cursorPosition -= p.length();
            }

            if (currentParameter == 0 || cursorPosition <= 0) {
                cursorPosition = input.getCursorPosition();
                for (String p : parameters) {
                    if (cursorPosition <= p.length()) {
                        currentParameter = parameters.indexOf(p);
                        break;
                    }

                    cursorPosition -= p.length();
                }
            }
        } else {
            parameters.add("");
        }
        String currentParameterText = parameters.get(currentParameter);

        // arrays to hold possible options of text completion
        Set<File> files = new HashSet<File>();
        Set<File> filesCopy = new HashSet<File>();

        String completedText = ""; // make a string containing the text of the current parameter, before the cursor, to manipulate into the finished parameter
        if (!currentParameterText.matches("^\\s+$")) {
            completedText = currentParameterText;
            completedText = completedText.substring(0, cursorPosition);
        }

        // fill the arrays with executable files in /bin as well as all the files in the current directory
        files.addAll(device.getBin().getRecursiveFileList(device.getBin()));
        if (currentParameter > 0) {
            files.addAll(directory.getFileMap().values());
        }

        // exclude files that don't start with the current parameter before the cursor
        filesCopy.addAll(files);
        for (hakd.other.File f : filesCopy) {
            if (!f.getName().startsWith(completedText)) {
                files.remove(f);
            }
        }

        int totalLength = 0;
        File tempFile = (File) files.toArray()[0]; // I am running out of names
        if (files.size() > 1) { // show the list of possible options and set the manipulated parameter to the common beginning text of that list
            addText("");
            for (File f : files) {
                String availableFiles = f.getName();
                if (f.isDirectory()) {
                    availableFiles += "/";
                }
                addText(availableFiles);
            }

            if (currentParameterText.matches("^\\s+$")) {
                return;
            }

            int lastSameCharacter = 0; // cut the loops down by setting this to cursorposition, but I don't want to risk creating bugs
            l1:
            for (int i = 0; i < tempFile.getName().length(); i++) {
                char character = tempFile.getName().charAt(i);
                for (File f : files) {
                    if (f.getName().charAt(i) != character) {
                        lastSameCharacter = i;
                        break l1;
                    }
                }
            }
            completedText = tempFile.getName().substring(0, lastSameCharacter) + currentParameterText.substring(cursorPosition);
            parameters.set(currentParameter, completedText);
            totalLength = lastSameCharacter;
        } else if (files.size() == 1) { // set the manipulated text to this parameter, adding any extra text after the cursor to the end
            if (cursorPosition == tempFile.getName().length()) {
                return;
            }

            completedText = tempFile.getName();
            totalLength = completedText.length() + 1;
            completedText += " " + currentParameterText.substring(cursorPosition);
            parameters.set(currentParameter, completedText);
        } else { // there are no matches
            return;
        }

        // fill a string with each parameter to make the new input text
        String s = "";
        for (String parameter : parameters) {
            s += parameter;
        }

        // find the total length up to the end of the new parameter, and set the cursor to it
        for (int i = 0; i < currentParameter; i++) {
            totalLength += parameters.get(i).length();
        }

        input.setText(s);
        input.setCursorPosition(totalLength);
        directory = tempDirectory;
    }

    public String input(final String display) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                addText(display);
            }
        });

        userInputString = null;
        while (userInputString == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String s = userInputString;
        userInputString = null;
        return s;
    }

    @Override
    public void open() {
        window.getCanvas().addActor(table);
    }

    @Override
    public void close() {
        window.getCanvas().removeActor(table);
    }

    public Label getDisplay() {
        return display;
    }

    public ScrollPane getScroll() {
        return scroll;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Device getDevice() {
        return device;
    }

    public File getDirectory() {
        return directory;
    }

}
