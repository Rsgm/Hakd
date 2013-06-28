package hakd.gui;

import hakd.game.gameplay.Player;
import hakd.gui.screens.GameScreen;
import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;
import hakd.other.enumerations.DeviceType;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Room {
	private Player				player;
	private Network				network;
	private List<Device>		devices;

	private Router[]			routerSlots;
	private Dns[]				dnsSlots;
	private Server[]			serverSlots;

	private TiledMap			map;
	private Object[][]			mapObjects;

	private TiledMapTileLayer	floor;
	private TiledMapTileLayer	wall;
	private MapLayer			bounds;		// if it matters, maybe rename this to wall/boundary layer
	private TiledMapTileLayer	objetcts;		// Intractable tiles

	private GameScreen			gameScreen;

	public Room(Player player, GameScreen gameScreen) {
		network = player.getNetwork();
		devices = network.getDevices();

		map = new TmxMapLoader().load("hakd/gui/resources/maps/room" + network.getLevel() + ".tmx");

		floor = (TiledMapTileLayer) map.getLayers().get("floor");
		floor = (TiledMapTileLayer) map.getLayers().get("wall");
		bounds = map.getLayers().get("bounds");
		objetcts = (TiledMapTileLayer) map.getLayers().get("objects");

		int servers = Integer.parseInt((String) map.getProperties().get("servers"));
		int dnss = Integer.parseInt((String) map.getProperties().get("dnss"));
		int routers = Integer.parseInt((String) map.getProperties().get("routers"));

		routerSlots = new Router[routers];
		dnsSlots = new Dns[dnss];
		serverSlots = new Server[servers];
//
		network.setServerLimit(servers);
		network.setDnsLimit(dnss);
		network.setRouterLimit(routers);

		assignDevices();
		buildRoom();

		gameScreen.changeMap(map);

	}

	private void assignDevices() { // TODO make something to check for too many servers, maybe before you buy a room
		ArrayList<Dns> dnss = new ArrayList<Dns>();
		ArrayList<Server> servers = new ArrayList<Server>();
		ArrayList<Router> routers = new ArrayList<Router>();

		for (Device d : devices) {
			if (d.getType() == DeviceType.DNS) {
				dnss.add((Dns) d);
			} else if (d.getType() == DeviceType.ROUTER) {
				routers.add((Router) d);
			} else if (d.getType() == DeviceType.SERVER) {
				servers.add((Server) d);
			}
		}

		dnsSlots = dnss.toArray(new Dns[dnss.size()]);
		serverSlots = servers.toArray(new Server[servers.size()]);
		routerSlots = routers.toArray(new Router[routers.size()]);
	}

	private void buildRoom() {
		mapObjects = new Object[bounds.getObjects().getCount()][3];

		int i = 0;
		for (com.badlogic.gdx.maps.MapObject o : bounds.getObjects()) {
			mapObjects[i][0] = o.getName();

			if (o.getName().matches("[rds]s.*")) {
				mapObjects[i][1] = Integer.parseInt((String) o.getProperties().get("x"));
				mapObjects[i][2] = Integer.parseInt((String) o.getProperties().get("y"));
			}
			i++;
		}
	}

	public Device getDeviceAtTile(Object x, Object y) {
		Device device = null;

		for (Object[] o : mapObjects) {
			if (o[1] == x && o[2] == y) {
				String s = (String) o[0];
				if (s.matches("ss.*")) {
					s = s.replace("ss", "");
					device = serverSlots[Integer.parseInt(s)];
				} else if (s.matches("rs.*")) {
					s = s.replace("rs", "");
					device = routerSlots[Integer.parseInt(s)];
				} else if (s.matches("ds.*")) {
					s = s.replace("ds", "");
					device = dnsSlots[Integer.parseInt(s)];
				}
				break;
			}
		}
		return device;
	}

	public void dispose() {
		map.dispose();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Router[] getRouterSlots() {
		return routerSlots;
	}

	public void setRouterSlots(Router[] routerSlots) {
		this.routerSlots = routerSlots;
	}

	public Dns[] getDnsSlots() {
		return dnsSlots;
	}

	public void setDnsSlots(Dns[] dnsSlots) {
		this.dnsSlots = dnsSlots;
	}

	public Server[] getServerSlots() {
		return serverSlots;
	}

	public void setServerSlots(Server[] serverSlots) {
		this.serverSlots = serverSlots;
	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public TiledMapTileLayer getFloor() {
		return floor;
	}

	public void setFloor(TiledMapTileLayer floor) {
		this.floor = floor;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public Object[][] getMapObjects() {
		return mapObjects;
	}

	public void setMapObjects(Object[][] mapObjects) {
		this.mapObjects = mapObjects;
	}

	public TiledMapTileLayer getWall() {
		return wall;
	}

	public void setWall(TiledMapTileLayer wall) {
		this.wall = wall;
	}

	public MapLayer getBounds() {
		return bounds;
	}

	public void setBounds(MapLayer bounds) {
		this.bounds = bounds;
	}

	public TiledMapTileLayer getObjetcts() {
		return objetcts;
	}

	public void setObjetcts(TiledMapTileLayer objetcts) {
		this.objetcts = objetcts;
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
 * TODO have different virtual screens(todoception make this an implementation or extending class) that return orthocameras
 * or something, matrices maybe?
 * 
 * 
 * To get a new room, you first buy the room. Next you remove the old room, then update the network limits. Finally
 * you generate a new room.
 * 
 * Use seeds to generate the internet, that way you can reload a save easier, maybe, but all mission networks would have to
 * be pre-generated and nothing can ever despawn.
 */