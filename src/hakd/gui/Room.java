package hakd.gui;

import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;
import hakd.other.enumerations.DeviceType;

import java.util.ArrayList;

public class Room {
	private Network				network;
	private ArrayList<Device>	devices;

	private int					dnsLimit;
	private int					routerLimit;
	private int					serverLimit;

	ArrayList<Dns>				dnsSlots	= new ArrayList<Dns>();
	ArrayList<Router>			routerSlots	= new ArrayList<Router>();
	ArrayList<Server>			serverSlots	= new ArrayList<Server>();

	// graphics
	private int					floor;									// floor texture
	private int					wall;									// wall texture

	public Room(Network n) {
		network = n;

		devices = n.getDevices();

		dnsLimit = n.getDnsLimit();
		routerLimit = n.getRouterLimit();
		serverLimit = n.getServerLimit();

	}

	private boolean assignDevices() {
		int dnsTotal = 0;
		int routerTotal = 0;
		int serverTotal = 0;

		for (Device d : devices) {
			if (d.getType() == DeviceType.DNS) {
				dnsTotal++;
			}
			if (d.getType() == DeviceType.ROUTER) {
				routerTotal++;
			}
			if (d.getType() == DeviceType.SERVER) {
				serverTotal++;
			}
		}

		for (Device d : devices) {
			if (dnsTotal > dnsLimit && d.getType() == DeviceType.DNS) {
				dnsSlots.add((Dns) d);
				return true;
			} else if (routerTotal > routerLimit && d.getType() == DeviceType.ROUTER) {
				routerSlots.add((Router) d);
				return true;
			} else if (serverTotal > serverLimit && d.getType() == DeviceType.SERVER) {
				serverSlots.add((Server) d);
				return true;
			}

			return false;
		}
	}

	private void buildNewRoom() {

	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}
}

/* Just some place to put my ideas:
 * This may be getting away from the old school look that I first wanted, I will try to keep the menu the same though
 *
 * I have decided to add more to the idea I have in my head. Instead of looking at a computer screen, you will see an
 *
 * orthographic projection of a server room. You will be able to interact with stuff, like open a terminal, check system
 * temps, et cetera. The server room will start out with a desktop in the middle, and could potentially scale up to data
 * center sizes. This will allow for better visuals and a more in control feeling than just being an admin on a terminal.
 * I have a feel that this is a terrible mistake.
 * 
 * That means I will need a placement system for servers, like slots in the room.
 * I will also need graphics and 3d models on top of the 3d models for the internet map.
 * 
 * TODO have different virtual screens(TODO-inception make this an implementation or extending class) that return 
 * 
 * 
 * To get a new room, you first buy the room. Next you remove the old room, then update the network limits. Finnaly
 * you generate a new room.
 * 
 * Also use server levels for what graphic to use, now do I try for 8 graphics or 4?
 */