package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import game.gameplay.Character;
import game.gameplay.City;
import game.gameplay.Player;
import gui.Room;
import gui.screens.GameScreen;
import gui.screens.MapScreen;
import networks.InternetProviderNetwork;
import networks.Network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GamePlay {
    private static final float ROOM_UPDATE_SPEED = 1;
    private static final float CHARACTERMAP_UPDATE_SPEED = 3;
    private static final float CHARACTER_UPDATE_SPEED = 1;

    private final Internet internet;
    private Player player;
    private final GameScreen gameScreen;

    private final HashMap<String, Character> characterMap;
    private final HashMap<String, City> cityMap;

    private Thread updateThread;
    private boolean active;

    public GamePlay(GameScreen screen, String name) {
        this.gameScreen = screen;
        cityMap = new HashMap<String, City>();
        characterMap = new HashMap<String, Character>();

        makeCities(1f, 4000);
        makeCities(.4f, 6000);
        makeCities(.2f, 8000);

        internet = new Internet(cityMap);

        makeCompanies();
        makeAgencies();
        makeHackers();
        makePlayer(name);

        startUpdateThread();
    }

    private void makeCompanies() {

    }

    private void makeAgencies() {

    }

    private void makeHackers() {

    }

    private void makePlayer(String name) {
        List<City> citiesSuffled = new ArrayList<City>(cityMap.values());
        Collections.shuffle(citiesSuffled);
        City playerCity = null;

        for (City c : citiesSuffled) {
            playerCity = c;
            for (Network n : playerCity.getNetworks().values()) {
                if (n instanceof InternetProviderNetwork) {
                    break;
                }
            }
        }

        player = new Player(name, playerCity);

        Room room = new Room(player, this, Room.RoomMap.room1);
        gameScreen.setRoom(room);
        gameScreen.setPlayer(player);
    }

    private void startUpdateThread() {
        active = true;
        updateThread = new Thread(new Runnable() {
            public float timer = 0;
            public long lastTime;

            @Override
            public void run() {
                lastTime = System.currentTimeMillis();

                updateCharacterMap();
                while (active) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Gdx.app.error("GamePlay Thread", "Insomnia", e);
                        e.printStackTrace();
                    }
                    float deltaTime = (System.currentTimeMillis() - lastTime) / 1000.0f;
                    timer += deltaTime;

                    if (timer % CHARACTERMAP_UPDATE_SPEED <= deltaTime) {
                        updateCharacterMap();
                    }

                    if (timer % CHARACTER_UPDATE_SPEED <= deltaTime) {
                        updateCharacters();
                    }

                    lastTime = System.currentTimeMillis();
                }
            }
        });
        updateThread.start();
    }

    private void updateCharacters() {
        for (Character c : characterMap.values()) {
            c.update();
        }
    }

    private void updateCharacterMap() {
        characterMap.clear();
        for (Network n : internet.getNetworkMap().values()) {
            Character c = n.getOwner();
            characterMap.put(c.getName(), c);
        }
    }

    private void makeCities(float minDensity, int minDist) {
        float w = City.width / 2;
        float h = City.height / 2;
        int density = 500; // how close together to pick the possible city generation points (this should be smaller than the map density)

        for (int y = 0; y < density; y++) {
            l:
            for (int x = 0; x < density; x++) { // maybe use separate threads to get the values and set them to arrays. e.g. float[250][5000] ten times
                final Vector2 pos = new Vector2();
                pos.x = (x - density / 2) * (MapScreen.NOISE_DISPLAY_SIZE - 5000) / density + w;
                pos.y = (y - density / 2) * (MapScreen.NOISE_DISPLAY_SIZE - 5000) / density + h;
                float f = (float) Noise.DENSITY.getValue(pos.x, 0, pos.y);

                if (f < minDensity) {
                    continue;
                }

                for (City c : cityMap.values()) {
                    if (c.getPosition().dst(pos) < minDist) {
                        continue l;
                    }
                }

                final City city = new City(pos);
                cityMap.put(city.getName(), city);
            }
            Gdx.app.debug("Generating Cities", (float) y / density * 100 + "% done");
        }
    }

    public void dispose() {
        active = false;
    }

    public HashMap<String, City> getCityMap() {
        return cityMap;
    }

    public Internet getInternet() {
        return internet;
    }

    public Player getPlayer() {
        return player;
    }

    public HashMap<String, Character> getCharacterMap() {
        return characterMap;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }
}
