package hakd.networking.networks.devices.parts;

import hakd.networking.networks.Network;
import hakd.networking.networks.devices.Device;

import java.util.ArrayList;

public class Storage extends Part {
	private boolean			ssd;										// doubles the speed
	private int				capacity;									// in GB

	// storage ArrayLists
	private ArrayList<File>	osFiles			= new ArrayList<File>();	// operating system files, !FUN!.
	private ArrayList<File>	userFiles		= new ArrayList<File>();	// random files people save
	private ArrayList<File>	programFiles	= new ArrayList<File>();	// (lua)programs able to run // is this copywritten?(will Microsoft sue me?)

	public Storage(int level, Network network, Device device) {
		super(level, network, device);

		switch (level) {
			case 0:
				setSpeed((level + 1) * 30 + (int) (Math.random() * 30));
				capacity = (int) Math.pow(2, (level + 4) + (int) (Math.random() * 3 - 1));
			default:
				setSpeed((level + 1) * 30 + ((int) (Math.random() * 60 - 30)));
				capacity = (int) Math.pow(2, (level + 4) + (int) (Math.random() * 3 + 1) - 2); // start at 16 GB at level 1 and make sure the OS takes
				// up 15 GB
				if (1 == (int) ((Math.random() * 30) + 1)) {
					ssd = true;
					setSpeed(getSpeed() * 2);
					capacity /= 2;
				}
		}

	}

	public void addProgram(int size, String name, String data, String type) {
		userFiles.add(new File(size, name, data, type));
	}

	public void removeProgram(Directory directory, String name) {// removes the first file with the specified name from the specified directory
		switch (directory) {
			case OSFILES:
				for (File f : osFiles) {
					if (f.getName() == name) {
						osFiles.remove(f);
					}
				}
				break;
			default:
				for (File f : userFiles) {
					if (f.getName() == name) {
						userFiles.remove(f);
					}
				}
				break;
			case PROGRAMFILES:
				for (File f : programFiles) {
					if (f.getName() == name) {
						programFiles.remove(f);
					}
				}
		}
	}

	public enum Directory {
		OSFILES, USERFILES, PROGRAMFILES;
		private Directory() {
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

	public ArrayList<File> getOsFiles() {
		return osFiles;
	}

	public void setOsFiles(ArrayList<File> osFiles) {
		this.osFiles = osFiles;
	}

	public ArrayList<File> getUserFiles() {
		return userFiles;
	}

	public void setUserFiles(ArrayList<File> userFiles) {
		this.userFiles = userFiles;
	}

	public ArrayList<File> getProgramFiles() {
		return programFiles;
	}

	public void setProgramFiles(ArrayList<File> programFiles) {
		this.programFiles = programFiles;
	}
}
