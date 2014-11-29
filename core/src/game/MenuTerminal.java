package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import game.pythonapi.menu.PyMenu;
import gui.screens.MenuScreen;
import jline.ConsoleReader;
import jline.Terminal;
import joptsimple.BuiltinHelpFormatter;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import other.Util;

import java.io.*;
import java.util.Arrays;

public class MenuTerminal extends Terminal {
    private Label display;
    private ScrollPane scroll;
    private boolean echoEnabled;
    private char echoChar;
    private ConsoleReader consoleReader;

    private char blinkTempChar;
    private boolean cursorAtEnd;
    private static char BLINK_CHAR = '_'; // this would be preferred: '█';
    private boolean blinkCharShown = false;
    public static final float BLINK_SPEED = .6f; // in seconds

    private boolean newline = false;
    private MenuScreen menuScreen;

    private Thread commandThread;
    private boolean commandRunning = false;
    private boolean commandStarted = false;
    private boolean atBottom; // used to keep the terminal scrolled down
    private int tab = 0; // used to remove unwnated text added by jline
    private boolean tabComplete = false;
    private static final int spacesJlineAdds = 3; // the amount of spaces jline adds after a complete character is sent (it may be jline's tab)

    @Override
    public void initializeTerminal() throws Exception {
        Skin skin = Hakd.assets.get(("skins/uiskin.json"), Skin.class);

        display = new Label("", skin.get("console", Label.LabelStyle.class));
        scroll = new ScrollPane(display, skin);

        display.setFontScale(.6f);
        display.getStyle().font.setMarkupEnabled(true); // for now, this is broken for labels, but I will leave it in so I can easily tell when it gets fixed

        display.setWrap(false);
        display.setAlignment(10, Align.left);
        String terminalInfo = "[#3C91BF]Terminal [[Version 0." + ((int) (Math.random() * 100)) / 10 + "]";
        terminalInfo += "\n[#38FF4C]" + System.getProperty("user.name") + "[] @ [#FFC123]127.0.0.1[]";
        terminalInfo += "\nStorage:";

        // makes hakd start a bit slower, but lets the first jython program start a bit sooner
        new PythonInterpreter().exec("import sys"); // it is a trade off that lets the game feel more responsive

        // only shows the partition in which root is mounted from on linux, this will be the only time something works on windows but not linux
        // I could just search for /dev/sd* if on linux(mac too?) and read the size of them some how, not worth the work
        for (int i = 0; i < File.listRoots().length; i++) {
            terminalInfo += "\n    Drive[[" + i + "]  " + (-File.listRoots()[i].getFreeSpace() +
                    File.listRoots()[i].getTotalSpace()) / 1000000000 + "GB Used,  " +
                    File.listRoots()[i].getTotalSpace() / 1000000000 + "GB Total";
        }

        display.setText(terminalInfo + "\n-----------------------------------------------------\n" +
                "     Type \"help\" to get started.[]\n\n");
    }

    @Override
    public void beforeReadLine(ConsoleReader reader, String prompt, Character mask) {
        super.beforeReadLine(reader, prompt, mask);

        blink(false);

        if (menuScreen.getInputQueue().peek() == '\n') {
            newline = true;
            display.setText(display.getText() + "\n");
        } else if (menuScreen.getInputQueue().peek() == -58) { // used to remove unwanted text added by jline
            write(new char[]{'\r'});
            tabComplete = true;
        }
    }

    @Override
    public void afterReadLine(ConsoleReader reader, String prompt, Character mask) {
        super.afterReadLine(reader, prompt, mask);

        if (newline) {
            newline = false;
        } else if (tabComplete) {
            tabComplete = false;
        }
        atBottom = true;
    }

    @Override
    public int getTerminalWidth() {
        return 20;
    }

