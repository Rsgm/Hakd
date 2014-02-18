package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import hakd.gui.Assets;
import hakd.other.Util;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class MenuScreen extends HakdScreen {
    private final Stage stage;
    private final Table canvas;

    private TextField input;
    private Label display;
    private ScrollPane scroll;

    private boolean firstTab = true;
    private String tabString = "";
    private int tabIndex;
    private List<String> history; // command history
    private int line = 0; // holds the position of the history
    private boolean commandRunning = false;

    private Thread t;

    public MenuScreen(Game game) {
        super(game);

        cam = new OrthographicCamera();
        ((OrthographicCamera) cam).setToOrtho(false, width, height);
        cam.update();

        stage = new Stage();
        stage.getSpriteBatch().setShader(Assets.shaders.get(Assets.Shader.DEFAULT));
        canvas = new Table();
        stage.addActor(canvas);
        canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        super.show();

        Gdx.input.setInputProcessor(stage);
        stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        StartTerminal();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (commandRunning) {
            stop();
        }
    }

    private void StartTerminal() {
        Skin skin = Assets.skin;
        history = new ArrayList<String>();

        input = new TextField("", skin.get("console", TextField.TextFieldStyle.class));
        display = new Label("", skin.get("console", Label.LabelStyle.class));
        scroll = new ScrollPane(display, skin);

        display.setWrap(false);
        display.setAlignment(10, Align.left);
        String terminalInfo = "Terminal [Version 0." + ((int) (Math.random() * 100)) / 10 + "]";
        terminalInfo += "\n" + System.getProperty("user.name") + "@127.0.0.1";
        terminalInfo += "\nStorage:";

        for (int i = 0; i < File.listRoots().length; i++) {
            terminalInfo += "\n    Drive[" + i + "]  " + (-File.listRoots()[i].getFreeSpace() + File.listRoots()[i].getTotalSpace()) / 1000000000 + "GB Used,  " + File.listRoots()[i].getTotalSpace() / 1000000000 + "GB Total";
        }

        display.setText(terminalInfo + "\n-----------------------------------------------------");
        input.setMessageText("Type 'help' here to get started.");

        input.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (commandRunning) {
                    line = history.size();
                    input.setText("Press Control and C to cancel");
                }
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER && !commandRunning) {
                    display.setText(display.getText() + "\n\n" + System.getProperty("user.name") + " @ 127.0.0.1" + " : ~" + "\n$ " + input.getText());
                    history.add(input.getText());
                    Command(input.getText());
                    commandRunning = true;
                    line = history.size();
                    input.setText("");
                } else if (keycode == Input.Keys.C && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) && commandRunning) {
                    addText("Program Stopped");
                    stop();
                } else if (keycode == Input.Keys.TAB && !commandRunning) {
                    String s = "<";
                    File[] files = new File(Util.RESOURCES.getPath() + "/python/menu/").listFiles();
                    List<File> filesFiltered = new ArrayList<File>();

                    if (firstTab) {
                        tabString = input.getText();
                    }

                    if (files != null) {
                        for (File f : files) {
                            if (f.getName().startsWith(tabString) && f.getName().endsWith(".py")) {
                                filesFiltered.add(f);
                            }
                        }
                    }

                    for (File f : filesFiltered) {
                        s += f.getName().substring(0, f.getName().length() - 3);
                        if (filesFiltered.lastIndexOf(f) != filesFiltered.size() - 1) {
                            s += ", ";
                        }
                    }
                    if (!filesFiltered.isEmpty()) {
                        if (firstTab) {
                            addText(s + ">");
                            firstTab = false;
                        }

                        String name = filesFiltered.get(tabIndex).getName();
                        input.setText(name.substring(0, name.length() - 3));
                        input.setCursorPosition(input.getText().length());
                        // TODO change this to insert text for completion of parameters, instead of overwriting the input box
                    } else {
                        addText("<There are no programs with that name>");
                    }

                    tabIndex++;
                    if (tabIndex >= filesFiltered.size()) {
                        tabIndex = 0;
                    }
                } else if (keycode == Input.Keys.DOWN && line < history.size() - 1) {
                    line++;
                    input.setText(history.get(line));
                    input.setCursorPosition(input.getText().length());
                } else if (keycode == Input.Keys.UP && line > 0) {
                    line--;
                    input.setText(history.get(line));
                    input.setCursorPosition(input.getText().length());
                }

                if (keycode != Input.Keys.TAB && keycode != Input.Keys.LEFT && keycode != Input.Keys.RIGHT) {
                    tabIndex = 0;
                    tabString = "";
                    firstTab = true;
                }

                //				if(commandRunning != null) {
                //					userInputBuffer().offer(keycode); TODO
                //				}

                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER || (keycode == Input.Keys.C && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) && commandRunning)) {
                    scroll.setScrollY(display.getHeight());
                }
                return super.keyUp(event, keycode);
            }
        });

        canvas.add(scroll).expand().fill();
        canvas.row();
        canvas.add(input).left().fillX();
    }

    public void addText(String text) {
        display.setText(display.getText() + "\n   " + text);
        scroll.setScrollY(display.getHeight());
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
        scroll.setScrollY(display.getHeight());
    }

    private void Command(String s) {
        run(s);
    }

    private void run(final String s) {
        t = new Thread(new Runnable() {
            String in = s;

            @Override
            public void run() {
                List<String> parameters = new ArrayList<String>();

                while (in.matches("\\s*[(?:\".*?\")|\\S+].*")) {
                    if (in.startsWith(" ")) {
                        in = in.replaceFirst("\\s+", "");
                    }

                    String inTemp = in;
                    in = in.replaceFirst("(?:\".*?\")|\\S+", "");
                    int l = in.length();

                    String next = inTemp.substring(0, inTemp.length() - l);
                    parameters.add(next);
                }

                Gdx.app.debug("Menu Command", s + parameters.toString());
                if (!parameters.isEmpty()) {
                    try {
                        runPython(parameters);
                    } catch (FileNotFoundException e) {
                        Gdx.app.debug("Menu Info", "FileNotFound");
                    }
                }

                commandRunning = false;
                input.setText("");
            }
        });
        // t.setName(name); might want to change this
        t.start();
    }

    private void runPython(List<String> parameters) throws FileNotFoundException {
        PythonInterpreter pi = new PythonInterpreter();

        File[] files = new File(Util.RESOURCES.getPath() + "/python/menu/").listFiles();
        File file = null;

        if (files == null) {
            throw new FileNotFoundException();
        }

        for (File f : files) {
            if (f.getName().equals(parameters.get(0) + ".py")) {
                file = f;
            }
        }

        if (file == null || !file.exists()) {
            throw new FileNotFoundException();
        }
        Gdx.app.debug("Menu Info", "python file: " + file.getPath());

        parameters.remove(0); // first parameter is always the command
        pi.set("parameters", parameters);
        pi.set("screen", this);

        pi.execfile(file.getPath());
    }

    @SuppressWarnings("deprecation")
    private void stop() {
        input.setText("");
        t.stop();
        commandRunning = false;
    }
}
