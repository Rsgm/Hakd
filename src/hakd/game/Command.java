package hakd.game;

import com.badlogic.gdx.Gdx;
import hakd.gui.windows.deviceapps.Terminal;
import hakd.networks.devices.Device;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Command {
    private final String input;
    private final Device device;
    private final Terminal terminal;
    private Thread t;
    private final Queue<Integer> userInputBuffer;

    /**
     * Runs the desired command on a separate thread. Not that any program would
     * be intensive at all, but sleep(n) or large iterations will not lock up
     * the game.
     */
    public Command(String input, Device device, Terminal terminal) {
        this.input = input;
        this.device = device;
        this.terminal = terminal;

        userInputBuffer = new ConcurrentLinkedQueue<Integer>();

        run();
    }

    private void run() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
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

                Gdx.app.debug("Terminal Command", input + "   " + parameters.toString());
                if (!parameters.isEmpty()) {
                    try {
                        runPython(parameters);
                    } catch (FileNotFoundException e) {
                        Gdx.app.debug("Terminal Info", "FileNotFound");
                    } catch (PyException e) {
                        Gdx.app.error("Terminal Error", e.getMessage(), e);
                    }
                }

                terminal.setCommand(null);
            }
        });
        // t.setName(name); might want to change this
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

    private void runPython(List<String> parameters) throws FileNotFoundException {
        PythonInterpreter pi = new PythonInterpreter();

        hakd.other.File file;
        file = terminal.getDevice().getBin().getFile("parameters.get(0)" + ".py"); // executable files may only be run if they are in the bin directory


        if (file == null)

        {
            throw new FileNotFoundException();
        }

        Gdx.app.debug("Terminal Info", "python file: " + file.getName()); //TODO file.getPath()); maybe use SDa,b,c... for drive paths

        if (parameters.size() > 1)

        {
            parameters.remove(0); // first parameter is always the command
            pi.set("parameters", parameters);
        }

        pi.set("terminal", terminal);

        // there really is no good way of doing this
        // if (!checkPythonForCheats(file)) {
        // return;
        // }

        pi.exec(file.getData());
    }

    public Queue<Integer> getUserInputBuffer() {
        return userInputBuffer;
    }
}