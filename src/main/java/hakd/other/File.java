package hakd.other;

import hakd.networks.devices.Device;

import java.text.SimpleDateFormat;
import java.util.*;

public final class File { // TODO file hashes! maybe just run an md5 hash on the java object hashcode, or just convert a random int/short to hex
    private String name;
    String data;
    File parentDirectory;
    // final String owner;
    String time; // date modified
    long timeMs;
    Device device;

    private final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    boolean isDirectory;
    Map<String, File> fileMap;


    public File(String name, String data) {
        setName(name);
        this.data = data;

        if (data == null) {
            isDirectory = true;
            fileMap = new HashMap<String, File>();
        }

//        owner = d.getDevice().getNetwork().getOwner().getName();
        SimpleDateFormat f = new SimpleDateFormat(DATE_PATTERN);
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    /**
     * Change the byte into text and add it to the end of the file.
     */
    public void addDataByte(int b) {
        data += String.valueOf(b);

        SimpleDateFormat f = new SimpleDateFormat(DATE_PATTERN);
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    /**
     * Add the char to the end of the file.
     */
    public void addDataChar(char c) {
        data += c;

        SimpleDateFormat f = new SimpleDateFormat(DATE_PATTERN);
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    public void setData(String s) {
        data = s;

        SimpleDateFormat f = new SimpleDateFormat(DATE_PATTERN);
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    public File getFile(String fileName) {
        return fileMap.get(fileName);
    }

    public File getFileRecursive(String fileName) {
        for (File f : getRecursiveFileList(this)) {
            if (f.name.equals(fileName)) {
                return f;
            }
        }
        return null;
    }

    public void addFile(File file) {
        File parentDirectory = this;
        do {
            parentDirectory = parentDirectory.parentDirectory;

            if (parentDirectory == file) { // The only reason I care about this is because getRecursiveFileList() will be stuck in a loop, otherwise I wouldn't care how players abused it
                return; // relevant xkcd: http://xkcd.com/981/
            }
        } while (parentDirectory != null);

        for (File f : fileMap.values()) {
            if (f == file || f.name.equals(file.name)) { // I guess if they are the same file, they have the same name, but I am going to leave it so there is a less chance errors
                return;
            }
        }
        fileMap.put(file.name, file);
        file.parentDirectory = this;
        file.device = device;
    }

    public void removeFile(File file) {
        fileMap.remove(file.name);
    }

    public void removeFile(String fileName) {
        for (File f : fileMap.values()) {
            if (f.name.equals(fileName)) {
                fileMap.remove(f);
            }   // I should return after this, but I need to be sure there are no file copies that somehow got there
        }
    }

    public Map<String, File> getFileMap() {
        return Collections.unmodifiableMap(fileMap);
    }

    /**
     * @param file where to start the search, should be the object in which this is being called on
     */
    public List<File> getRecursiveFileList(File file) {
        List<File> files = new ArrayList<File>();
        for (File f : file.getFileMap().values()) {
            if (f.isDirectory()) {
                files.addAll(getRecursiveFileList(f));
            }
            files.add(f);
        }

        return files;
    }

    public String getPath() {
        File file = this;
        String path = "";

        do {
            if (file.isDirectory) {
                path = "/" + path;
            }
            path = file.getName() + path;
            file = file.parentDirectory;
        } while (file.parentDirectory != null);

        path = "/" + path;
        return path;
    }

    public File copy() {
        File file = new File(name, data);
        file.isDirectory = isDirectory;

        if (isDirectory) { // if it is a directory, copy its subfiles
            file.fileMap = new HashMap<String, File>();
            for (File f : fileMap.values()) {
                file.addFile(f.copy()); // copy recursively
            }
        }

        return file;
    }

    /**
     * Checks if a directory contains no files. This will return true if it is not a directory.
     */
    public boolean isEmpty() {
        return fileMap == null || fileMap.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    public int getSize() {
        if (isDirectory) {
            return 0;
        }
        return data.length() * 1024; // TODO is is probably more realistic to multiply by 100 instead, maybe 128
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.matches(".*[~*/\\\\\"\']+.*")) { // don't allow the use of the characters (space) ~ * / \ " '
            return;
        }
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public File getParentDirectory() {
        return parentDirectory;
    }

    public void setParentDirectory(File parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public Device getDevice() {
        return device;
    }

//    public String getOwner() {
//        return owner;
//    }

    public String getTime() {
        return time;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setDevice(Device device) {
        if (this.device == null) {
            this.device = device;
        }
    }
}
