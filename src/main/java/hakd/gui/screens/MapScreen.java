package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import hakd.game.Internet;
import hakd.game.Noise;
import hakd.game.gameplay.City;
import hakd.gui.Assets;
import hakd.gui.input.MapInput;
import hakd.networks.BackboneProviderNetwork;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.ModuleBase;
import libnoiseforjava.util.ColorCafe;
import libnoiseforjava.util.ImageCafe;
import libnoiseforjava.util.NoiseMap;
import libnoiseforjava.util.RendererImage;

import java.util.HashSet;
import java.util.Set;

public class MapScreen extends HakdScreen {
    private final Internet internet;

    private final GameScreen gameScreen;
    private final MapInput input;
    private float time = 0;

    private final SpriteBatch territoryBatch;

    private final Set<Sprite> networkSprites;
    private final Set<Sprite> ispSprites;
    private final Set<Sprite> territorySprites;
    private final Set<Sprite> backboneSprites;
    private final Set<Sprite> connectionLineSprites;
    private final Set<Sprite> parentLineSprites;
    private final Set<Sprite> backboneLineSprites;

    private Texture land;
    private Texture density;
    private Texture politics;
    private Texture ethics;
    private Texture country;
    private Texture income;
    private Texture crime;

    private Noise.NoiseType currentBackground;

    public static final int NOISE_GENERATION_SIZE = 512; // how many points to draw. default: 512(must be power of two), best quality would be around 2500
    public static final int NOISE_DISPLAY_SIZE = 50000; // how large of an area to spread the points out on. default: 50000
//    private overlay mapOverlay;

    public MapScreen(Game game, Internet internet) {
        super(game);

        this.internet = internet;
        this.gameScreen = (GameScreen) game.getScreen();

        territoryBatch = new SpriteBatch();
        networkSprites = new HashSet<Sprite>(internet.getNetworkMap().size());
        ispSprites = new HashSet<Sprite>(internet.getInternetProviderNetworksMap().size());
        territorySprites = new HashSet<Sprite>(internet.getInternetProviderNetworksMap().size());
        backboneSprites = new HashSet<Sprite>(internet.getBackboneProviderNetworksMap().size());
        connectionLineSprites = new HashSet<Sprite>(50);
        parentLineSprites = new HashSet<Sprite>(internet.getNetworkMap().size());
        backboneLineSprites = new HashSet<Sprite>(internet.getBackboneProviderNetworksMap().size());

        territoryBatch.setShader(Assets.shaders.get(Assets.Shader.TERRITORY));

        try {
            generateNoiseTexture(Noise.NoiseType.TERRAIN);
            generateNoiseTexture(Noise.NoiseType.DENSITY);
            generateNoiseTexture(Noise.NoiseType.COUNTRY);
            generateNoiseTexture(Noise.NoiseType.ETHICS);
            generateNoiseTexture(Noise.NoiseType.POLITICS);
            generateNoiseTexture(Noise.NoiseType.INCOME);
            generateNoiseTexture(Noise.NoiseType.CRIME);
        } catch (ExceptionInvalidParam exceptionInvalidParam) {
            exceptionInvalidParam.printStackTrace();
        }
        currentBackground = Noise.NoiseType.TERRAIN;

        cam = new OrthographicCamera();
        ((OrthographicCamera) cam).setToOrtho(false, width, height);

        input = new MapInput(this);
    }

