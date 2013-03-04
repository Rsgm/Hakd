package hakd.networking.devices;

import hakd.networking.Network;
import hakd.networking.NetworkController;
import hakd.networking.devices.parts.Cpu;
import hakd.networking.devices.parts.Gpu;
import hakd.networking.devices.parts.Memory;
import hakd.networking.devices.parts.Part;
import hakd.networking.devices.parts.Storage;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;

public class Device implements ConnectableDevice {

	// stats
	private final Network			network;
	private int						networkId;							// which network a server belongs to
	private int						serverId;							// server id starts at 0 in the gui as well
	private final int				level;

	// private final int webserver = 0; // 0 = 404, if port 80 is open
	private final ArrayList<String>	ports	= new ArrayList<String>();	// port, program / if its closed just delete it
	private final ArrayList<String>	logs	= new ArrayList<String>();	// TODO make this a file instead // connecting from and the action after that

	private String					brand;								// for example bell
	private String					model;

	// objects
	private int						cpuSockets;						// easier than using a for loop to count the amount, just remember to
	// change this value
	private int						memorySlots;						// maybe have a maximum part number, so you can specialize a server
	private int						storageSlots;
	private int						gpuSlots;

	// objects
	private final ArrayList<Part>	parts	= new ArrayList<Part>();

	// --------constructor--------
	public Device(Network n) {
		network = n;
// serverId = NetworkController.getNetworks().get(networkId).getServers().size(); // are these needed?
// networkId = network.getNetworkId();
		level = network.getLevel();

		switch (level) {
			case 0:
				cpuSockets = (int) (Math.random() * 2 + 1);
				gpuSlots = 1; // TODO server part generation code
				memorySlots = 1;
				storageSlots = 1;
				break;
			case 7:
				cpuSockets = (int) (Math.random() * 2 + 7);
				gpuSlots = 1;
				memorySlots = 1;
				storageSlots = 1;
				break;
			default:
				cpuSockets = (int) (Math.random() * 3 + level);
				gpuSlots = 1;
				memorySlots = 1;
				storageSlots = 1;
				break;
		}
		for (int i = 0; i < cpuSockets; i++) {
			parts.add(new Cpu(level, networkId, serverId));
		}
		for (int i = 0; i < gpuSlots; i++) {
			parts.add(new Gpu(level, networkId, serverId));
		}
		for (int i = 0; i < memorySlots; i++) {
			parts.add(new Memory(level, networkId, serverId));
		}
		for (int i = 0; i < storageSlots; i++) {
			parts.add(new Storage(level, networkId, serverId));
		}
	}

	// --------methods--------

	@Override
	public boolean Connect(Network network, String program, int port) {
		logs.add(network.);
		logs.add("connecting to " + program + ":" + port);
		if (ports.get(ports.indexOf(port) + 1).equals(program)) {
			try {
				switch (port) {
					default: // 80, 443, others but just get directed to a 404 if not a website
						Desktop.getDesktop().browse(java.net.URI.create("http://localhost:80/network/" + address));
					case 31337:
						// grant complete(root?) access
						// open ssh
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean Disconnect(Network network, String program, int port) {

		return false;
	}

	@Override
	public boolean addPorts(int port, String program, int server) {
		if (ports.contains(ports) == false) {

			ports.add(port + "");
			ports.add(program);
			return true;
		}
		return false;
	}

	@Override
	public boolean removePort(int port, String program, int server) {
		int index = ports.indexOf(port);
		if (ports.contains(port)) {
			ports.remove(index + 1);
			ports.remove(index);
			return true;
		}
		return false;
	}

	// --------getters/setters--------
}
