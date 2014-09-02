package gui.input;

import jline.ConsoleOperations;

import java.util.HashMap;
import java.util.Map;

public class GdxInputDecoder {
    private static Map<Integer, Integer> gdxToJlineCodes = new HashMap<Integer, Integer>(); // holds the conversions of gdx input codes(keys) to jline codes(values)
    private static final int modValue = 65536; // used to give each modifier key its own block of codes

    static {
        gdxToJlineCodes();
    }

    private static void gdxToJlineCodes() {
        gdxToJlineCodes.put(19, (int) ConsoleOperations.PREV_HISTORY); //UP = 19
        gdxToJlineCodes.put(20, (int) ConsoleOperations.NEXT_HISTORY); //DOWN = 20
        gdxToJlineCodes.put(21, (int) ConsoleOperations.PREV_CHAR); //LEFT = 21
        gdxToJlineCodes.put(22, (int) ConsoleOperations.NEXT_CHAR); //RIGHT = 22

        gdxToJlineCodes.put(67, (int) ConsoleOperations.BACKSPACE); //BACKSPACE = 67
        gdxToJlineCodes.put(61, (int) ConsoleOperations.COMPLETE); //TAB = 61

        // gdxToJlineCodes.put(31 + ModifierKeys.CONTROL.value * modValue, ...); // example of using modifiers and modvalue for control-c
    }

    public static int getJlineCode(int gdxCode, int modifiers) {
        return (gdxToJlineCodes.containsKey(gdxCode + modifiers * modValue)) ? gdxToJlineCodes.get(gdxCode) : ConsoleOperations.UNKNOWN;
    }

    public static boolean contains(int keycode, int modifiers) {
        return getJlineCode(keycode, modifiers) != ConsoleOperations.UNKNOWN;
    }

    public enum ModifierKeys {
        SHIFT(1), ALT(2), CONTROL(4);

        public int value;

        ModifierKeys(int value) {
            this.value = value;
        }
    }
}
