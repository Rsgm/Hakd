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
import hakd.other.Util;

import java.util.ArrayList;
import java.util.List;

public final class Room {
	private Player player;
	private Network network;
	private final List<Device> devices;
	private final List<EmptyDeviceTile> emptyDeviceTiles;

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

		devices = network.getDevices();
		emptyDeviceTiles = new ArrayList<EmptyDeviceTile>();

		map = new TmxMapLoader().load("hakd/gui/resources/maps/" + roomMap.toString() + ".tmx");

		floor = (TiledMapTileLayer) map.getLayers().get("floor");
		floor = (TiledMapTileLayer) map.getLayers().get("wall");
		bounds = map.getLayers().get("bounds");
		objetcts = (TiledMapTileLayer) map.getLayers().get("objects");

		int deviceLimit = Integer.parseInt((String) map.getProperties().get("devices"));
		network.setDeviceLimit(deviceLimit);

		buildRoom();
		gameScreen.changeMap(map);
	}

	private void buildRoom() {
		mapObjects = new Object[bounds.getObjects().getCount()][3];

		int i = 0;
		for(MapObject o : bounds.getObjects()) {
			mapObjects[i][0] = o.getName();
			mapObjects[i][1] = Integer.parseInt((String) o.getProperties().get("x"));
			mapObjects[i][2] = Integer.parseInt((String) o.getProperties().get("y"));
			i++;
		}

		// set sprites(tile) of network devices
		for(Device d : devices) {
			d.setTile(new Sprite(Assets.nearestTextures.findRegion("d" + d.getLevel())));
			d.getTile().setSize(d.getTile().getWidth() / GameScreen.tileSize, d.getTile().getHeight() / GameScreen.tileSize);

			float[] ortho = Util.isoToOrtho(d.getIsoX(), d.getIsoY(), floor.getHeight());

			d.getTile().setPosition(ortho[0], ortho[1]);
		}

		// create unused device spaces and set their sprite to "d-1"
		for(Object[] o : mapObjects) {
			if(o[0] instanceof String && o[1] instanceof Integer && o[2] instanceof Integer) {

				if(((String) o[0]).matches("device") && getObjectAtTile((Integer) o[1], (Integer) o[2]).equals("empty")) {
					EmptyDeviceTile emptyDeviceTile = new EmptyDeviceTile((Integer) o[1], (Integer) o[2]);

					emptyDeviceTile.setTile(new Sprite(Assets.nearestTextures.findRegion("d-1")));
					emptyDeviceTile.getTile().setSize(emptyDeviceTile.getTile().getWidth() / GameScreen.tileSize, emptyDeviceTile.getTile().getHeight() / GameScreen.tileSize);

					float[] ortho = Util.isoToOrtho((Integer) o[1], (Integer) o[2], floor.getHeight());

					emptyDeviceTile.getTile().setPosition(ortho[0], ortho[1]);

					emptyDeviceTiles.add(emptyDeviceTile);
				}
			}
		}
		network.setEmptyDeviceTiles(emptyDeviceTiles);
	}

	/**
	 * Finds the device at the specified isometric coordinates.
	 *
	 * @return The device found.
	 *         The string "other", if no device was found there.
	 *         Null if the tile in the map does not have an object.
	 */
	public Object getObjectAtTile(int x, int y) {
		for(Device d : devices) {
			if(x == d.getIsoX() && y == d.getIsoY()) {
				return d;
			}
		}

		for(EmptyDeviceTile e : emptyDeviceTiles) {
			if(x == e.getIsoX() && y == e.getIsoY()) {
				return e;
			}
		}

		for(Object[] o : mapObjects) {
			if(((Integer) o[1]) == x && ((Integer) o[2]) == y) {
				if(o[0].equals("device")) {
					return "empty";
				} else {
					return "other";
				}
			}
		}

		return null;
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

	public List<Device> getDevices() {
		return devices;
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
 * (done)to-do have different virtual screens that return orthocameras or something, matrices maybe?
 * (They have render methods.)
 *
 * To get a new room, you first buy the room. Next you remove the old room, then
 * update the network limits. Finally you generate a new room.
 * 
 * Use seeds to generate the internet, that way you can reload a save easier,
 * maybe, but all mission networks would have to be pre-generated and nothing
 * can ever despawn.
 */