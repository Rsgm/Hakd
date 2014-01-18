package hakd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import hakd.game.gameplay.Character;
import hakd.game.gameplay.City;
import hakd.game.gameplay.Player;
import hakd.gui.screens.GameScreen;
import hakd.gui.screens.MapScreen;
import hakd.gui.windows.TutorialWindow;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;
import hakd.networks.NetworkFactory;

import java.util.ArrayList;
import java.util.List;

public final class GamePlay {
    private final Internet internet;
    private Player player;
    private final GameScreen screen;

    private final List<hakd.game.gameplay.Character> characterList;
    private final List<City> cityList;

    private Thread updateThread;
    private boolean active;

    private int tutorialPos = 0;
    private boolean tutorialEnabled = true;

    public GamePlay(GameScreen screen, String name) {
        this.screen = screen;
        cityList = new ArrayList<City>();
        characterList = new ArrayList<Character>();

        makeCities(1f, 4000);
        makeCities(.4f, 6000);
        makeCities(.2f, 8000);

        internet = new Internet(cityList);
        ((ArrayList) characterList).ensureCapacity(internet.getIpNetworkHashMap().size());

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
        City playerCity = cityList.get(0);
        List<InternetProviderNetwork> isps = new ArrayList<InternetProviderNetwork>(5);

        for (int i = 0; i < cityList.size() * 4; i++) {
            playerCity = cityList.get((int) (Math.random() * cityList.size()));

            isps.clear();
            for (Network n : playerCity.getNetworks()) {
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

                updateCharacterList();
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
                        updateCharacterList();
                    }

                    update();
                    lastTime = System.currentTimeMillis();
                }
            }
        });
        updateThread.start();
    }

    private void update() {
        for (Character c : characterList) {
            c.update();
        }
    }

    private void updateCharacterList() {
        characterList.clear();
        for (Network n : internet.getIpNetworkHashMap().values()) {
            Character c = n.getOwner();
            characterList.add(c);
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

                for (City c : cityList) {
                    if (c.getPosition().dst(pos) < minDist) {
                        continue l;
                    }
                }

                cityList.add(new City(pos));
            }
            Gdx.app.debug("Generating Cities", (float) y / density * 100 + "% done");
        }

    }

    public void openTutorial() {
        if (!tutorialEnabled) {
            return;
        }

        tutorialPos++;
        TutorialWindow t = new TutorialWindow(tutorialPos);

        t.open();
    }

    public void dispose() {
        active = false;
    }

    public List<City> getCityList() {
        return cityList;
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

    public List<Character> getCharacterList() {
        return characterList;
    }
}
