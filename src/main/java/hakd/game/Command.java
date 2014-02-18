package hakd.game;

import com.badlogic.gdx.Gdx;
import hakd.game.pythonapi.PyDebug;
import hakd.game.pythonapi.PyDisplay;
import hakd.game.pythonapi.PyNetworking;
import hakd.game.pythonapi.PyTest;
import hakd.gui.windows.deviceapps.Terminal;
import hakd.other.coreutils.FileUtils;
import hakd.other.coreutils.ShellUtils;
import hakd.other.coreutils.TextUtils;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Command {
    private final String input;
    private final Terminal terminal;

    private final Operation operation;
    private List<String> pipedInput;
    private List<String> pipedOutput;
    private final Queue<Character> out = new ConcurrentLinkedQueue<Character>();
    private final OutputStream outputStream;

    /**
     * Runs the desired command on a separate thread. Not that any program would
     * be intensive at all, but sleep(n) or large iterations will not lock up
     * the game.
     */
    public Command(String input, final Terminal terminal, Operation operation) {
        this.input = input;
        this.terminal = terminal;
        this.operation = operation;

        outputStream = new OutputStream() {
            @Override
            public void write(int i) {
                terminal.addText((char) i);
                out.offer((char) i);
            }
        };
    }

    public boolean run() {
        List<String> parameters = parameters();

        Gdx.app.debug("Terminal Command", input + "   " + parameters.toString());

        if (parameters.isEmpty()) {
            return false;
        }

        try {
            runChooser(parameters);   // stop the command list altogether, that is how linux does it
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            Gdx.app.debug("Terminal Info", e.getMessage(), e);
            terminal.addTextln("file not found");
            return false;
        } catch (PyException e) {
            Gdx.app.error("Terminal Error", e.getMessage(), e);
            return false;
        }

        return true;
    }

    private List<String> parameters() {
        List<String> parameters = new ArrayList<String>();
        String s = input;

        while (s.matches("\\s*[(?:\".*?\")|\\S+].*")) {
            if (s.startsWith(" ")) {
                s = s.replaceFirst("\\s+", "");
            }

            String inputTemp = s;
            s = s.replaceFirst("(?:\".*?\")|\\S+", "");
            int l = s.length();

            String next = inputTemp.substring(0, inputTemp.length() - l);
            parameters.add(next);
        }

        return parameters;
    }

    private void runChooser(List<String> parameters) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
        if (FileUtils.METHOD_MAP.containsKey(parameters.get(0))) {
            FileUtils fileUtils = new FileUtils(this);
            Method method = FileUtils.METHOD_MAP.get(parameters.get(0));
            parameters.remove(0);
            method.invoke(fileUtils, parameters, pipedInput);
            return;
        } else if (TextUtils.METHOD_MAP.containsKey(parameters.get(0))) {
            TextUtils textUtils = new TextUtils(this);
            Method method = TextUtils.METHOD_MAP.get(parameters.get(0));
            parameters.remove(0);
            method.invoke(textUtils, parameters, pipedInput);
            return;
        } else if (ShellUtils.METHOD_MAP.containsKey(parameters.get(0))) {
            ShellUtils shellUtils = new ShellUtils(this);
            Method method = ShellUtils.METHOD_MAP.get(parameters.get(0));
            parameters.remove(0);
            method.invoke(shellUtils, parameters, pipedInput);
            return;
        } else {
            runPython(parameters, pipedInput);
        }

        pipedOutput.add("");
        for (char c : out) {
            if (c == '\n') {
                pipedOutput.add("");
            } else {
                String element = pipedOutput.get(pipedOutput.size() - 1) + c;
                pipedOutput.set(pipedOutput.size() - 1, element);
            }
        }
    }

    private void runPython(List<String> parameters, List<String> pipedText) throws FileNotFoundException {
        PythonInterpreter pi = new PythonInterpreter();

        hakd.other.File file;
        file = terminal.getDevice().getBin().getFile(parameters.get(0)); // executable files may only be run if they are in the bin directory


        if (file == null) {
            throw new FileNotFoundException();
        }

        Gdx.app.debug("Terminal Info", "python file: " + file.getName());

        if (parameters.size() > 1) {
            parameters.remove(0); // first parameter is always the command
        }

        pi.set("terminal", terminal);
        pi.set("DISPLAY", new PyDisplay(terminal));
        pi.set("DEBUG", new PyDebug(terminal));
        pi.set("NETWORKING", new PyNetworking(terminal));
        pi.set("TEST", new PyTest(terminal));

        pi.set("parameters", parameters);
        pi.set("piped_text", pipedText);

//        pi.set("Display", new PyDisplay(Terminal));
        pi.setIn(terminal.getInputStream());
        pi.setOut(outputStream);

        // there really is no good way of doing this
        // if (!checkPythonForCheats(file)) {
        // return; // screw it, for now let them hack away
        // }

        pi.exec(file.getData());
    }

    public enum Operation {
        PIPE/* '|' */, SUCCESSFUL/* '&&' */, NOT_SUCCESSFUL/* '||' */, EITHER/* ';' */
    }

    public List<String> getPipedOutput() {
        return pipedOutput;
    }

    public void setPipedInput(List<String> pipedInput) {
        this.pipedInput = pipedInput;
    }

    public Operation getOperation() {
        return operation;
    }

    public Queue<Character> getOut() {
        return out;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}