package hakd.gui.windows.device;

import hakd.other.File;

public interface FileHandler {
    public void openFile(File file);

    public boolean saveAsFile(File directory, String name);

    public void save();

    public boolean isModified();

    public File getFile();

    public void close();
}
