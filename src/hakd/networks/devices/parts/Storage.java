package hakd.networks.devices.parts;

import hakd.other.File;
import hakd.other.Util;

import java.util.List;

public final class Storage extends Part {
    private boolean ssd; // doubles the speed
    private int capacity; // in GB
    private int speed; // either MHz or MB/s(megabyte/s, not megabit/s) depending on the part cpu also has core modifier speed = speed (1.8*cores)

    // storage ArrayLists
    private final File root = new File("root", null, null, this); // root directory of the storage filesystem, rm -rf /
    private File sys = new File("sys", null, root, this); // operating system files, !FUN!
    private File home = new File("home", null, root, this); // random files people save
    private File bin = new File("bin", null, root, this); // (python)programs able to run
    private File log = new File("log", null, root, this); // these log arrays have infinite storage, thanks to a new leap in quantum physics

    public Storage() {
        super();
        type = PartType.STORAGE;


        try {
            for (int i = 0; i < 3; i++) {
                home.addFile(new File("test file.txt", Util.getFileData((int) (Util.FILE_ENTRY_SIZE * Math.random())), home, this));

            }

            List<java.io.File> fileList = Util.listFiles(new java.io.File("python/programs/"));
            for (java.io.File f : fileList) {
                bin.addFile(new File(f.getName(), Util.getProgramData(f.getName()), bin, this));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public File getRoot() {
        return root;
    }

    public File getSys() {
        return sys;
    }

    public File getHome() {
        return home;
    }

    public File getBin() {
        return bin;
    }

    public File getLog() {
        return log;
    }
}
