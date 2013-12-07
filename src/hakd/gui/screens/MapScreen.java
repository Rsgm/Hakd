package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hakd.game.Internet;
import hakd.game.gameplay.Player;
import hakd.gui.Assets;
import hakd.gui.input.MapInput;
import hakd.networks.BackboneProviderNetwork;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;

import java.util.ArrayList;
import java.util.List;

public final class MapScreen extends HakdScreen {
    private Player player;
    private Network network;
    private final Internet internet;

    private final GameScreen gameScreen;
    private final MapInput input;
    private float time = 0;

    private final SpriteBatch territoryBatch;

    private final List<Sprite> networkSprites;
    private final List<Sprite> ispSprites;
    private final List<Sprite> territorySprites;
    private final List<Sprite> backboneSprites;
    private final List<Sprite> connectionLineSprites;
    private final List<Sprite> parentLineSprites;
    private final List<Sprite> backboneLineSprites;

    public MapScreen(Game game, GameScreen gameScreen, Internet internet) {
        super(game);

        this.internet = internet;
        this.gameScreen = gameScreen;

        territoryBatch = new SpriteBatch();
        territoryBatch.setShader(Assets.shaders.get(Assets.Shader.TERRITORY));

        networkSprites = new ArrayList<Sprite>(internet.getIpNetworkHashMap().size());
        ispSprites = new ArrayList<Sprite>(internet.getInternetProviderNetworks().size());
        territorySprites = new ArrayList<Sprite>(internet.getInternetProviderNetworks().size());
        backboneSprites = new ArrayList<Sprite>(internet.getBackboneProviderNetworks().size());
        connectionLineSprites = new ArrayList<Sprite>(50);
        parentLineSprites = new ArrayList<Sprite>(internet.getIpNetworkHashMap().size());
        backboneLineSprites = new ArrayList<Sprite>(internet.getBackboneProviderNetworks().size());

        cam = new OrthographicCamera();
        ((OrthographicCamera) cam).setToOrtho(false, width, height);

        input = new MapInput(this);
    }

    @Override
    public void show() {
        super.show();

        cam.position.x = 0;
        cam.position.y = 0;
        ((OrthographicCamera) cam).zoom = 2;
        cam.update();

        reloadSprites();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

//        System.out.println((int) (1 / delta));
        time += delta;
        if (time >= 1) {
            reloadSprites();
        }

        territoryBatch.setProjectionMatrix(cam.combined);
        territoryBatch.begin();
        for (Sprite s : territorySprites) {
            s.draw(territoryBatch);
        }
        territoryBatch.end();

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        for (Sprite s : backboneLineSprites) {
            s.draw(batch);
        }
        for (Sprite s : parentLineSprites) {
            s.draw(batch);
        }
        for (Sprite s : backboneSprites) {
            s.draw(batch);
        }
        for (Sprite s : ispSprites) {
            s.draw(batch);
        }
        for (Sprite s : networkSprites) {
            s.draw(batch);
        }
        for (Sprite s : connectionLineSprites) {
            s.draw(batch);
        }
        batch.end();
    }

    private void reloadSprites() {
        networkSprites.clear();
        ispSprites.clear();
        territorySprites.clear();
        backboneSprites.clear();
        connectionLineSprites.clear();
        parentLineSprites.clear();
        backboneLineSprites.clear();

        for (Network n : internet.getIpNetworkHashMap().values()) {
            if (n instanceof BackboneProviderNetwork) {
                backboneSprites.add(n.getMapIcon());
                backboneLineSprites.addAll(((BackboneProviderNetwork) n).getBackboneConnectionLines());
            } else if (n instanceof InternetProviderNetwork) {
                ispSprites.add(n.getMapIcon());
                parentLineSprites.add(n.getMapParentLine());
                territorySprites.add(((InternetProviderNetwork) n).getTerritory());
            } else {
                networkSprites.add(n.getMapIcon());
                parentLineSprites.add(n.getMapParentLine());
            }

            //			for(Connection c : n.getconnections) {
            //			    connectionLineSprites.addc.getLine();
            //			}

        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(gameScreen.getInput());
        time = 0;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }
}
