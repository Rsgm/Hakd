package hakd.other;

import hakd.networks.devices.parts.Storage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class File { // TODO file hashes! maybe just run an md5 hash on the java object hashcode, or just convert a random int/short to hex
    private String name;
    private String data;
    private File parentDirectory;
    //    private final String owner;
    private String time;
    private long timeMs;
    private final Storage storage;

    private boolean isDirectory;
    private List<File> fileList;


    public File(String name, String data, File directory, Storage s) {
        this.name = name;
        this.data = data;
        this.parentDirectory = directory;

        if (data == null) {
            isDirectory = true;
            fileList = new ArrayList<File>();
        }

//        owner = s.getDevice().getNetwork().getOwner().getName();
        storage = s;

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

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public Storage getStorage() {
        return storage;
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
