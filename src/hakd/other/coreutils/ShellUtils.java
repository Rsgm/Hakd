package hakd.other.coreutils;

import hakd.gui.windows.deviceapps.Terminal;
import hakd.networks.devices.Device;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShellUtils {
    public static final Map<String, Method> METHOD_MAP;
    private Device device;
    private Terminal terminal;

    public ShellUtils(Terminal terminal) {
        this.terminal = terminal;
        this.device = terminal.getDevice();
    }

    static {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for (Method m : FileUtils.class.getMethods()) {
            methodMap.put(m.getName(), m);
        }
        METHOD_MAP = Collections.unmodifiableMap(methodMap);
    }

}
