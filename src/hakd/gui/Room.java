package hakd.gui;

import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;
import hakd.other.enumerations.DeviceType;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Room {
	private Network				network;
	private ArrayList<Device>	devices;

	private int					dnsLimit;
	private int					routerLimit;
	private int					serverLimit;

	private ArrayList<Dns>		dnsSlots	= new ArrayList<Dns>();
	private ArrayList<Router>	routerSlots	= new ArrayList<Router>();
	private ArrayList<Server>	serverSlots	= new ArrayList<Server>();

	private TiledMap			map;

	public Room(Network n) {
		network = n;
		devices = n.getDevices();
		map = new TmxMapLoader().load("src/hakd/gui/resources/maps/untitled" + n.getLevel() + ".tmx");

		dnsLimit = n.getDnsLimit();
		routerLimit = n.getRouterLimit();
		serverLimit = n.getServerLimit();

		assignDevices();

		buildRoom();

	}

	private void assignDevices() {
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

		for (int i = 0; i < devices.size(); i++) {
			if (dnsTotal > dnsLimit && devices.get(i).getType() == DeviceType.DNS) {
				dnsSlots.add((Dns) devices.get(i));
			} else if (routerTotal > routerLimit && devices.get(i).getType() == DeviceType.ROUTER) {
				routerSlots.add((Router) devices.get(i));
			} else if (serverTotal > serverLimit && devices.get(i).getType() == DeviceType.SERVER) {
				serverSlots.add((Server) devices.get(i));
			}
		}
	}

	private void buildRoom() {
		TiledMapTileLayer t = (TiledMapTileLayer) map.getLayers().get(0);

		Cell c = new Cell(); // cells hold tile atributes
		c.setTile(new TiledMapTileSet().getTile(1));

		t.setCell(0, 0, c); // have the layer or map have an attribute of "slot0" up to the max slots with its coordinates
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}
}

/* Just some place to put my ideas:
 * This may be getting away from the old school look that I first wanted, I will try to keep the menu the same though.
 *
 * I have decided to add more to the idea I have in my head. Instead of looking at a computer screen, you will see an
 * isometric projection of a server room. You will be able to interact with stuff, like open a terminal, check system
 * temps, etc. The server room will start out with a desktop in the middle, and could potentially scale up to data
 * center sizes. This will allow for better visuals and a more in control feeling than just being an admin on a terminal.
 * I have a feel that this is a terrible mistake.
 * 
 * That means I will need a placement system for servers, like slots in the room.
 * I will also need graphics and 3d models on top of the 3d projection models for the internet map.
 * 
 * TODO have different virtual screens(TODO-inception make this an implementation or extending class) that return 
 * 
 * 
 * To get a new room, you first buy the room. Next you remove the old room, then update the network limits. Finally
 * you generate a new room.
 * 
 * Use seeds to generate the internet, that way you can reload a save easier, maybe, but all mission networks would have to
 * be pre-generated and nothing can ever despawn.
 */