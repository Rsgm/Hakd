package hakd.other.coreutils;

import hakd.game.Command;
import hakd.gui.windows.deviceapps.Terminal;
import hakd.networks.devices.Device;
import hakd.other.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
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
    private final Device device;
    private final Terminal terminal;
    private final PrintStream out;

    public FileUtils(Command command) {
        this.terminal = command.getTerminal();
        this.device = terminal.getDevice();
        out = new PrintStream(command.getOutputStream());
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

    private List<File> getPathFileList(String path) {
        String[] pathArray = path.split("/");
        List<File> fileList = new ArrayList<File>();
        fileList.add(device.getRoot());

        for (String fileName : pathArray) {
            File file = fileList.get(fileList.size() - 1).getFile(fileName);
            if (file == null) {
                break;
            } else if (file.isDirectory()) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    private List<String> getPathStringList(String path) {
        String[] pathArray = path.split("/");
        List<String> pathList = new ArrayList<String>();
        pathList.add("/");

        for (String fileName : pathArray) {
            String filePath = pathList.get(pathList.size() - 1) + fileName + "/";
            pathList.add(filePath);
        }

        return pathList;
    }

    public void cp(List<String> parameters, List<String> input) throws IOException {
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
            out.println("cp [-hor] sourcefile targetfile");
            out.println("Copy the sourcefile to the target file");
            out.println("sourcefile is the file(s) or directory to be copied");
            out.println("targetfile is the file or directory to copy to");
            out.println("");
            out.println("Use a * as a wildcard for specifying multiple source files");
            out.println("");
            out.println("Options:");
            out.println("  -h   shows this help text");
            out.println("  -o   overwrites all matching files");
            out.println("  -r   recursively find sourcefiles in the sourcefile directory");
            return;
        } else {
            out.println("cp [-hor] sourcefile targetfile");
            return;
        }

        if (sourcePath.isEmpty() || targetPath.isEmpty()) {
            out.println("cp [-hor] sourcefile targetfile");
            return;
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
                out.println("Please include -r to copy a directory");
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
                out.println("Not all files could be copied");
            }

        } else {
            throw new IOException("Could not copied");
        }
    }

    public void ls(List<String> parameters, List<String> input) throws IOException {
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
            out.println("ls [-fhlmpQRrSt] [directory]");
            out.println("List the files in the directory");
            out.println("");
            out.println("Options:");
            out.println("  -f   do not sort the files, default sorts alphabetically");
            out.println("  -h   shows this help text");
            out.println("  -l   long format: size, last-modified date and filename");
            out.println("  -m   separate lines with commas");
            out.println("  -p   gives the files as a path");
            out.println("  -Q   enclose entry names in double quotes");
            out.println("  -R   recursively list files");
            out.println("  -r   reverse the list of files");
            out.println("  -S   sort the list of files by size");
            out.println("  -t   sort the list of files by modification time");
            return;
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
            return;
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
                out.println(s);
            }
        } else if (options.contains("p")) {
            for (File f : fileList) {
                out.println(f.getPath() + f.getName());
            }
        } else {
            for (File f : fileList) {
                out.println(f.getName());
            }
        }
    }

    public void mkdir(List<String> parameters, List<String> input) throws IOException {
        String options = "";
        String directoryPath = "";
        if (parameters.size() >= 2) {
            options = parameters.get(0);
            directoryPath = parameters.get(1);
        } else if (parameters.size() == 1) {
            directoryPath = parameters.get(0);
        }

        if (options.contains("h")) {
            out.println("mkdir [-pv] [directory]");
            out.println("Creates a directory at the given path");
            out.println("");
            out.println("Options:");
            out.println("  -p   will also create all directories leading up to the given directory that do not exist already. If the given directory already exists, ignore the error.");
            out.println("  -v   display each directory that mkdir creates, most often used with -p");
            return;
        } else if (directoryPath.isEmpty()) {
            out.println("mkdir [-pv] [directory]");
            return;
        } else if (directoryPath.startsWith("./")) { //|| !directoryPath.startsWith("/")) {
            directoryPath = terminal.getDirectory().getPath() + directoryPath.substring(2);
        }

        String name;
        if (options.contains("p")) {
            List<String> pathList = getPathStringList(directoryPath);

            File lastFile = device.getRoot();
            for (String dir : pathList) {
                try {
                    lastFile = device.getFile(dir);
                } catch (FileNotFoundException e) {
                    // expected

                    String[] names = dir.split("/");
                    name = names[names.length - 1];

                    File file = new File(name, null);
                    lastFile.addFile(file);
                    lastFile = file;
                    if (options.contains("v")) {
                        out.println(file.getPath());
                    }
                }
            }

        } else {
            String[] names = directoryPath.split("/");
            name = names[names.length - 1];

            List<File> fileList = getPathFileList(directoryPath);
            File dir = fileList.get(fileList.size() - 1);
            File file = new File(name, null);
            dir.addFile(file);

            if (options.contains("v") && dir.getFileMap().containsValue(file)) {
                out.println(file.getPath());
            }
        }
    }

}
