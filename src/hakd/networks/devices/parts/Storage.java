package hakd.networks.devices.parts;

import hakd.other.File;
import hakd.other.File.FileType;

import java.util.ArrayList;
import java.util.List;

public final class Storage extends Part {
    private boolean ssd; // doubles the speed
    private int capacity; // in GB
    private int speed; // either MHz or MB/s(megabyte/s, not megabit/s) depending on the part cpu also has core modifier speed = speed (1.8*cores)

    // storage ArrayLists
    private List<File> sys = new ArrayList<File>(); // operating system files, !FUN!
    private List<File> home = new ArrayList<File>(); // random files people save
    private List<File> bin = new ArrayList<File>(); // (python)programs able to run, is this copywritten, will Microsoft sue?
    private List<File> log = new ArrayList<File>(); // these log arrays have infinite storage, thanks to a new leap in quantum physics

    public Storage() {
        super();
        type = PartType.STORAGE;
    }

    /**
     * Add a file to the specified directory.
     */
    public void addFile(File file) throws Exception {
        switch (file.getType()) {
            case OS:
                if (!sys.contains(file)) {
                    sys.add(file);
                    return;
                }
                break;
            case PROGRAM:
                if (!bin.contains(file)) {
                    bin.add(file);
                    return;
                }
                break;
            case LOG:
                log.add(file);
                return;
            default:
                if (!home.contains(file)) {
                    home.add(file);
                    return;
                }
                break;
        }
        throw new Exception("File already exists");
    }

    /**
     * Removes the first file with the specified name from the specified directory.
     */
    public void removeFile(FileType type, String name) {
        switch (type) {
            case OS:
                for (File f : sys) {
                    if (f.getName().equals(name)) {
                        sys.remove(f);
                        return;
                    }
                }
                break;
            default:
                for (File f : home) {
                    if (f.getName().equals(name)) {
                        home.remove(f);
                        return;
                    }
                }
                break;
            case PROGRAM:
                for (File f : bin) {
                    if (f.getName().equals(name)) {
                        bin.remove(f);
                        return;
                    }
                }
                break;
            case LOG:
                for (File f : log) {
                    if (f.getName().equals(name)) {
                        log.remove(f);
                        return;
                    }
                }
        }
    }

    /**
     * Removes the file from the storage part, if it exists.
     */
    public void removeFile(File file) {
        switch (file.getType()) {
            case OS:
                sys.remove(file);
                return;
            default:
                home.remove(file);
                return;
            case PROGRAM:
                bin.remove(file);
                return;
            case LOG:
                log.remove(file);
                return;
        }
    }


    // removes the first file with the specified name from the specified directory
    public File getFile(FileType type, String name) {
        switch (type) {
            case OS:
                for (File f : sys) {
                    if (f.getName().equals(name)) {
                        return sys.get(sys.indexOf(f));
                    }
                }
                break;
            default:
                for (File f : home) {
                    if (f.getName().equals(name)) {
                        return home.get(home.indexOf(f));
                    }
                }
                break;
            case PROGRAM:
                for (File f : bin) {
                    if (f.getName().equals(name)) {
                        return bin.get(bin.indexOf(f));
                    }
                }
                break;
            case LOG:
                for (File f : log) {
                    if (f.getName().equals(name)) {
                        return log.get(log.indexOf(f));
                    }
                }
        }
        return null;
    }

    public boolean isSsd() {
        return ssd;
    }

    public void setSsd(boolean ssd) {
        this.ssd = ssd;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<File> getSys() {
        return sys;
    }

    public void setSys(List<File> sys) {
        this.sys = sys;
    }

    public List<File> getHome() {
        return home;
    }

    public void setHome(List<File> home) {
        this.home = home;
    }

    public List<File> getBin() {
        return bin;
    }

    public void setBin(List<File> bin) {
        this.bin = bin;
    }

    public List<File> getLog() {
        return log;
    }

    public void setLog(List<File> log) {
        this.log = log;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
