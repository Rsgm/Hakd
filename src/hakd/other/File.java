package hakd.other;

import hakd.networks.devices.Device;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class File { // TODO file hashes! maybe just run an md5 hash on the java object hashcode, or just convert a random int/short to hex
    String name;
    String data;
    File parentDirectory;
    // final String owner;
    String time;
    long timeMs;
    Device device;

    boolean isDirectory;
    List<File> fileList;


    public File(String name, String data, File directory, Device d) {
        this.name = name;
        this.data = data;
        this.parentDirectory = directory;

        if (data == null) {
            isDirectory = true;
            fileList = new ArrayList<File>();
        }

//        owner = d.getDevice().getNetwork().getOwner().getName();
        device = d;

        SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm:ss");
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    /**
     * Change the byte into text and add it to the end of the file.
     */
    public void addDataByte(int b) {
        data += String.valueOf(b);

        SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm:ss");
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    /**
     * Add the char to the end of the file.
     */
    public void addDataChar(char c) {
        data += c;

        SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm:ss");
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    public void setData(String s) {
        data = s;

        SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm:ss");
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    public File getFile(String fileName) {
        for (File f : fileList) {
            if (f.name.equals(fileName)) {
                return f;
            }
        }
        return null;
    }

    public File getFileRecursive(String fileName) {
        for (File f : listFilesRecursive(this)) {
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

            if (parentDirectory == file) { // The only reason I care about this is because listFilesRecursive() will be stuck in a loop, otherwise I wouldn't care how players abused it
                return; // relevant xkcd: http://xkcd.com/981/
            }
        } while (parentDirectory != null);

        for (File f : fileList) {
            if (f == file || f.name.equals(file.name)) { // I guess if they are the same file, they have the same name, but I am going to leave it so there is a less chance errors
                return;
            }
        }
        fileList.add(file);
        file.parentDirectory = this;
    }

    public void removeFile(File file) {
        while (fileList.contains(file)) { // again, just to make sure there are no copies
            fileList.remove(file);
        }
    }

    public void removeFile(String fileName) {
        for (File f : fileList) {
            if (f.name.equals(fileName)) {
                fileList.remove(f);
            }   // I should return after this, but I need to be sure there are no file copies that somehow got there
        }
    }

    public List<File> listFiles() {
        return Collections.unmodifiableList(fileList);
    }

    /**
     * @param file where to start the search, should be the object in which this is being called on
     */
    public List<File> listFilesRecursive(File file) {
        List<File> files = new ArrayList<File>();
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                files.addAll(listFilesRecursive(f));
            } else {
                files.add(f);
            }
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
        File file = new File(name, data, null, device);
        file.isDirectory = isDirectory;

        if (isDirectory) { // if it is a directory, copy its subfiles
            file.fileList = new ArrayList<File>();
            for (File f : fileList) {
                file.addFile(f.copy()); // copy recursively
            }
        }

        return file;
    }

    /**
     * Checks if a directory contains no files. This will return true if it is not a directory.
     */
    public boolean isEmpty() {
        if (fileList == null) {
            return true;
        }

        return fileList.isEmpty();
    }

    @Override
    public String toString() {
        return name;
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
}
