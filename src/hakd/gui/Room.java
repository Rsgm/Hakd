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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Room {
	private Player				player;
	private Network				network;
	private List<Device>		devices;

	private Router[]			routerSlots;
	private Dns[]				dnsSlots;
	private Server[]			serverSlots;

	private TiledMap			map;

	private TiledMapTileLayer	floor;
	private MapLayer			objectLayer;

	private GameScreen			gameScreen;

	public Room(Player player, GameScreen gameScreen) { // TODO this is just blocked until I can finish with the map rendering and stuff
		// I guess what I am really waiting for is a slot tile to be made, that way I can try to populate a room with a network

// network = player.getHome();
//
// devices = network.getDevices();
//
// routerSlots = new Router[network.getRouterLimit()];
// dnsSlots = new Dns[network.getServerLimit()];
// serverSlots = new Server[network.getServerLimit()];
//
// map = new TmxMapLoader().load("src/hakd/gui/resources/maps/room" + network.getlevel() + ".tmx");
//
// assignDevices();
//
// buildRoom();

		map = new TmxMapLoader().load("src/hakd/gui/resources/maps/untitled64.tmx");

		floor = (TiledMapTileLayer) map.getLayers().get("floor");
		objectLayer = map.getLayers().get("object");
		// other tile layer

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

		dnsSlots = (Dns[]) dnss.toArray();
		serverSlots = (Server[]) servers.toArray();
		routerSlots = (Router[]) routers.toArray();
	}

	private void buildRoom() {
		TiledMapTileLayer t = (TiledMapTileLayer) map.getLayers().get(0);

		Cell c = new Cell(); // cells hold tile atributes
		c.setTile(new TiledMapTileSet().getTile(1));

		t.setCell(0, 0, c); // have the layer or map have an attribute of "slot0" up to the max slots with its coordinates
		t.getObjects().get("slot1").getProperties().get(key);
	}

	public void dispose() {
		map.dispose();
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

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Player getPlayer() {
		return player;
	}

	public TiledMapTileLayer getBackground() {
		return floor;
	}

	public MapLayer getObjectLayer() {
		return objectLayer;
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

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setBackground(TiledMapTileLayer background) {
		this.floor = background;
	}

	public void setObjectLayer(MapLayer objectLayer) {
		this.objectLayer = objectLayer;
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
 * TODO have different virtual screens(TODO-inception make this an implementation or extending class) that return orthocameras
 * or something, matrices maybe?
 * 
 * 
 * To get a new room, you first buy the room. Next you remove the old room, then update the network limits. Finally
 * you generate a new room.
 * 
 * Use seeds to generate the internet, that way you can reload a save easier, maybe, but all mission networks would have to
 * be pre-generated and nothing can ever despawn.
 */