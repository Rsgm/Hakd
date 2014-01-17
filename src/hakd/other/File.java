package hakd.other;

import hakd.networks.devices.parts.Storage;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class File { // TODO file hashes!

    // data
    private final String name;
    private String data;
    private final FileType type;
    private final String owner;
    private String time;
    private long timeMs;
    private Storage storage;

    public File(String name, String data, FileType type, Storage s) {
        this.name = name;
        this.data = data;
        this.type = type;

        owner = s.getDevice().getNetwork().getOwner().getName();
        storage = s;

        SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm:ss");
        Date date = new Date();
        timeMs = date.getTime();
        time = f.format(date);
    }

    public enum FileType {
        OS, USER, PROGRAM, LOG;

        private FileType() {
        }
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

    public int getSize() { // TODO balance this a bit better
        return data.length();
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public FileType getType() {
        return type;
    }
}
