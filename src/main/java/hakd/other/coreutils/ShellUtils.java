package hakd.other.coreutils;

import hakd.game.Command;
import hakd.gui.windows.device.Terminal;
import hakd.networks.devices.Device;

import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShellUtils {
    public static final Map<String, Method> METHOD_MAP;
    private final Device device;
    private final Terminal terminal;
    private final OutputStreamWriter out;

    public ShellUtils(Command command) {
        this.terminal = command.getTerminal();
        this.device = terminal.getDevice();
        out = new OutputStreamWriter(command.getOutputStream());
    }

    static {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for (Method m : FileUtils.class.getMethods()) {
            methodMap.put(m.getName(), m);
        }
        METHOD_MAP = Collections.unmodifiableMap(methodMap);
    }

}
