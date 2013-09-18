package hakd.internet;

import hakd.internet.Internet.Protocol;

import java.util.List;

/**
 * Ports are places that connections can be made. Note: you can have multiple
 * connections per portNumber.
 */
public final class Port {
	private String program;
	private int portNumber;
	private Protocol protocol;

	public Port(String program, int portNumber, Protocol protocol) {
		this.program = program;
		this.portNumber = portNumber;
		this.protocol = protocol;
	}

	public Port(Protocol protocol) {
		this.program = protocol.name();
		this.portNumber = protocol.portNumber;
		this.protocol = protocol;
	}

	/**
	 * Searches an array for a given portNumber.
	 *
	 * @return True if the portNumber is open.
	 */
	public static PortStatus checkPort(List<Port> array, int port) {
		for(Port p : array) {
			if(p.getPortNumber() == port) {
				return PortStatus.OPEN;
			}
		}
		return PortStatus.CLOSED;
	}

	/**
	 * Searches an array for a portNumber number of an open portNumber.
	 *
	 * @param array - The array of ports to search.
	 * @param port  - The portNumber number to search for.
	 * @return The portNumber with the specified portNumber number.
	 */
	public static Port getPort(List<Port> array, int port) {
		for(Port p : array) {
			if(p.getPortNumber() == port) {
				return p;
			}
		}
		return null; // it is best to search for individual parameters because
		// they are compared with or
	}

	public enum PortStatus {
		OPEN, CLOSED
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
}
