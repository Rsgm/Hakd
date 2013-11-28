package hakd.networks.devices.parts;

import hakd.other.File;
import hakd.other.File.FileType;

import java.util.ArrayList;
import java.util.List;

public final class Storage extends Part {
    private boolean ssd; // doubles the speed
    private int capacity; // in GB
    int speed; // either MHz or MB/s(megabyte/s, not megabit/s) depending on the part cpu also has core modifier speed = speed (1.8*cores)

    // storage ArrayLists
    private List<File> osFiles = new ArrayList<File>(); // operating system files, !FUN!
    private List<File> userFiles = new ArrayList<File>(); // random files people save
    private List<File> programFiles = new ArrayList<File>(); // (python)programs able to run, is this copywritten, will Microsoft sue?
    private List<File> logFiles = new ArrayList<File>(); // these log arrays have infinite storage, thanks to a new leap in quantum physics

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
                if (!osFiles.contains(file)) {
                    osFiles.add(file);
                    return;
                }
                break;
            case PROGRAM:
                if (!programFiles.contains(file)) {
                    programFiles.add(file);
                    return;
                }
                break;
            case LOG:
                logFiles.add(file);
                return;
            default:
                if (!userFiles.contains(file)) {
                    userFiles.add(file);
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
                for (File f : osFiles) {
                    if (f.getName().equals(name)) {
                        osFiles.remove(f);
                        return;
                    }
                }
                break;
            default:
                for (File f : userFiles) {
                    if (f.getName().equals(name)) {
                        userFiles.remove(f);
                        return;
                    }
                }
                break;
            case PROGRAM:
                for (File f : programFiles) {
                    if (f.getName().equals(name)) {
                        programFiles.remove(f);
                        return;
                    }
                }
                break;
            case LOG:
                for (File f : logFiles) {
                    if (f.getName().equals(name)) {
                        logFiles.remove(f);
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
                osFiles.remove(file);
                return;
            default:
                userFiles.remove(file);
                return;
            case PROGRAM:
                programFiles.remove(file);
                return;
            case LOG:
                logFiles.remove(file);
                return;
        }
    }


    // removes the first file with the specified name from the specified directory
    public File getFile(FileType type, String name) {
        switch (type) {
            case OS:
                for (File f : osFiles) {
                    if (f.getName().equals(name)) {
                        return osFiles.get(osFiles.indexOf(f));
                    }
                }
                break;
            default:
                for (File f : userFiles) {
                    if (f.getName().equals(name)) {
                        return userFiles.get(userFiles.indexOf(f));
                    }
                }
                break;
            case PROGRAM:
                for (File f : programFiles) {
                    if (f.getName().equals(name)) {
                        return programFiles.get(programFiles.indexOf(f));
                    }
                }
                break;
            case LOG:
                for (File f : logFiles) {
                    if (f.getName().equals(name)) {
                        return logFiles.get(logFiles.indexOf(f));
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

    public List<File> getOsFiles() {
        return osFiles;
    }

    public void setOsFiles(List<File> osFiles) {
        this.osFiles = osFiles;
    }

    public List<File> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(List<File> userFiles) {
        this.userFiles = userFiles;
    }

    public List<File> getProgramFiles() {
        return programFiles;
    }

    public void setProgramFiles(List<File> programFiles) {
        this.programFiles = programFiles;
    }

    public List<File> getLogFiles() {
        return logFiles;
    }

    public void setLogFiles(List<File> logFiles) {
        this.logFiles = logFiles;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
