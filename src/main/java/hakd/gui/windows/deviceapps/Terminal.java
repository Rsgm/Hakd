package hakd.gui.windows.deviceapps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hakd.game.Command;
import hakd.gui.Assets;
import hakd.networks.devices.Device;
import hakd.other.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Terminal extends SceneWindow {
    private final TextField input;
    private final Label display;
    private final ScrollPane scroll;

    private final List<String> history; // command history
    private int line = 0; // holds the position of the history
    private final Device device;
    private Queue<Command> commandQueue;
    private File directory;

    private final Queue<Character> inputQueue;
    private final InputStream inputStream;
    private Thread commandThread;

    public Terminal(GameScene scene) {
        super(scene);
        device = this.scene.getDevice();
        directory = device.getHome();

        commandQueue = new ConcurrentLinkedQueue<Command>(); // not sure if this should be concurrent, it might need to be in the future
        history = new ArrayList<String>();

        ImageButton close = new ImageButton(new TextureRegionDrawable(Assets.linearTextures.findRegion("close")));
        close.setPosition(window.getWidth() - close.getWidth(), window.getHeight() - close.getHeight() - 20);

        input = new TextField("", skin.get("console", TextFieldStyle.class));
        display = new Label("", skin.get("console", LabelStyle.class));
        scroll = new ScrollPane(display, skin);

        display.setWrap(false); // this being true would mess up text line insertion
        display.setAlignment(10, Align.left);
        display.setText("Terminal [Version 0." + ((int) (Math.random() * 100)) / 10 + "]" + "\nroot @ " + device.getIp() + "\nMemory: " + device.getMemoryCapacity() + "MB\nStorage: " + device.getStorageCapacity() + "GB");
        input.setFocusTraversal(false);

        window.add(scroll).expand().fill();
        window.row();
        window.add(input).left().fillX();
        window.addActor(close);

        inputQueue = new ConcurrentLinkedQueue<Character>();
        inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                while (inputQueue.isEmpty()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Gdx.app.error("Thread Sleep", "Insomnia", e);
                    }
                }

                if (!inputQueue.isEmpty()) {
                    return inputQueue.poll();
                } else {
                    return '\n';
                }
            }
        };


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
                if (keycode == Keys.ENTER && commandQueue.isEmpty()) {
                    addTextln("\nroot @ " + device.getIp() + " : " + directory.getPath() + "\n$ " + input.getText());
                    history.add(input.getText());
                    command();

                    line = history.size();
                    input.setText("");
                } else if (keycode == Keys.C && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && !commandQueue.isEmpty()) {
                    addTextln("Program Stopped");
                    stop();
                } else if (keycode == Keys.TAB && commandQueue.isEmpty()) {
                    tab(); // this was getting way too long
                } else if (keycode == Keys.DOWN && line < history.size() - 1) {
                    line++;
                    input.setText(history.get(line));
                    input.setCursorPosition(input.getText().length());
                } else if (keycode == Keys.UP && line > 0) {
                    line--;
                    input.setText(history.get(line));
                    input.setCursorPosition(input.getText().length());
                } else if (!commandQueue.isEmpty()) {
                    if (keycode == Keys.ENTER) {
                        input.setText("");
                    }
                    inputQueue.offer(event.getCharacter());
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER || (keycode == Keys.C && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && !commandQueue.isEmpty())) {
                    scroll.setScrollY(scroll.getMaxY());
                }
                return true;
            }
        });

        display.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                super.keyDown(event, keycode);

                if (!commandQueue.isEmpty()) {
                    if (keycode == Keys.ENTER) {
                        input.setText("");
                    }
                    inputQueue.offer(event.getCharacter());
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                super.keyUp(event, keycode);
                return true;
            }
        });
    }

    private void command() {
        String s = input.getText() + ";";
        String inputString = input.getText();

        Scanner scanner = new Scanner(s);
        String next;
        while (true) {
            next = scanner.findInLine(".*?[|&;]{1,2}");
            if (next == null) {
                break;
            }

            Command.Operation operation = Command.Operation.EITHER;
            if (next.endsWith("|")) {
                operation = Command.Operation.PIPE;
            } else if (next.endsWith("&&")) {
                operation = Command.Operation.SUCCESSFUL;
            } else if (next.endsWith("||")) {
                operation = Command.Operation.NOT_SUCCESSFUL;
            } else if (next.endsWith(";")) {
                operation = Command.Operation.EITHER;
            }

            next = next.replaceAll("[|&;]{1,2}", ""); // is this assignment needed? does replace all need to be assigned to the string it is called on?
            inputString = inputString.substring(next.length());

            Command c = new Command(next, Terminal.this, operation);
            commandQueue.offer(c);
        }

        commandThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean successful = false;

                List<String> previousOutput = null;
                while (!commandQueue.isEmpty()) {
                    Command c = commandQueue.poll();
                    Command.Operation operation = c.getOperation();
                    c.setPipedInput(previousOutput);

                    if (operation == Command.Operation.PIPE) { // assumes successful
                        successful = c.run();
                    } else if (operation == Command.Operation.EITHER) {
                        successful = c.run();
                    } else if (operation == Command.Operation.SUCCESSFUL && successful) {
                        successful = c.run();
                    } else if (operation == Command.Operation.NOT_SUCCESSFUL && !successful) {
                        successful = c.run();
                    }

                    previousOutput = c.getPipedOutput();
                }
            }
        });
        commandThread.start();
    }

    public void addTextln(final String text) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                display.setText(display.getText() + text + "\n   ");
                scroll.setScrollY(scroll.getMaxY());
            }
        });
    }

    public void addText(final char c) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (c == '\n') {

                    display.setText(display.getText() + "\n   ");
                } else {
                    display.setText(display.getText() + "" + c);
                }
                scroll.setScrollY(scroll.getMaxY());
            }
        });
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

        final String text = s;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                display.setText(display.getText() + "\n   " + text);
                scroll.setScrollY(scroll.getMaxY());
            }
        });
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

        if (files.isEmpty()) {
            return;
        }

        int totalLength;
        File tempFile = (File) files.toArray()[0]; // I am running out of names
        if (files.size() > 1) { // show the list of possible options and set the manipulated parameter to the common beginning text of that list
            addTextln("");
            for (File f : files) {
                String availableFiles = f.getName();
                if (f.isDirectory()) {
                    availableFiles += "/";
                }
                addTextln(availableFiles);
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

    /**
     * Stops the command thread. This uses the deprecated Thread.stop(), which means it
     * will stop it no matter what. Normally this is very bad to do because it
     * will be in the middle of something. This is realistic so I will leave it,
     * there is also no better way.
     */
    @SuppressWarnings("deprecation")
    public void stop() {
//        commandThread.interrupt();
//        if (!commandThread.isInterrupted() || commandThread.isAlive()) {
        commandThread.stop();
//        }

        commandQueue.clear();
    }

    public Label getDisplay() {
        return display;
    }

    public Queue<Command> getCommandQueue() {
        return commandQueue;
    }

    public void setCommandQueue(Queue<Command> commandQueue) {
        this.commandQueue = commandQueue;
    }

    public Thread getCommandThread() {
        return commandThread;
    }

    public void setCommandThread(Thread commandThread) {
        this.commandThread = commandThread;
    }

    public Queue<Character> getInputQueue() {
        return inputQueue;
    }

    public List<String> getHistory() {
        return history;
    }

    public Device getDevice() {
        return device;
    }

    public File getDirectory() {
        return directory;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
