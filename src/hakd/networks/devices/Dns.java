package hakd.networks.devices;

import hakd.internet.Connection;
import hakd.internet.Internet.Protocol;
import hakd.internet.Port;
import hakd.networks.DefaultNetwork;
import hakd.networks.Network;
import hakd.networks.Network.IpRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Dns extends Device { // TODO let DNSs communicate a bit.
	public static List<Short> ipNumbers = new ArrayList<Short>(255);

	public Dns(DefaultNetwork defaultNetwork, int level) {
		super(defaultNetwork, level, DeviceType.DNS);

		ports.add(new Port("Dns", Protocol.DNS.portNumber, Protocol.DNS));
	}

	public Dns(Network network, int level, DeviceType type, int cpuSockets, int gpuSlots, int memorySlots, int storageSlots) {
		super(network, level, type, cpuSockets, gpuSlots, memorySlots, storageSlots);

		ports.add(new Port("Dns", Protocol.DNS.portNumber, Protocol.DNS));
	}

	/**
	 * Assigns an ip to an object that requests one, also checks it and adds it
	 * to the dns list.
	 */
	public short[] assignIp() {
		short[] ip = null;
		Collections.shuffle(ipNumbers);

		int n = 0;
		for(Connection c : connections) {
			if(c.getClient().getParentRouter().getParent() == this) {
				n++;
			} else if(n > 255) {
				return null;
			}
		}

		for(short i = 0; i < 255; i++) {
			// just randomly generate numbers for the isp, there are not enough
			// to slow it down
			switch(network.getType()) {
				case BACKBONE:
					ip = new short[]{this.ip[0], ipNumbers.get(i), generateIpByte(IpRegion.none), 1};
					// routers always end in 1
					break;
				default:
					ip = new short[]{this.ip[0], this.ip[1], ipNumbers.get(i), 1};
					break;
			}

			if(findDevice(ip) == null) {
				break;
			}
		}
		return ip;
	}

	/**
	 * used to create a (somewhat) realistic, random ip loosely based on the
	 * ipv4 internet map. This is used mostly with the assign ip method, but can
	 * be useful for other things.
	 */
	public static short generateIpByte(IpRegion ipRegion) {
		return (short) (Math.random() * (ipRegion.max - ipRegion.min) + ipRegion.min);
	}

	// registers a url to an ip just so not everything is an ip // ip can only
	// be a player's ip if they buy it
	public boolean addUrl(short[] ip, String address) {
		if(address.matches("^[\\d|\\w]{1,64}\\.\\w{2,3}$") && network.getInternet().getIp(address) == null) { // address regex
			findDevice(ip).setAddress(address);
			return true; // "you have successfully registered the url " + url +
			// " for the ip " + ip;
		}
		return false; // "Sorry, either that URL is already registered, or a bug)."
	}

	/**
	 * Finds the device with the given ip connected to the dns.
	 */
	public Device findDevice(short[] ip) {
		for(Connection c : connections) {
			if(Arrays.equals(ip, c.getClient().getIp())) {
				return c.getClient();
			}
		}
		return null;
	}

	/**
	 * This finds the ip of the given address.
	 */
	public short[] getIp(String address) {
		for(Connection c : connections) {
			if(c.getClient().getAddress().equals(address)) {
				return c.getClient().getIp();
			}
		}
		return null;
	}

	int getRandomNumber() {
		return 4; // chosen by fair dice roll
		// guaranteed to be random
	}
}