    @Override
    public void show() {
        super.show();

        cam.position.set(gameScreen.getPlayer().getNetwork().getPos(), 0);
        ((OrthographicCamera) cam).zoom = 2;
        cam.update();

        reloadSprites();

//        mapOverlay = new overlay();

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
        switch (currentBackground) {
            case TERRAIN:
                territoryBatch.draw(land, -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case DENSITY:
                territoryBatch.draw(density, -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case POLITICS:
                territoryBatch.draw(politics, -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case ETHICS:
                territoryBatch.draw(ethics, -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case COUNTRY:
                territoryBatch.draw(country, -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case INCOME:
                territoryBatch.draw(income, -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
        }
        territoryBatch.end();

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
//        for (Sprite s : territorySprites) {
//            s.draw(batch);
//        }
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

        for (City c : game.getGamePlay().getCityMap().values()) {
            c.getIcon().draw(batch);
            Assets.textFont.draw(batch, c.getName(), c.getPosition().x, c.getPosition().y + 80);
        }
        batch.end();

//        mapOverlay.render();
    }

    private void reloadSprites() {
        networkSprites.clear();
        ispSprites.clear();
        territorySprites.clear();
        backboneSprites.clear();
        connectionLineSprites.clear();
        parentLineSprites.clear();
        backboneLineSprites.clear();

        for (Network n : internet.getNetworkMap().values()) {
            if (n instanceof BackboneProviderNetwork) {
                backboneSprites.add(n.getMapIcon());
                backboneLineSprites.addAll(((BackboneProviderNetwork) n).getBackboneConnectionLines());
            } else if (n instanceof InternetProviderNetwork) {
                ispSprites.add(n.getMapIcon());
                parentLineSprites.add(n.getMapParentLine());
            } else {
                networkSprites.add(n.getMapIcon());
                parentLineSprites.add(n.getMapParentLine());
            }

            //			for(Connection c : n.getconnections) {
            //			    connectionLineSprites.addc.getLine();
            //			}

        }

//        for (InternetProviderNetwork n : internet.getInternetProviderNetworksMap()) {
//            ispSprites.add(n.getMapIcon());
//            parentLineSprites.add(n.getMapParentLine());
//        }
//
//        for (Network n : internet.getBackboneProviderNetworksMap()) {
//            backboneSprites.add(n.getMapIcon());
//            backboneLineSprites.addAll(((BackboneProviderNetwork) n).getBackboneConnectionLines());
//        }
    }

    private void generateNoiseTexture(Noise.NoiseType type) throws ExceptionInvalidParam {
        ModuleBase noise = null;
        RendererImage renderer = new RendererImage();
        double[][] noiseArray = new double[NOISE_GENERATION_SIZE][NOISE_GENERATION_SIZE];

        switch (type) {
            case TERRAIN:
                if (land != null) {
                    return;
                }
                noise = Noise.TERRAIN;
                renderer.clearGradient();
                renderer.addGradientPoint(-1, new ColorCafe(10, 10, 255, 255));
                renderer.addGradientPoint(0, new ColorCafe(39, 140, 255, 255));
                renderer.addGradientPoint(.25, new ColorCafe(240, 240, 88, 255));
                renderer.addGradientPoint(.5, new ColorCafe(0, 140, 0, 255));
                renderer.addGradientPoint(.73, new ColorCafe(127, 51, 0, 255));
                renderer.addGradientPoint(.75, new ColorCafe(120, 120, 120, 255));
                renderer.addGradientPoint(1, new ColorCafe(234, 234, 234, 255));
                break;
            case DENSITY:
                if (density != null) {
                    return;
                }
                noise = Noise.DENSITY;
                renderer.clearGradient();
                renderer.clearGradient();
                renderer.addGradientPoint(-1, new ColorCafe(0, 0, 0, 255));
                renderer.addGradientPoint(1, new ColorCafe(50, 50, 255, 255));
                break;
            case COUNTRY:
                if (country != null) {
                    return;
                }
                noise = Noise.COUNTRY;
                renderer.clearGradient();
                renderer.clearGradient();
                renderer.addGradientPoint(-1, new ColorCafe(0, 0, 0, 255));
                renderer.addGradientPoint(1, new ColorCafe(0, 0, 0, 255));
                break;
            case POLITICS:
                if (politics != null) {
                    return;
                }
                noise = Noise.POLITICS;
                renderer.clearGradient();
                renderer.clearGradient();
                renderer.addGradientPoint(-1, new ColorCafe(0, 0, 0, 255));
                renderer.addGradientPoint(1, new ColorCafe(255, 0, 0, 255));
                break;
            case ETHICS:
                if (ethics != null) {
                    return;
                }
                noise = Noise.ETHICS;
                renderer.clearGradient();
                renderer.clearGradient();
                renderer.addGradientPoint(-1, new ColorCafe(0, 0, 0, 255));
                renderer.addGradientPoint(1, new ColorCafe(255, 255, 255, 255));
                break;
            case INCOME:
                if (income != null) {
                    return;
                }
                noise = Noise.INCOME;
                renderer.clearGradient();
                renderer.clearGradient();
                renderer.addGradientPoint(-1, new ColorCafe(0, 0, 0, 255));
                renderer.addGradientPoint(1, new ColorCafe(0, 255, 0, 255));
                break;
            case CRIME:
                if (income != null) {
                    return;
                }
                noise = Noise.INCOME;
                renderer.clearGradient();
                renderer.clearGradient();
                renderer.addGradientPoint(-1, new ColorCafe(0, 0, 0, 255));
                renderer.addGradientPoint(1, new ColorCafe(255, 160, 80, 255));
                break;
        }

        for (int y = 0; y < NOISE_GENERATION_SIZE; y++) {
            for (int x = 0; x < NOISE_GENERATION_SIZE; x++) { // maybe use separate threads to get the values and set them to arrays. e.g. float[250][5000] ten times
                float f = (float) noise.getValue((x - NOISE_GENERATION_SIZE / 2) * NOISE_DISPLAY_SIZE / NOISE_GENERATION_SIZE, 0, (y - NOISE_GENERATION_SIZE / 2) * NOISE_DISPLAY_SIZE / NOISE_GENERATION_SIZE);
                noiseArray[x][NOISE_GENERATION_SIZE - y - 1] = f;
            }
            Gdx.app.debug("Filling Noise Array", (float) y / NOISE_GENERATION_SIZE * 100 + "% done");
        }

        Pixmap pixmap = new Pixmap(NOISE_GENERATION_SIZE, NOISE_GENERATION_SIZE, Pixmap.Format.RGBA8888);
        NoiseMap noiseMap = new NoiseMap(NOISE_GENERATION_SIZE, NOISE_GENERATION_SIZE);
        noiseMap.setNoiseMap(noiseArray);
        ImageCafe imageCafe = new ImageCafe(NOISE_GENERATION_SIZE, NOISE_GENERATION_SIZE);
        renderer.setSourceNoiseMap(noiseMap);
        renderer.setDestImage(imageCafe);
        renderer.render();

        for (int y = 0; y < NOISE_GENERATION_SIZE; y++) {
            for (int x = 0; x < NOISE_GENERATION_SIZE; x++) { // maybe use separate threads to get the values and set them to arrays. e.g. float[250][5000] ten times
                ColorCafe color = imageCafe.getValue(x, y);
                int c = Color.rgba8888((float) color.getRed() / 255f, (float) color.getGreen() / 255f, (float) color.getBlue() / 255f, (float) color.getAlpha() / 255f);
                pixmap.drawPixel(x, y, c);
            }
            Gdx.app.debug("Filling Pixmap", (float) y / NOISE_GENERATION_SIZE * 100 + "% done");
        }

        switch (type) {
            case TERRAIN:
                land = new Texture(pixmap);
                land.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
            case DENSITY:
                density = new Texture(pixmap);
                density.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
            case COUNTRY:
                country = new Texture(pixmap);
                country.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
            case POLITICS:
                politics = new Texture(pixmap);
                politics.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
            case ETHICS:
                ethics = new Texture(pixmap);
                ethics.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
            case INCOME:
                income = new Texture(pixmap);
                income.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
            case CRIME:
                income = new Texture(pixmap);
                income.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
        }
        pixmap.dispose();
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

    public Noise.NoiseType getCurrentBackground() {
        return currentBackground;
    }

    public void setCurrentBackground(Noise.NoiseType currentBackground) {
        this.currentBackground = currentBackground;
    }
}
