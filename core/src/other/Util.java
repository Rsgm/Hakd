package other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import gui.Room;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.io.File;
import java.util.*;

public class Util {
    private static final int MAX_FILE_NUMBER = 1000;

    public static final Map<String, Map<String, File>> TEXT_FILES; // this allows text to be mod friendly, because you don't have to change this class
    public static final Map<String, String> PROGRAMS; // this allows text to be mod friendly, because you don't have to change this class
    public static final String ASSETS;

    static {
        if (new java.io.File("core/assets/").exists()) {
            ASSETS = "core/assets/";
        } else {
            ASSETS = "assets/";
        }

        // this may take longer to load if there are a lot of files, but it is worth not having to iterate through a directory more than once
        Map<String, Map<String, File>> textFileMap = new HashMap<String, Map<String, File>>();
        File[] textFileList = Gdx.files.internal(ASSETS + "textfiles/").file().listFiles();
        if (textFileList != null) {
            for (File d : textFileList) {
                if (d.isDirectory()) {

                    HashMap<String, File> fileHashMap = new HashMap<String, File>();

                    File[] files = d.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            if (!f.isDirectory()) {
                                fileHashMap.put(f.getName(), Gdx.files.internal(f.getPath()).file());
                            }
                        }
                    }

                    textFileMap.put(d.getName(), fileHashMap);
                }
            }
        }
        TEXT_FILES = Collections.unmodifiableMap(textFileMap);

        Map<String, String> programs = new HashMap<String, String>();
        for (File f : listFiles(Gdx.files.internal(Util.ASSETS + "/python/programs/").file())) {
            if (!f.isDirectory()) {
                String data = "";

                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    while (true) {
                        String s = reader.readLine();
                        if (s == null) {
                            break;
                        }
                        data += s + "\n";
                    }
                    programs.put(f.getName().substring(0, f.getName().length() - 3), data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        PROGRAMS = Collections.unmodifiableMap(programs);
    }


    /**
     * Converts float x and y orthogonal screen coordinates into int x and y
     * isometric.
     */
    public static Vector2 orthoToIso(float x, float y) {
        y += .25f;

        Vector2 iso = new Vector2();
        iso.x = Room.ROOM_HEIGHT - (int) ((x / 1f) + (y / .5f));
        iso.y = (int) ((x / 1f) - (y / .5f) + 1); // x is divided by the tile width (measured in ortho coords, in this case it is one), y is divided by the tile height
        return iso;
    }

    /**
     * Returns float x and y orthogonal screen coordinates at the bottom middle
     * of the isometric tile coordinate.
     */
    public static Vector2 isoToOrtho(float iX, float iY) {
        Vector2 ortho = new Vector2();
        ortho.x = (Room.ROOM_HEIGHT + iX - iY) / 2 - 0.5f;
        ortho.y = (Room.ROOM_HEIGHT - iX - iY) / 4 - 0.25f;
        return ortho; // adapted from http://www.java-gaming.org/topics/isometric-screen-space/27698/view.html
    }

    public static String ganerateName() {
        String[] names = new String[]{"Adara", "Adena", "Adrianne", "Alarice", "Alvita", "Amara", "Ambika", "Antonia", "Araceli", "Balandria", "Basha",
                "Beryl", "Bryn", "Callia", "Caryssa", "Cassandra", "Casondrah", "Chatha", "Ciara", "Cynara", "Cytheria", "Dabria", "Darcei",
                "Deandra", "Deirdre", "Delores", "Desdomna", "Devi", "Dominique", "Drucilla", "Duvessa", "Ebony", "Fantine", "Fuscienne",
                "Gabi", "Gallia", "Hanna", "Hedda", "Jerica", "Jetta", "Joby", "Kacila", "Kagami", "Kala", "Kallie", "Keelia", "Kerry",
                "Kerry-Ann", "Kimberly", "Killian", "Kory", "Lilith", "Lucretia", "Lysha", "Mercedes", "Mia", "Maura", "Perdita", "Quella",
                "Riona", "Ryan", "Safiya", "Salina", "Severin", "Sidonia", "Sirena", "Solita", "Tempest", "Thea", "Treva", "Trista", "Vala", "Winta"};

        PythonInterpreter pi = new PythonInterpreter();
        pi.set("PLACES", names);
        pi.execfile(Gdx.files.internal(ASSETS + "/python/NameGenerator.py").read());
        return pi.get("name").toString();
    }

    public static String ganerateCityName() {
        String[] names = new String[]{"Bangkok", "Beijing", "Buenos Aires", "Cairo", "Delhi", "Dhaka", "Guangzhou", "Hong Kong", "Istanbul", "Jakarta",
                "Karachi", "Kinshasa", "Kolkata", "Lagos", "Lima", "London", "Los Angeles", "Manila", "Mexico City", "Moscow", "Mumbai",
                "New York City", "Osaka", "Rio de Janeiro", "São Paulo", "Seoul", "Shanghai", "Tehran", "Tianjin", "Tokyo"
        };

        PythonInterpreter pi = new PythonInterpreter();
        pi.set("PLACES", names);
        pi.execfile(Gdx.files.internal(ASSETS + "/python/NameGenerator.py").read());
        String name = pi.get("name").toString();
        System.out.println(name);
        return name;
    }

    public static String ganerateCountryName() {
        String[] names = new String[]{};

        PythonInterpreter pi = new PythonInterpreter();
        pi.set("PLACES", names);
        pi.execfile(Gdx.files.internal(ASSETS + "/python/NameGenerator.py").read());
        return pi.get("name").toString();
    }

    public static other.File getFileData(String directory, String fileName) throws FileNotFoundException {
        String name = "";
        String data = "";
        BufferedReader reader;
        File file = TEXT_FILES.get(directory).get(fileName);

        if (file == null) {
            throw new FileNotFoundException();
        }

        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String s;
        while (true) {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                Gdx.app.error("IO exception", "", e);
                break;
            }

            if (s == null) {
                break;
            } else if (s.startsWith("@name:")) {
                name = s.substring(6);
                int index = name.lastIndexOf('.');
                name = name.substring(0, index) + "_" + (int) (Math.random() * MAX_FILE_NUMBER) + name.substring(index);
            } else {
                data += s + "\n";
            }
        }

        return new other.File(name, data);
    }

    /**
     * Recursively list all of the (java.io)files in a directory.
     *
     * @param file is the starting directory
     * @return the list of fies in that directory
     */
    public static List<File> listFiles(File file) {
        List<File> files = new ArrayList<File>();
        File[] fileArray = file.listFiles();
        if (fileArray != null) {
            for (File f : fileArray) {
                if (f.isDirectory()) {
                    files.addAll(listFiles(f));
                } else {
                    files.add(f);
                }
            }
        }

        return files;
    }



    public static String tutorialText(int pos) {
        String text = "";

        String data = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream("/resources/tutorial" + pos + ".txt")));

        try {
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                text += s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static String getParserCode(File file) {
        String text = Gdx.files.internal(file.getPath()).readString();
        String parseCode = text.substring(text.indexOf("#@parse_start"), text.lastIndexOf("#@parse_end"));

        return "from joptsimple import OptionParser\n\n" + parseCode + "\nparser = getParser()";
    }
}
