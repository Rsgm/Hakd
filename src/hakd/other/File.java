package hakd.other;

import hakd.other.enumerations.FileType;

public class File { // TODO file hashes!

	// data
	private int			size;
	private String		name;
	private String		data;
	private FileType	type;

	public File(int size, String name, String data, FileType type) {
		this.size = size;
		this.name = name;
		this.data = data;
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
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

	public void setData(String data) {
		this.data = data;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}
}
