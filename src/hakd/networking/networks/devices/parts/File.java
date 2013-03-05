package hakd.networking.networks.devices.parts;

public class File {

	// data
	private int		size;
	private String	name;
	private String	data;
	private String	type;

	public File(int size, String name, String data, String type) {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
