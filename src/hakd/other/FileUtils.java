package hakd.other;

import hakd.gui.windows.deviceapps.Terminal;
import hakd.networks.devices.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the file utilities from GNU CoreUtils.
 * The list of programs included in CoreUtils can be found here https://en.wikipedia.org/wiki/GNU_Core_Utilities
 * <p/>
 * I am not going to give documentation of any public methods in here.
 * Only the CoreUtil programs are public, and they are fully documented on the wiki.
 * Plus I included their help text in the methods, see that if you don't understand one.
 * I will try my best to simulate the functionality of them, but I will have to leave out options for some.
 */
public class FileUtils {
    private Device device;
    private Terminal terminal;

    public FileUtils(Terminal terminal) {
        this.terminal = terminal;
        this.device = terminal.getDevice();
    }

    /**
     * Finds the file with the given path on the server.
     */
    private File getFile(String path) {
        return device.getFile(path);
    }

    /**
     * Searches for files based on wildcards(*) in the given path.
     */
    private List<File> getFilesStar(String path) throws IOException {
        List<File> Files = new ArrayList<File>();

        if (path.contains("*")) {
            String[] pathArray = path.split("/");
            String name = pathArray[pathArray.length - 1];
            name = name.replace("*", ".*"); // setting up the regex

            pathArray[pathArray.length - 1] = "";
            String dirPath = "/"; // path to use to find the directory of the files
            for (String s : pathArray) {
                dirPath += s + "/";
            }

            File dir = getFile(dirPath);
            if (dir == null) {
                throw new IOException("File not found");
            }
            for (File f : dir.listFiles()) { // search for files matching the regex name
                if (f.name.matches(name)) {
                    Files.add(f);
                }
            }
        }

        return Files;
    }

    public String cp(String options, String source, String target) throws IOException {
        if (options.contains("h")) {
            return "cp [-hor] sourcefile targetfile\n" +
                    "Copy the sourcefile to the target file\n" +
                    "sourcefile is the file(s) or directory to be copied\n" +
                    "targetfile is the file or directory to copy to\n\n" +
                    "Use a * as a wildcard for specifying multiple source files\n\n" +
                    "Options:\n" +
                    "  -h   shows this help text\n" +
                    "  -o   overwrites all matching files\n" +
                    "  -r   recursively find sourcefiles in the sourcefile directory";
        } else if (source.isEmpty() || target.isEmpty()) {
            return "cp [-hor] sourcefile targetfile";
        } else if (source.startsWith("./")) {
            source = terminal.getDirectory().getPath() + source.substring(2);
        } else if (target.startsWith("./")) {
            target = terminal.getDirectory().getPath() + target.substring(2);
        }

        File targetFile = getFile(target);
        List<File> sourceFiles = getFilesStar(source);

        if (sourceFiles.isEmpty()) {
            sourceFiles.add(device.getFile(source));
        }

        if (sourceFiles.size() == 1) {
            File sourceFile = sourceFiles.get(0);
            if (sourceFile.isDirectory && !options.contains("r")) {
                return "Please include -r to copy a directory";
            } else if (targetFile.isDirectory) {
                if (targetFile.getFile(sourceFile.getName()) == null || options.contains("o")) {
                    targetFile.addFile(sourceFile.copy());
                    return "";
                } else {
                    throw new IOException("File already exists");
                }
            } else if (!sourceFile.isDirectory) {
                File targetParent = targetFile.parentDirectory;
                targetParent.removeFile(targetFile);
                targetParent.addFile(sourceFile.copy());
            } else {
                throw new IOException("Could not be copied");
            }

        } else if (targetFile.isDirectory) {
            boolean notAllCopied = false;

            for (File f : sourceFiles) {
                if (targetFile.getFile(f.getName()) == null || options.contains("o")) {
                    targetFile.addFile(f.copy());
                } else {
                    notAllCopied = true;
                }
            }

            if (notAllCopied) {
                return "Not all files could be copied";
            } else {
                return "";
            }

        } else {
            throw new IOException("Could not copied");
        }

        return "";
    }

}
