package gui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import game.GamePlay;
import game.Hakd;
import game.gameplay.Character;
import game.gameplay.City;
import game.gameplay.Player;
import gui.windows.device.DeviceScene;
import networks.InternetProviderNetwork;
import networks.Network;
import networks.NetworkFactory;
import networks.devices.Device;
import networks.devices.DeviceFactory;
import other.Util;

import java.util.*;

public class Room {
    public static int TILE_SIZE;
    public static int ROOM_HEIGHT;

    private final Character player;
    private final Network network;
    private final Map<String, Device> devices;
    private final Map<Vector2, DeviceTile> deviceTileMap;

    private final TiledMap map;

    private final TiledMapTileLayer floorLayer;
    private final TiledMapTileLayer wallLayer;
    private final MapLayer deviceLayer; // if it matters, maybe rename this to wall/boundary layer
    private final TiledMapTileLayer objectLayer; // Interactable tiles?

    private final GamePlay gamePlay;

    public Room(Character player, GamePlay gamePlay, RoomMap roomMap) {
        this.player = player;
        this.gamePlay = gamePlay;

        ((Player) player).setRoom(this);
        deviceTileMap = new HashMap<Vector2, DeviceTile>();


        map = new TmxMapLoader().load(Util.ASSETS + "maps/" + roomMap.toString() + ".tmx");

        floorLayer = (TiledMapTileLayer) map.getLayers().get("floor");
        wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        deviceLayer = map.getLayers().get("devices");
        objectLayer = (TiledMapTileLayer) map.getLayers().get("objects");

        ROOM_HEIGHT = floorLayer.getHeight();
        TILE_SIZE = (int) floorLayer.getTileWidth();

        buildRoom();
        gamePlay.getGameScreen().setRenderer(new IsometricTiledMapRenderer(map, 1f / TILE_SIZE)); // don't let this round, that is why 1 is a floatmap

        if (player.getNetwork() == null) {
            City playerCity = player.getCity();
            List<InternetProviderNetwork> isps = new ArrayList<InternetProviderNetwork>(5);
            for (Network n : playerCity.getNetworks().values()) {
                if (n instanceof InternetProviderNetwork) {
                    isps.add((InternetProviderNetwork) n);
                }
            }

            network = NetworkFactory.createPlayerNetwork((Player) player, playerCity, gamePlay.getInternet());
            InternetProviderNetwork isp = isps.get((int) (Math.random() * isps.size()));
            gamePlay.getInternet().addNetworkToInternet(network, isp);
            playerCity.addNetwork(network);
            network.setDeviceLimit(1);

            Device device = DeviceFactory.createDevice(0, Device.DeviceType.SERVER);
            HakdSprite tile = new HakdSprite(Hakd.assets.get("nTextures.txt", TextureAtlas.class).findRegion("d0"));
            tile.setSize(tile.getWidth() / TILE_SIZE, tile.getHeight() / TILE_SIZE);
            tile.setObject(device);
            device.setTile(tile);
            network.addDevice(device);
        } else {
            network = player.getNetwork();
        }
        int deviceLimit = deviceLayer.getObjects().getCount();
        network.setDeviceLimit(deviceLimit);
        devices = network.getDevices();
    }

    private void buildRoom() {
        for (MapObject o : deviceLayer.getObjects()) {
            if (!(o instanceof RectangleMapObject)) {
                continue;
            }
            RectangleMapObject r = (RectangleMapObject) o; // all objects on the device layer will be rectangles

            int x = Integer.parseInt((String) r.getProperties().get("x"));
            int y = Integer.parseInt((String) r.getProperties().get("y"));
            DeviceTile tile = new DeviceTile(x, y);
            deviceTileMap.put(tile.isoPos, tile);
        }
    }

    /**
     * Finds the device at the specified isometric coordinates.
     *
     * @return The device found.
     * The string "other", if no device was found there.
     * Null if the tile in the map does not have an object.
     */

    public DeviceTile getDeviceAtTile(int x, int y) {
        return deviceTileMap.get(new Vector2(x, y));
    }

    public void addDevice(Device device) {
        if (deviceTileMap.containsKey(device.getIsoPos())) {
            deviceTileMap.get(device.getIsoPos()).device = device;
        } else { // if the device is not in the room, assign it to a random tile
            for (DeviceTile tile : deviceTileMap.values()) { // due to the nature of a set, this should be considered random
                if (tile.device == null) {
                    Vector2 v = tile.getIsoPos();
                    device.setIsoPos(v);
                    tile.setDevice(device);
                    break;
                }
            }
        }

        device.setDeviceScene(new DeviceScene(gamePlay.getGameScreen(), device));
    }

    public void removeDevice(Device device) {
        deviceTileMap.get(device.getIsoPos()).device = null;
    }

    public enum RoomMap {
        room1(), room2(), room3(), room4(), room5(), room6();

        private RoomMap() {
        }
    }

    public void dispose() {
        map.dispose();
    }

    public static class DeviceTile {
        final Vector2 isoPos;
        Device device;

        private static final Sprite TABLE;
        private final HakdSprite table; // the default sprite if there is no device, or the empty device tile

        static {
            TABLE = new Sprite(Hakd.assets.get("nTextures.txt", TextureAtlas.class).findRegion("d-1"));
            TABLE.setSize(TABLE.getWidth() / TILE_SIZE, TABLE.getHeight() / TILE_SIZE);
        }

        public DeviceTile(int x, int y) {
            isoPos = new Vector2(x, y);

            table = new HakdSprite(TABLE);
            Vector2 pos = Util.isoToOrtho(isoPos.x, isoPos.y);
            table.setPosition(pos.x, pos.y);
        }

        public Vector2 getIsoPos() {
            return isoPos;
        }

        public HakdSprite getTile() {
            if (device == null || device.getTile() == null) {
                return table;
            }

            return device.getTile();
        }

        public Device getDevice() {
            return device;
        }

        public void setDevice(Device device) {
            this.device = device;

            if (device.getTile() != null) {
                device.getTile().setPosition(table.getX(), table.getY());
            }
        }
    }

    public Character getPlayer() {
        return player;
    }

    public Network getNetwork() {
        return network;
    }

    public TiledMap getMap() {
        return map;
    }

    public TiledMapTileLayer getFloorLayer() {
        return floorLayer;
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }

    public TiledMapTileLayer getWallLayer() {
        return wallLayer;
    }

    public MapLayer getDeviceLayer() {
        return deviceLayer;
    }

    public TiledMapTileLayer getObjectLayer() {
        return objectLayer;
    }

    public Map<String, Device> getDevices() {
        return devices;
    }

    public Map<Vector2, DeviceTile> getDeviceTileMap() {
        return Collections.unmodifiableMap(deviceTileMap);
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
 * I have a feeling that this is a terrible mistake. // (months later trying to figure this out) probably due to the amount of work it will(has in hindsight) coasted
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
 *
 * Is this even relevant anymore?
 * (talking to my past self again) Not really, but I will keep it as a reminder of what I saw in this game at that time.
 */
