package hakd.game;

import com.badlogic.gdx.Gdx;
import hakd.gui.windows.deviceapps.Terminal;
import hakd.networks.devices.Device;
import hakd.other.coreutils.FileUtils;
import hakd.other.coreutils.ShellUtils;
import hakd.other.coreutils.TextUtils;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class Command {
    private final String input;
    private final Device device;
    private final Terminal terminal;
    private Thread t;
    private boolean piped = false;

    /**
     * Runs the desired command on a separate thread. Not that any program would
     * be intensive at all, but sleep(n) or large iterations will not lock up
     * the game.
     */
    public Command(String input, Device device, Terminal terminal) {
        this.input = input;
        this.device = device;
        this.terminal = terminal;

        run();
    }

    private void run() {
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                List<List<String>> parameters = parameters();

                Gdx.app.debug("Terminal Command", input + "   " + parameters.toString());

                ArrayList outputOfPreviousProgram = null;
                boolean lastCommandFailed = false;
                for (List<String> l : parameters) {
                    if (l.size() >= 2) {
                        String operator = l.remove(0);

                        try {
                            if (operator.equals("|")) { // assumes successful
                                outputOfPreviousProgram = runChooser(l, outputOfPreviousProgram);
                            } else if (operator.equals(";")) {
                                outputOfPreviousProgram = runChooser(l, null);
                            } else if (operator.equals("&&") && !lastCommandFailed) {
                                outputOfPreviousProgram = runChooser(l, null);
                            } else if (operator.equals("||") && lastCommandFailed) {
                                outputOfPreviousProgram = runChooser(l, null);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            lastCommandFailed = true;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            lastCommandFailed = true;
                        } catch (FileNotFoundException e) {
                            Gdx.app.debug("Terminal Info", e.getMessage(), e);
                            terminal.addText("file not found");
                            lastCommandFailed = true;
                        } catch (PyException e) {
                            Gdx.app.error("Terminal Error", e.getMessage(), e);
                            lastCommandFailed = true;
                        }

                        if (outputOfPreviousProgram != null) {
                            for (final Object text : outputOfPreviousProgram) {
                                if (text instanceof String) {
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            terminal.addText((String) text);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
                terminal.setCommand(null);
            }
        });
        t.setName("terminal command");
        t.start();
    }

    private List<List<String>> parameters() {
        List<List<String>> parameters = new ArrayList<List<String>>();
        parameters.add(new ArrayList<String>());
        parameters.get(0).add(";");

        String s = input;

        while (s.matches("\\s*[(?:\".*?\")|\\S+].*")) {
            if (s.startsWith(" ")) {
                s = s.replaceFirst("\\s+", "");
            }

            String inputTemp = s;
            s = s.replaceFirst("(?:\".*?\")|\\S+", "");
            int l = s.length();

            String next = inputTemp.substring(0, inputTemp.length() - l);
            if (next.equals("|") || next.equals("&&") || next.equals(";") || next.equals("||")) {
                piped = next.equals("|");
                parameters.add(new ArrayList<String>());
            }

            parameters.get(parameters.size() - 1).add(next);
        }

        return parameters;
    }

    private ArrayList runChooser(List<String> parameters, ArrayList pipedText) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
        if (FileUtils.METHOD_MAP.containsKey(parameters.get(0))) {
            FileUtils fileUtils = new FileUtils(terminal);
            Method method = FileUtils.METHOD_MAP.get(parameters.get(0));
            parameters.remove(0);
            return (ArrayList) method.invoke(fileUtils, parameters);
        } else if (TextUtils.METHOD_MAP.containsKey(parameters.get(0))) {
            TextUtils textUtils = new TextUtils(terminal);
            Method method = TextUtils.METHOD_MAP.get(parameters.get(0));
            parameters.remove(0);
            return (ArrayList) method.invoke(textUtils, parameters);
        } else if (ShellUtils.METHOD_MAP.containsKey(parameters.get(0))) {
            ShellUtils shellUtils = new ShellUtils(terminal);
            Method method = ShellUtils.METHOD_MAP.get(parameters.get(0));
            parameters.remove(0);
            return (ArrayList) method.invoke(shellUtils, parameters);
        } else {
            return runPython(parameters, pipedText);
        }
    }

    private ArrayList runPython(List<String> parameters, ArrayList pipedText) throws FileNotFoundException {
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
        pi.set("parameters", parameters);
        pi.set("piped_text", pipedText);
        pi.set("out", new ArrayList<String>());


        // there really is no good way of doing this
        // if (!checkPythonForCheats(file)) {
        // return; // screw it, for now let them hack away
        // }

        pi.exec(file.getData());
        return pi.get("out", ArrayList.class);
    }

    /**
     * Stops the command. This uses the deprecated Thread.stop(), which means it
     * will stop it no matter what. Normally this is very bad to do because it
     * will be in the middle of something. This is realistic so I will leave it,
     * there is also no better way.
     */
    @SuppressWarnings("deprecation")
    public void stop() {
        t.stop();
        terminal.setCommand(null);
    }

}