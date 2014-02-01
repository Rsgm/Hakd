package hakd.game;

import com.badlogic.gdx.Gdx;
import hakd.gui.windows.deviceapps.Terminal;
import hakd.networks.devices.Device;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;

import java.io.FileNotFoundException;
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
        String name = input.split(" ")[0];
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                List<List<String>> parameters = new ArrayList<List<String>>();
                parameters.add(new ArrayList<String>());

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

                Gdx.app.debug("Terminal Command", input + "   " + parameters.toString());

                ArrayList returnedText = null;
                boolean lastCommandFailed = false;

                if (!parameters.isEmpty()) {
                    for (List<String> l : parameters) {
                        if (l.size() >= 2) {
                            String operator = l.remove(0);

                            try {
                                if (operator.equals("|")) { // assumes successful
                                    returnedText = runPython(l, returnedText);
                                } else if (operator.equals(";")) {
                                    returnedText = runPython(l, null);
                                } else if (operator.equals("&&") && !lastCommandFailed) {
                                    returnedText = runPython(l, null);
                                } else if (operator.equals("||") && lastCommandFailed) {
                                    returnedText = runPython(l, null);
                                }
                            } catch (FileNotFoundException e) {
                                Gdx.app.debug("Terminal Info", "FileNotFound");
                                lastCommandFailed = true;
                            } catch (PyException e) {
                                Gdx.app.error("Terminal Error", e.getMessage(), e);
                                lastCommandFailed = true;
                            }

                            if (returnedText != null) {
                                for (Object text : returnedText) {
                                    if (text instanceof String) {
                                        terminal.addText(s);
                                    }
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

    private ArrayList runPython(List<String> parameters, ArrayList returnedText) throws FileNotFoundException {
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
        pi.set("piped_text", returnedText);


        // there really is no good way of doing this
        // if (!checkPythonForCheats(file)) {
        // return;
        // }

        pi.exec(file.getData());
        return pi.get("returnText", ArrayList.class);
    }

}