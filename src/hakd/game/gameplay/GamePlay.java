package hakd.game.gameplay;

import com.badlogic.gdx.Gdx;
import hakd.game.Internet;
import hakd.networks.Network;

import java.util.ArrayList;
import java.util.List;

public final class GamePlay {
    private final Internet internet;

    private final List<Character> characters;

    private Thread updateThread;
    private boolean active;

    public GamePlay(Internet internet) {
        this.internet = internet;
        characters = new ArrayList<Character>(internet.getIpNetworkHashMap().size());

//        start();
    }

    private void start() {


        active = true;
        updateThread = new Thread(new Runnable() {
            public float timer = 0;
            public long startTime;

            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                while (active) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Gdx.app.error("GamePlay Thread", "Insomnia", e);
                        e.printStackTrace();
                    }
                    timer += (System.currentTimeMillis() - startTime) / 1000;

                    if (timer >= 10) {
                        timer = 0;
                        updateCharacters();
                    }

                    update();
                }
            }
        });

        updateThread.run();
    }

    private void update() {

    }

    private void updateCharacters() {
        characters.clear();
        for (Network n : internet.getIpNetworkHashMap().values()) {
            Character c = n.getOwner();
            characters.add(c);
        }

    }

    public void dispose() {
        active = false;
    }
}
