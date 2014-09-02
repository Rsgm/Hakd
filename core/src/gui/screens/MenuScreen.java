package gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import game.Hakd;
import game.MenuTerminal;
import gui.input.GdxInputDecoder;
import jline.*;
import joptsimple.OptionParser;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import other.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MenuScreen extends HakdScreen {
    private final Stage stage;
    private final Table canvas;

    private ConsoleReader consoleReader;
    private MenuTerminal terminal;
    private Queue<Integer> inputQueue;
    private InputStream in;
    private MenuOutput out;
    private float timer = 0;

    private final SimpleCompletor firstArgumentCompletor = new SimpleCompletor(listFileNames(Gdx.files.internal(Util.ASSETS + "/python/menu/").file().listFiles()));
    private Completor argumentCompletor = new ArgumentCompletor(firstArgumentCompletor);

    public MenuScreen(Hakd game) {
        super(game);

        cam = new OrthographicCamera();
        ((OrthographicCamera) cam).setToOrtho(false, width, height);
        cam.update();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam));
        canvas = new Table();
        stage.addActor(canvas);
        canvas.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        terminal = new MenuTerminal();
        inputQueue = new ConcurrentLinkedQueue<Integer>();
        in = new MenuInput();
        out = new MenuOutput();

        try {
            consoleReader = new ConsoleReader(in, out, null, terminal);
            terminal.initializeTerminal();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        terminal.setConsoleReader(consoleReader);
        terminal.setMenuScreen(this);
        consoleReader.setDefaultPrompt("[#BF5C2B]" + System.getProperty("user.name") + "[] @ 127.0.0.1" + " ~ $ ");

        consoleReader.addCompletor(argumentCompletor);
    }

    @Override
    public void show() {
        super.show();

        Gdx.input.setInputProcessor(stage);

        StartTerminal();
        canvas.add(terminal.getScroll()).expand().fill();

        try {
            consoleReader.redrawLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void StartTerminal() {
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                terminal.setAtBottom(false);
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return true;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                int keycode = event.getKeyCode();
                timer = 0;

                if (terminal.isCommandRunning()) {
                    return true;
                }

                int modifiers = 0; // using binary to pass non-mutually exclusive arguments
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                    modifiers |= GdxInputDecoder.ModifierKeys.SHIFT.value;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
                    modifiers |= GdxInputDecoder.ModifierKeys.ALT.value;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                    modifiers |= GdxInputDecoder.ModifierKeys.CONTROL.value;
                }

                int jlineCode = character >= 32 && !GdxInputDecoder.contains(keycode, modifiers) ? character : GdxInputDecoder.getJlineCode(keycode, modifiers);

                // various stuff to do before writing to screen or before adding characters to the queue
                if (keycode == 66) { // enter
                    jlineCode = 10;
                } else if (jlineCode == '[' || jlineCode == ']') { // the user may not enter brackets
                    jlineCode = ConsoleOperations.UNKNOWN; // due to the blink character messing with gdx color markup
                } else if (keycode == 61) {
                    consoleReader.removeCompletor(argumentCompletor);
                    argumentCompletor = new ArgumentCompletor(new Completor[]{firstArgumentCompletor, OptionsCompletor()});
                    consoleReader.addCompletor(argumentCompletor);
                }

                inputQueue.add(jlineCode);

                if (jlineCode == '\n') {
                    terminal.setCommandStarted(true);
                }

                String line = null;
                try {
                    line = consoleReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (jlineCode == '\n') {
                    terminal.run(line);
                }

                try {
                    consoleReader.redrawLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
//        input.addListener(new InputListener() {
//            @Override
//            public boolean keyTyped(InputEvent event, char character) {
//                if (commandRunning) {
//                    line = history.size();
//                    input.setText("Press Control and C to cancel");
//                    input.setDisabled(true);
//                }
//                return true;
//            }
//
//            @Override
//            public boolean keyDown(InputEvent event, int keycode) {
//                if (keycode == Input.Keys.ENTER && !commandRunning) {
//                    display.setText(display.getText() + "\n\n[#BF5C2B]" + System.getProperty("user.name") + "[] @ 127.0.0.1" + " : ~" + "\n$ " + input.getText());
//                    history.add(input.getText());
//
//                    Command(input.getText());
//
//                    commandRunning = true;
//                    line = history.size();
//                    input.setText("");
//                } else if (keycode == Input.Keys.C && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))) {
//                    addText("^C");
//                    stop();
//                } else if (keycode == Input.Keys.DOWN && line < history.size() - 1) {
//                    line++;
//                    input.setText(history.get(line));
//                    input.setCursorPosition(input.getText().length());
//                } else if (keycode == Input.Keys.UP && line > 0) {
//                    line--;
//                    input.setText(history.get(line));
//                    input.setCursorPosition(input.getText().length());
//                }
//                return true;
//            }
//
//            @Override
//            public boolean keyUp(InputEvent event, int keycode) {
//                if (keycode == Input.Keys.ENTER || (keycode == Input.Keys.C && (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) && commandRunning)) {
//                    scroll.setScrollY(display.getHeight());
//                }
//                return super.keyUp(event, keycode);
//            }
//        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        timer += delta;
        if (timer % MenuTerminal.BLINK_SPEED <= delta && !terminal.isCommandRunning()) {
            terminal.blink(!terminal.isBlinkCharShown());
            try {
                consoleReader.redrawLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (terminal.isAtBottom()) {
            terminal.getScroll().setScrollY(terminal.getDisplay().getHeight());
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (terminal.isCommandRunning()) {
            terminal.stop();
        }
    }

    private String[] listFileNames(File[] files) {
        Set<String> names = new HashSet<String>();
        for (File f : files) {
            String name = f.getName();
            if (name.endsWith(".py")) {
                names.add(name.substring(0, name.length() - 3));
            }
        }
        return names.toArray(new String[names.size()]);
    }

    /**
     * Runs the parser code in a command to get the options available.
     */
    private Completor OptionsCompletor() {
        String name = consoleReader.getCursorBuffer().getBuffer().toString();
        if (name.isEmpty()) {
            return new NullCompletor();
        } else if (name.contains(" ")) {
            name = name.substring(0, name.indexOf(" "));
        }

        File[] files = Gdx.files.internal(Util.ASSETS + "/python/menu/").file().listFiles();
        File file = null;

        if (files == null) {
            return new NullCompletor();
        }

        for (File f : files) {
            if (f.getName().equals(name + ".py")) {
                file = f;
            }
        }

        if (file == null || !file.exists()) {
            return new NullCompletor();
        }

        PythonInterpreter pi = new PythonInterpreter();
        Py.getSystemState().path.append(new PyString(file.getParentFile().getAbsolutePath()));

        String parserCode = Util.getParserCode(file);
        pi.exec(parserCode);
        OptionParser parser = (OptionParser) pi.get("parser").__tojava__(OptionParser.class);
        Set<String> options = new HashSet<String>(parser.recognizedOptions().keySet());
        Set<String> optionSet = new HashSet<String>();

        if (options.contains("[arguments]")) {
            options.remove("[arguments]");
        }

        for (String s : options) {
            if (s.length() == 1) {
                optionSet.add("-" + s);
            } else {
                optionSet.add("--" + s);
            }
        }

        return new SimpleCompletor(optionSet.toArray(new String[options.size()]));
    }

    private class MenuInput extends InputStream {
        @Override
        public int read() throws IOException {
            return (inputQueue.peek() != null) ? inputQueue.poll() : ConsoleOperations.UNKNOWN; // unknown is -99
        }
    }

    private class MenuOutput extends Writer {
        @Override
        public void write(char[] chars, int start, int end) throws IOException {
            terminal.write(Arrays.copyOfRange(chars, start, end));
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }

    public MenuTerminal getTerminal() {
        return terminal;
    }

    public Queue<Integer> getInputQueue() {
        return inputQueue;
    }

    public MenuOutput getOut() {
        return out;
    }
}
