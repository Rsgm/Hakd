package hakd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import hakd.game.gameplay.Character;
import hakd.game.gameplay.City;
import hakd.game.gameplay.Player;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.MapScreen;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;
import hakd.networks.NetworkFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public final class GamePlay {
    private final Internet internet;
    private Player player;
    private final GameScreen screen;

    private final HashMap<String, Character> characterMap;
    private final HashMap<String, City> cityMap;

    private Thread updateThread;
    private boolean active;

    private int tutorialPos = 0;
    private boolean tutorialEnabled = true;

    public GamePlay(GameScreen screen, String name) {
        this.screen = screen;
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

        startCharacterUpdateTrhead();
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
        City playerCity = citiesSuffled.get(0); // I only initialize it because intellij yells at me
        List<InternetProviderNetwork> isps = new ArrayList<InternetProviderNetwork>(5);

        for (City c : citiesSuffled) {
            playerCity = c;

            isps.clear();
            for (Network n : playerCity.getNetworks().values()) {
                if (n instanceof InternetProviderNetwork) {
                    isps.add((InternetProviderNetwork) n);
                }
            }

            if (!isps.isEmpty()) {
                break;
            }
        }

        player = new Player(name, screen, playerCity);
        Network n = NetworkFactory.createPlayerNetwork(player, playerCity, internet);

        final InternetProviderNetwork isp = isps.get((int) (Math.random() * isps.size()));
        internet.addNetworkToInternet(n, isp);
        playerCity.addNetwork(n);
        screen.setPlayer(player);
    }

    private void startCharacterUpdateTrhead() {
        active = true;
        updateThread = new Thread(new Runnable() {
            public float timer = 0;
            public long lastTime;

            @Override
            public void run() {
                lastTime = System.currentTimeMillis();

                updateCharacterMap();
                while (active) {
                    timer += (System.currentTimeMillis() - lastTime) / 1000f;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Gdx.app.error("GamePlay Thread", "Insomnia", e);
                        e.printStackTrace();
                    }

                    if (timer >= 3) {
                        timer = 0;
                        updateCharacterMap();
                    }

                    update();
                    lastTime = System.currentTimeMillis();
                }
            }
        });
        updateThread.start();
    }

    private void update() {
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
        int density = 500; // how close to gether to pick the possible city generation points (this should be smaller than the map density)

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

    public void openTutorial() {
        if (!tutorialEnabled) {
            return;
        }

        tutorialPos++;
        // tutorial code
    }

    public void dispose() {
        active = false;
    }

    public HashMap<String, City> getCityMap() {
        return cityMap;
    }

    public int getTutorialPos() {
        return tutorialPos;
    }

    public void setTutorialPos(int tutorialPos) {
        this.tutorialPos = tutorialPos;
    }

    public boolean isTutorialEnabled() {
        return tutorialEnabled;
    }

    public void setTutorialEnabled(boolean tutorialEnabled) {
        this.tutorialEnabled = tutorialEnabled;
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
}
