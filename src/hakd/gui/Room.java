package hakd.gui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import hakd.game.gameplay.Player;
import hakd.gui.screens.GameScreen;
import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.networks.devices.Device.DeviceType;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;
import hakd.other.Util;

import java.util.List;

public final class Room {
	private Player player;
	private Network network;
	private final List<Dns> dnss;
	private final List<Server> servers;
	private final List<Router> routers;
	private final List<Device> otherDevices;

	private TiledMap map;
	private Object[][] mapObjects;

	private TiledMapTileLayer floor;
	private TiledMapTileLayer wall;
	private MapLayer bounds; // if it matters, maybe rename this to
	// wall/boundary layer
	private TiledMapTileLayer objetcts; // Intractable tiles

	private GameScreen gameScreen;

	public Room(Player player, GameScreen gameScreen, RoomMap roomMap) {
		this.player = player;
		network = player.getNetwork();

		dnss = network.getDnss();
		servers = network.getServers();
		routers = network.getRouters();
		otherDevices = network.getOtherDevices();

		map = new TmxMapLoader().load("hakd/gui/resources/maps/" + roomMap.toString() + ".tmx");

		floor = (TiledMapTileLayer) map.getLayers().get("floor");
		floor = (TiledMapTileLayer) map.getLayers().get("wall");
		bounds = map.getLayers().get("bounds");
		objetcts = (TiledMapTileLayer) map.getLayers().get("objects");

		int servers = Integer.parseInt((String) map.getProperties().get("servers"));
		int dnss = Integer.parseInt((String) map.getProperties().get("dnss"));
		int routers = Integer.parseInt((String) map.getProperties().get("routers"));

		network.setServerLimit(servers);
		network.setDnsLimit(dnss);
		network.setRouterLimit(routers);

		buildRoom();

		gameScreen.changeMap(map);
	}

	private void buildRoom() {
		mapObjects = new Object[bounds.getObjects().getCount()][3];

		int i = 0;
		for(MapObject o : bounds.getObjects()) {
			mapObjects[i][0] = o.getName();

			if(o.getName().matches("[rds]s.*")) {
				mapObjects[i][1] = Integer.parseInt((String) o.getProperties().get("x"));
				mapObjects[i][2] = Integer.parseInt((String) o.getProperties().get("y"));
			}
			i++;
		}

		for(Dns d : dnss) {
		}
		for(Router r : routers) {

		}
		for(Server s : servers) {
			s.setTile(new Sprite(Assets.nearestTextures.findRegion("s" + s.getLevel())));
			s.getTile().setSize(s.getTile().getWidth() / GameScreen.tileSize, s.getTile().getHeight() / GameScreen.tileSize);

			int[] isoPos = findDeviceCoords(s, DeviceType.SERVER); // position
			// in iso
			float[] ortho = Util.isoToOrtho(isoPos[0], isoPos[1], floor.getHeight());

			s.getTile().setPosition(ortho[0], ortho[1]);
		}
	}

	// returns the position of a device in isometric coordinates
	public int[] findDeviceCoords(Device d, DeviceType type) {
		int index;
		switch(type) {
			case DNS:
				index = network.getDnss().indexOf(d);
				break;
			case ROUTER:
				index = network.getRouters().indexOf(d);
				break;
			default:
				index = network.getServers().indexOf(d);
				break;
		}

		String s;
		for(Object[] o : mapObjects) {
			s = ((String) o[0]).replace("ss", "");
			if(Integer.parseInt(s) == index) {
				return new int[]{(Integer) o[1], (Integer) o[2]};
			}
		}
		return null;
	}

	public Device getDeviceAtTile(Object x, Object y) {
		Device device = null;

		for(Object[] o : mapObjects) {
			if(o[1] == x && o[2] == y) {
				String s = (String) o[0];
				if(s.matches("ss.*")) {
					s = s.replace("ss", "");
					device = servers.get(Integer.parseInt(s));
				} else if(s.matches("rs.*")) {
					s = s.replace("rs", "");
					device = routers.get(Integer.parseInt(s));
				} else if(s.matches("ds.*")) {
					s = s.replace("ds", "");
					device = dnss.get(Integer.parseInt(s));
				}
				break;
			}
		}
		return device;
	}

	public enum RoomMap {
		room1(), room2(), room3(), room4(), room5(), room6();

		private RoomMap() {
		}
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

	public List<Dns> getDnss() {
		return dnss;
	}

	public List<Server> getServers() {
		return servers;
	}

	public List<Router> getRouters() {
		return routers;
	}

	public List<Device> getOtherDevices() {
		return otherDevices;
	}
}

/*
 * Just some place to put my ideas: This may be getting away from the old school
 * look that I first wanted, I will try to keep the menu the same though.
 * 
 * I have decided to add more to the idea I have in my head. Instead of looking
 * at a computer screen, you will see an isometric projection of a server room.
 * You will be able to interact with stuff, like open a terminal, check system
 * temps, etc. The server room will start out with a desktop in the middle, and
 * could potentially scale up to data center sizes. This will allow for better
 * visuals and a more in control feeling than just being an admin on a terminal.
 * I have a feel that this is a terrible mistake.
 * 
 * That means I will need a placement system for servers, like slots in the
 * room. I will also need graphics and 3d models on top of the 3d projection
 * models for the internet map.
 * 
 * TODO have different virtual screens(todoception make this an implementation
 * or extending class) that return orthocameras or something, matrices maybe?
 * 
 * 
 * To get a new room, you first buy the room. Next you remove the old room, then
 * update the network limits. Finally you generate a new room.
 * 
 * Use seeds to generate the internet, that way you can reload a save easier,
 * maybe, but all mission networks would have to be pre-generated and nothing
 * can ever despawn.
 */