    @Override
    public int getTerminalHeight() {
        return 20;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean getEcho() {
        return false;
    }

    @Override
    public boolean isEchoEnabled() {
        return echoEnabled;
    }

    @Override
    public void enableEcho() {
        echoEnabled = true;
    }

    @Override
    public void disableEcho() {
        echoEnabled = false;
    }

    @Override
    public boolean isANSISupported() {
        return false; // for now
    }

    @Override
    public InputStream getDefaultBindings() {
        return Terminal.class.getResourceAsStream("keybindings.properties");
    }

    @Override
    public int readVirtualKey(InputStream in) throws IOException {
        int code = super.readVirtualKey(in);

        if (code >= 0) {
            return code;
        }

        System.out.println();
        switch (code) {
            case MOVE_TO_BEG:
                return 1;
            case PREV_CHAR:
                return 2;
            case EXIT:
                return 4;
            case MOVE_TO_END:
                return 5;
            case NEXT_CHAR:
                return 6;
            case ABORT:
                return 7;
            case DELETE_PREV_CHAR:
                return 8;
            case COMPLETE:
                return 9;
            case NEWLINE: // LF newline
                return 10;
            case KILL_LINE:
                return 11;
            case CLEAR_SCREEN:
                return 12;
//            case NEWLINE: // CR newline, I don't know what to do with this, so I am disabling it and using LF for now
//                return 13;
            case NEXT_HISTORY:
                return 14;
            case PREV_HISTORY:
                return 16;
            case SEARCH_PREV:
                return 18;
            case KILL_LINE_PREV:
                return 21;
            case PASTE:
                return 22;
            case DELETE_PREV_WORD:
                return 23;
            case PREV_WORD:
                return 24;
            case REDISPLAY:
                return 27;
            case DELETE_NEXT_CHAR:
                return 127;
            default:
                return -1;
        }
    }

    public synchronized void write(char[] chars) {
        if (chars.length == 0 || commandStarted || commandRunning || tab > 0) {
            return;
        }

        if (tabComplete && chars.length == 1 && chars[0] == ' ') {
            return;
        }

        String text = "";
        for (char c : chars) {
            if (c == '\r') {
                display.setText(display.getText().toString().substring(0, display.getText().toString().lastIndexOf('\n') + 1));
                return;
            } else if (c == '\n' && newline) {
                // nothing goes here
            } else if (c != '\u0000' && c != '\b' && c != (char) -1) {
                text += c;
            }
        }

        if (text.length() > 0) {
            display.setText(display.getText() + escapeBrackets(text));
            System.out.println(text);
        }
    }

    public void run(final String command) {
        commandThread = new Thread(new Runnable() {
            String in = command;

            @Override
            public void run() {
                Gdx.app.debug("Menu Command", command);
                if (!command.isEmpty()) {
                    commandStarted = false;
                    commandRunning = true;

                    try {
                        runPython(command);
                    } catch (FileNotFoundException e) {
                        Gdx.app.debug("Menu Info", "FileNotFound");
                    } catch (OptionException e) {
                        addText(e.getMessage() + ". Try using -h.");
                        Gdx.app.debug("Jopt", e.getMessage());
                    } catch (Exception e) {
                        Gdx.app.debug("Menu Info", "Parser not found, ");
                    }

                    commandRunning = false;
                } else {
                    commandStarted = false;
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            consoleReader.redrawLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        // commandThread.setName(name); might want to change this
        commandThread.start();
    }

    private void runPython(String command) throws Exception {
        String name = command.contains(" ") ? command.substring(0, command.indexOf(" ")) : command;
        File[] files = Gdx.files.internal(Util.ASSETS + "/python/menu/").file().listFiles();
        File file = null;

        if (files == null) {
            addText(name + ": command not found");
            throw new FileNotFoundException();
        }

        for (File f : files) {
            if (f.getName().equals(name + ".py")) {
                file = f;
            }
        }

        if (file == null || !file.exists()) {
            throw new FileNotFoundException();
        }
        Gdx.app.debug("Menu Info", "python file: " + file.getPath());

        PythonInterpreter pi = new PythonInterpreter();
        Py.getSystemState().path.append(new PyString(file.getParentFile().getAbsolutePath()));

        String parserCode = Util.getParserCode(file);
        pi.exec(parserCode);
        OptionParser parser = (OptionParser) pi.get("parser").__tojava__(OptionParser.class);

        String arguments = command.substring(name.length());
        OptionSet options = parser.parse(arguments.split("(?<!\\\\)\\s+"));
        parser.formatHelpWith(new BuiltinHelpFormatter(65, 10)); // only for 800 pixel wide screen, which fits about 70 characters

        if (options.has("h") || options.has("help")) {
            CommandWriter writer = new CommandWriter();
            try {
                parser.printHelpOn(writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            pi.set("options", options);
            pi.set("menu", new PyMenu(menuScreen));
            pi.execfile(file.getPath());
        }
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (commandThread != null) {
            commandThread.stop();
            Gdx.app.debug("Menu Info", "Command Stopped");
        }
        commandRunning = false;
    }

    /**
     * Escapes square brackets to allow libgdx to use color markup.
     * It does only allows hex colors, defined color names are banned.
     * This is to allow programs to use brackets to use brackets if needed.
     * The user is not allowed to enter brackets though.
     *
     * @param s The String to add escape characters to.
     * @return The String with escape characters added.
     */
    private String escapeBrackets(String s) {
        String text = s;
        if (text.matches(".*\\[(?!(?:#[\\da-fA-F]{6}\\])|(?:\\])).*")) { // same regex as below, but tests if the string contains it
            text = text.replaceAll("\\[(?!(?:#[\\da-fA-F]{6}\\])|(?:\\]))", "[["); // looks for singular '[' characters that are not followed by a ']' or "#\d{6}]"
        }
        return text;
    }

    public synchronized void addText(String text) {
        text = text.replace("\n", "\n   ");
        display.setText(display.getText() + "   " + escapeBrackets(text) + "\n");
    }

    public synchronized void replaceText(String newText, int lineFromBottom) {
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

    public void blink(boolean blink) {
        StringBuffer buffer = consoleReader.getCursorBuffer().getBuffer();
        int cursor = consoleReader.getCursorBuffer().cursor;

        if (!(blinkCharShown ^ blink)) {
            return;
        }

        if (blink) {
            blinkCharShown = true;

            if (buffer.length() == cursor) {
                cursorAtEnd = true;
                blinkTempChar = 0;
                buffer.append(BLINK_CHAR);
            } else {
                cursorAtEnd = false;
                blinkTempChar = buffer.charAt(cursor);
                buffer.setCharAt(cursor, BLINK_CHAR);
            }
        } else {
            blinkCharShown = false;

            if (cursorAtEnd) {
                buffer.setCharAt(cursor, '\u0000');
                buffer.setLength(cursor);
            } else {
                buffer.setCharAt(cursor, blinkTempChar);
            }
        }
        System.out.println();
    }

    // this class is strictly for the optionparser in runPython(), there is no character escapes or any other necessary modification
    private class CommandWriter extends Writer {
        @Override
        public void write(char[] chars, int start, int end) throws IOException {
            addText(new String(Arrays.copyOfRange(chars, start, end)));
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }

    public void setConsoleReader(ConsoleReader consoleReader) {
        if (this.consoleReader != null) {
            return;
        }
        this.consoleReader = consoleReader;
    }

    public void setMenuScreen(MenuScreen menuScreen) {
        if (this.menuScreen != null) {
            return;
        }
        this.menuScreen = menuScreen;
    }

    public boolean isCommandRunning() {
        return commandRunning;
    }

    public ScrollPane getScroll() {
        return scroll;
    }

    public Label getDisplay() {
        return display;
    }

    public boolean isBlinkCharShown() {
        return blinkCharShown;
    }

    public boolean isAtBottom() {
        return atBottom;
    }

    public void setAtBottom(boolean atBottom) {
        this.atBottom = atBottom;
    }

    public boolean isCommandStarted() {
        return commandStarted;
    }

    public void setCommandStarted(boolean commandStarted) {
        this.commandStarted = commandStarted;
    }
}
