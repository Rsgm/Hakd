package hakd.other;

import hakd.networks.devices.Device;
import hakd.other.enumerations.Protocol;

import java.util.List;

public class Port {
	private Device		device;
	private String		program;
	private int			port;
	private Protocol	protocol;

	public Port(Device device, String program, int port, Protocol protocol) {
		this.device = device;
		this.program = program;
		this.port = port;
		this.protocol = protocol;
	}

	// --------methods--------

	// searches an array for a given port parameter
	public static boolean checkPortOr(List<Port> array, Device device, String program, int port, Protocol protocol) {
		for (Port p : array) { // this will work as long as none of the ports in the array have null parameters
			if (p.getDevice() == device || p.getProgram().equals(program) || p.getPort() == port || p.getProtocol() == protocol) {
				return true;
			}
		}
		return false; // it is best to search for individual parameters because they are compared with or
	}

	// searches an array for a given program binding
	public static boolean checkPortAnd(List<Port> array, String program, int port, Protocol protocol) {
		for (Port p : array) { // this will work as long as none of the ports in the array have null parameters
			if (p.getProgram().equals(program) && p.getPort() == port && p.getProtocol() == protocol) {
				return true;
			}
		}
		return false; // it is best to search for individual parameters because they are compared with or
	}

	// given a device or port number, this will return the port it is on
	public static Port getPort(List<Port> array, Device device, int port) {
		for (Port p : array) {
			if (p.getDevice() == device || p.getPort() == port) {
				return p;
			}
		}
		return null; // it is best to search for individual parameters because they are compared with or
	}

	// --------getters/setters--------
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
}
