package hakd.other.coreutils;

import hakd.gui.windows.deviceapps.Terminal;
import hakd.networks.devices.Device;
import hakd.other.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

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
    public static final Map<String, Method> METHOD_MAP;
    private Device device;
    private Terminal terminal;

    public FileUtils(Terminal terminal) {
        this.terminal = terminal;
        this.device = terminal.getDevice();
    }

    static {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for (Method m : FileUtils.class.getDeclaredMethods()) {
            methodMap.put(m.getName(), m);
        }
        METHOD_MAP = Collections.unmodifiableMap(methodMap);
    }

    /**
     * Finds the file with the given path on the server.
     */
    private File getFile(String path) throws FileNotFoundException {
        return device.getFile(path);
    }

    /**
     * Searches for files based on wildcards(*) in the given path.
     */
    private List<File> getFilesStar(String path) throws FileNotFoundException {
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
                throw new FileNotFoundException();
            }
            for (File f : dir.getFileMap().values()) { // search for files matching the regex name
                if (f.getName().matches(name)) {
                    Files.add(f);
                }
            }
        }

        return Files;
    }

    public List<String> cp(List<String> parameters) throws IOException {
        List<String> returnText = new ArrayList<String>();

        String options;
        String sourcePath;
        String targetPath;
        if (parameters.size() >= 2) {
            if (parameters.get(0).contains("-")) {
                options = parameters.get(0);
                sourcePath = parameters.get(1);
                targetPath = parameters.get(2);
            } else {
                options = "";
                sourcePath = parameters.get(0);
                targetPath = parameters.get(1);
            }
        } else if (parameters.get(0).contains("h")) {
            returnText.add("cp [-hor] sourcefile targetfile");
            returnText.add("Copy the sourcefile to the target file");
            returnText.add("sourcefile is the file(s) or directory to be copied");
            returnText.add("targetfile is the file or directory to copy to");
            returnText.add("");
            returnText.add("Use a * as a wildcard for specifying multiple source files");
            returnText.add("");
            returnText.add("Options:");
            returnText.add("  -h   shows this help text");
            returnText.add("  -o   overwrites all matching files");
            returnText.add("  -r   recursively find sourcefiles in the sourcefile directory");
            return returnText;
        } else {
            returnText.add("cp [-hor] sourcefile targetfile");
            return returnText;
        }

        if (sourcePath.isEmpty() || targetPath.isEmpty()) {
            returnText.add("cp [-hor] sourcefile targetfile");
            return returnText;
        } else if (sourcePath.startsWith("./")) {
            sourcePath = terminal.getDirectory().getPath() + sourcePath.substring(2);
        } else if (targetPath.startsWith("./")) {
            targetPath = terminal.getDirectory().getPath() + targetPath.substring(2);
        }

        File targetFile = getFile(targetPath);
        List<File> sourceFiles = getFilesStar(sourcePath);

        if (sourceFiles.isEmpty()) {
            sourceFiles.add(device.getFile(sourcePath));
        }

        if (sourceFiles.size() == 1) {
            File sourceFile = sourceFiles.get(0);

            if (sourceFile.isDirectory() && !options.contains("r")) {
                returnText.add("Please include -r to copy a directory");
                return returnText;
            } else if (targetFile == null || !sourceFile.isDirectory()) {
                File targetDirectory;
                if (targetFile == null) {
                    String targetDirectoryPath = sourcePath.substring(0, targetPath.lastIndexOf('\\'));
                    targetDirectory = getFile(targetDirectoryPath);
                } else {
                    targetDirectory = targetFile.getParentDirectory();
                    targetDirectory.removeFile(targetFile);
                }
                targetDirectory.addFile(sourceFile.copy());
            } else if (targetFile.isDirectory()) {
                if (targetFile.getFile(sourceFile.getName()) == null || options.contains("o")) {
                    targetFile.addFile(sourceFile.copy());
                    returnText.clear();
                    return returnText;
                } else {
                    throw new IOException("File already exists");
                }
            } else {
                throw new IOException("Could not be copied");
            }

        } else if (targetFile.isDirectory()) {
            boolean notAllCopied = false;

            for (File f : sourceFiles) {
                if (targetFile.getFile(f.getName()) == null || options.contains("o")) {
                    targetFile.addFile(f.copy());
                } else {
                    notAllCopied = true;
                }
            }

            if (notAllCopied) {
                returnText.add("Not all files could be copied");
                return returnText;
            } else {
                return returnText;
            }

        } else {
            throw new IOException("Could not copied");
        }

        returnText.clear();
        return returnText;
    }

    public List<String> ls(List<String> parameters) {
        List<String> returnText = new ArrayList<String>();

        String options;
        String directoryPath;
        if (parameters.size() >= 2) {
            options = parameters.get(0);
            directoryPath = parameters.get(1);
        } else if (parameters.size() == 1) {
            if (parameters.get(0).contains("-")) {
                options = parameters.get(0);
                directoryPath = terminal.getDirectory().getPath();
            } else {
                options = "";
                directoryPath = parameters.get(0);
            }
        } else {
            options = "";
            directoryPath = terminal.getDirectory().getPath();
        }

        if (options.contains("h")) {
            returnText.add("ls [-fhlmpQRrSt] [directory]");
            returnText.add("Copy the sourcefile to the target file");
            returnText.add("Directory is the directory to list files in");
            returnText.add("");
            returnText.add("Options:");
            returnText.add("  -f   do not sort the files, default sorts alphabetically");
            returnText.add("  -h   shows this help text");
            returnText.add("  -l   long format: size, last-modified date and filename");
            returnText.add("  -m   separate lines with commas");
            returnText.add("  -p   gives the files as a path");
            returnText.add("  -Q   enclose entry names in double quotes");
            returnText.add("  -R   recursively list files");
            returnText.add("  -r   reverse the list of files");
            returnText.add("  -S   sort the list of files by size");
            returnText.add("  -t   sort the list of files by modification time");
            return returnText;
        } else if (directoryPath.isEmpty()) {
            directoryPath = terminal.getDirectory().getPath();
        } else if (directoryPath.startsWith("./")) {
            directoryPath = terminal.getDirectory().getPath() + directoryPath.substring(2);
        }

        File directory = null;
        try {
            directory = getFile(directoryPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return returnText;
        }
        List<File> fileList;

        if (options.contains("R")) {
            fileList = directory.getRecursiveFileList(directory);
        } else {
            fileList = new ArrayList<File>(directory.getFileMap().values());
        }

        if (!options.contains("f")) {
            if (options.contains("S")) { // sort by size
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File file, File file2) {
                        return (file.getSize() + "").compareTo(file2.getSize() + ""); // this probably won't work numarically
                    }
                });
            } else if (options.contains("t")) { // sort by time
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File file, File file2) {
                        return file.getTime().compareTo(file2.getTime());  // maybe sort by miliseconds
                    }
                });
            } else { // sort by name
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File file, File file2) {
                        return file.toString().compareTo(file2.toString());
                    }
                });
            }
        }

        if (options.contains("r")) {
            Collections.reverse(fileList);
        }

        if (options.contains("l")) {
            for (File f : fileList) {
                String s = "";
//                s += f.getOwner(); TODO
                s += f.getSize() + "   ";
                s += f.getTime() + "   ";
                s += f.getName() + "   ";
                returnText.add(s);
            }
        } else if (options.contains("p")) {
            for (File f : fileList) {
                returnText.add(f.getPath() + f.getName());
            }
        } else {
            for (File f : fileList) {
                returnText.add(f.getName());
            }
        }

        if (options.contains("Q")) {
            for (int i = 0; i < returnText.size(); i++) {
                String s = "\"" + returnText.get(i) + "\"";
                returnText.set(i, s);
            }
        } else if (options.contains("m")) {
            for (int i = 0; i < returnText.size(); i++) {
                String s = returnText.get(i) + ",";
                returnText.set(i, s);
            }
        }

        return returnText;
    }
}
