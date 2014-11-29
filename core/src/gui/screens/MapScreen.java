package gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import game.Hakd;
import game.Internet;
import game.Noise;
import game.gameplay.City;
import gui.input.MapInput;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.ModuleBase;
import libnoiseforjava.util.ColorCafe;
import libnoiseforjava.util.ImageCafe;
import libnoiseforjava.util.NoiseMap;
import libnoiseforjava.util.RendererImage;
import networks.BackboneProviderNetwork;
import networks.InternetProviderNetwork;
import networks.Network;
import org.jdelaunay.delaunay.ConstrainedMesh;
import org.jdelaunay.delaunay.error.DelaunayError;
import org.jdelaunay.delaunay.geometries.DEdge;
import org.jdelaunay.delaunay.geometries.DPoint;
import other.Line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private final Set<Line> connectionLines; // change this to a line set
    private final Set<Line> parentLines;
    private final Set<Line> backboneLines;

    private Mesh connectionLinesMesh;
    private Mesh parentLinesMesh;
    private Mesh backboneLinesMesh;

    private Texture land;
    private Texture density;
    private Texture politics;
    private Texture ethics;
    private Texture country;
    private Texture income;
    private Texture crime;

    BitmapFont textFont = assets.get("skins/font/Fonts_7.fnt", BitmapFont.class);

    private Noise.NoiseType currentBackground;

    public static final int NOISE_GENERATION_SIZE = 800; // how many points to draw. default: 800, best quality would be around 2500
    public static final int NOISE_DISPLAY_SIZE = 50000; // how large of an area to spread the points out on. default: 50000
    public static final int INITIAL_TRIANGLE_SIZE = MapScreen.NOISE_DISPLAY_SIZE * 1000;
//    private overlay mapOverlay;

    public MapScreen(Hakd game, Internet internet) {
        super(game);

        this.internet = internet;
        this.gameScreen = (GameScreen) game.getScreen();

        territoryBatch = new SpriteBatch();
        networkSprites = new HashSet<Sprite>(internet.getNetworkMap().size());
        ispSprites = new HashSet<Sprite>(internet.getInternetProviderNetworksMap().size());
        territorySprites = new HashSet<Sprite>(internet.getInternetProviderNetworksMap().size());
        backboneSprites = new HashSet<Sprite>(internet.getBackboneProviderNetworksMap().size());

        connectionLines = new HashSet<Line>(50);
        parentLines = new HashSet<Line>(internet.getBackboneProviderNetworksMap().size());
        backboneLines = new HashSet<Line>(internet.getBackboneProviderNetworksMap().size());

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
            time = 0;
        }

        renderBackground();
        renderSprites();
        renderLines();

//        mapOverlay.render();
    }

    private void renderBackground() {
        territoryBatch.setProjectionMatrix(cam.combined);
        territoryBatch.begin();
        switch (currentBackground) {
            case TERRAIN:
                territoryBatch.draw(land,
                        -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case DENSITY:
                territoryBatch.draw(density,
                        -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case POLITICS:
                territoryBatch.draw(politics,
                        -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case ETHICS:
                territoryBatch.draw(ethics,
                        -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case COUNTRY:
                territoryBatch.draw(country,
                        -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
                break;
            case INCOME:
                territoryBatch.draw(income,
                        -NOISE_DISPLAY_SIZE / 2, -NOISE_DISPLAY_SIZE / 2, NOISE_DISPLAY_SIZE, NOISE_DISPLAY_SIZE);
        }
        territoryBatch.end();
    }

    private void renderSprites() {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

//        for (Sprite s : territorySprites) {
//            s.draw(batch);
//        }

        for (Sprite s : backboneSprites) {
            s.draw(batch);
        }

        for (Sprite s : ispSprites) {
            s.draw(batch);
        }

        for (Sprite s : networkSprites) {
            s.draw(batch);
        }

//        for (Sprite s : connectionLineSprites) {
//            s.draw(batch);
//        }

        for (City c : game.getGamePlay().getCityMap().values()) {
            c.getIcon().draw(batch);
            textFont.draw(batch, c.getName(), c.getPosition().x, c.getPosition().y + 80);
        }
        batch.end();
    }

    private void renderLines() {
        ShaderProgram lines = shaders.get("lines");

        lines.begin();
        lines.setUniformMatrix("u_worldView", cam.combined);

        if (backboneLinesMesh != null && backboneLinesMesh.getNumVertices() > 0) {
            lines.setAttributef("a_color", 255f / 255f, 149f / 255f, 38f / 255f, 1);
            backboneLinesMesh.render(lines, GL20.GL_LINES);
        }

        if (parentLinesMesh != null && parentLinesMesh.getNumVertices() > 0) {
            lines.setAttributef("a_color", 0f / 255f, 255f / 255f, 142f / 255f, 1);
            parentLinesMesh.render(lines, GL20.GL_LINES);
        }

//        if (connectionLinesMesh != null && connectionLinesMesh.getNumVertices() > 0) {
//        lines.setAttributef("a_color", 0f / 255f, 255f / 255f, 142f / 255f, 1);
//            connectionLinesMesh.render(shaders.getCurrent(), GL20.GL_LINES);
//        }

        lines.end();
    }

    private void reloadSprites() {
        networkSprites.clear();
        ispSprites.clear();
        territorySprites.clear();
        backboneSprites.clear();

        backboneLines.clear();
        parentLines.clear();
        connectionLines.clear();

        for (Network n : internet.getNetworkMap().values()) {
            if (n instanceof BackboneProviderNetwork) {
                backboneSprites.add(n.getMapIcon());
            } else if (n instanceof InternetProviderNetwork) {
                ispSprites.add(n.getMapIcon());
                parentLines.add(n.getMapParentLine());
            } else {
                networkSprites.add(n.getMapIcon());
                parentLines.add(n.getMapParentLine());
            }
        }

        drawBackboneLines();

        // create backbone line mesh
        float[] vertexArray = fillLineVertexArray(backboneLines, new Color(255f / 255f, 149f / 255f, 38f / 255f, 1));
        backboneLinesMesh = new Mesh(true, vertexArray.length, 0, VertexAttribute.Position());
        backboneLinesMesh.setVertices(vertexArray);

        // create parent line mesh
        vertexArray = fillLineVertexArray(parentLines, new Color(0f / 255f, 255f / 255f, 142f / 255f, 1));
        parentLinesMesh = new Mesh(true, vertexArray.length, 0, VertexAttribute.Position());
        parentLinesMesh.setVertices(vertexArray);

        // create connection line mesh
        vertexArray = fillLineVertexArray(connectionLines, new Color(0f / 255f, 0f / 255f, 0f / 255f, 1));
        connectionLinesMesh = new Mesh(true, vertexArray.length, 0, VertexAttribute.Position());
        connectionLinesMesh.setVertices(vertexArray);
    }

    /**
     * Fills a vertex array to be used in openGL rendering for the lines.
     *
     * @param lineList which line list to use
     * @param color    the color to use
     * @return
     */
    private float[] fillLineVertexArray(Set<Line> lineList, Color color) {
        List<Float> vertexList = new ArrayList<Float>();
        for (Line line : lineList) {
            // position A
            vertexList.add(line.getPointA().x);
            vertexList.add(line.getPointA().y);
            vertexList.add(0f);

            // position B
            vertexList.add(line.getPointB().x);
            vertexList.add(line.getPointB().y);
            vertexList.add(0f);
        }

        float[] vertexArray = new float[vertexList.size()]; // probably could only use an array and set the size to linelist.size*3 or something
        int i = 0;
        for (Float v : vertexList) {
            vertexArray[i++] = (v != null ? v : 0f); // Or whatever default you want.
        }
        return vertexArray;
    }


    /**
     * Generate the noise texture to use for the map, then renders it
     *
     * @param type
     * @throws ExceptionInvalidParam
     */
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
                renderer.addGradientPoint(-1, new ColorCafe(10, 10, 80, 255));
                renderer.addGradientPoint(1, new ColorCafe(39, 140, 255, 255));
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

        // fill noise array
        for (int y = 0; y < NOISE_GENERATION_SIZE; y++) {
            for (int x = 0; x <
                            NOISE_GENERATION_SIZE; x++) { // maybe use separate threads to get the values and set them to arrays. e.g. float[250][5000] ten times
                float f = (float) noise.getValue(
                        (x - NOISE_GENERATION_SIZE / 2) * NOISE_DISPLAY_SIZE / NOISE_GENERATION_SIZE, 0,
                        (y - NOISE_GENERATION_SIZE / 2) * NOISE_DISPLAY_SIZE / NOISE_GENERATION_SIZE);
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

        // fill color array
        for (int y = 0; y < NOISE_GENERATION_SIZE; y++) {
            for (int x = 0; x <
                            NOISE_GENERATION_SIZE; x++) { // maybe use separate threads to get the values and set them to arrays. e.g. float[250][5000] ten times
                ColorCafe color = imageCafe.getValue(x, y);
                int c = Color.rgba8888(
                        (float) color.getRed() / 255f,
                        (float) color.getGreen() / 255f,
                        (float) color.getBlue() / 255f, (float) color.getAlpha() / 255f);
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

    private void drawBackboneLines() {
        ConstrainedMesh mesh = new ConstrainedMesh(); // everything will be on the x/y plane, so z will always be zero

        try {
            mesh.addConstraintEdge(new DEdge(-INITIAL_TRIANGLE_SIZE, -INITIAL_TRIANGLE_SIZE, 0, INITIAL_TRIANGLE_SIZE, -INITIAL_TRIANGLE_SIZE, 0));
            mesh.addConstraintEdge(new DEdge(-INITIAL_TRIANGLE_SIZE, -INITIAL_TRIANGLE_SIZE, 0, -INITIAL_TRIANGLE_SIZE, INITIAL_TRIANGLE_SIZE, 0));
            mesh.addConstraintEdge(new DEdge(INITIAL_TRIANGLE_SIZE, INITIAL_TRIANGLE_SIZE, 0, INITIAL_TRIANGLE_SIZE, -INITIAL_TRIANGLE_SIZE, 0));
            mesh.addConstraintEdge(new DEdge(INITIAL_TRIANGLE_SIZE, INITIAL_TRIANGLE_SIZE, 0, -INITIAL_TRIANGLE_SIZE, INITIAL_TRIANGLE_SIZE, 0));

            for (BackboneProviderNetwork backbone : game.getGamePlay().getInternet().getBackboneProviderNetworksMap().values()) { // iterate through backbones and add points to set and triangulation
                mesh.addPoint(new DPoint(backbone.getPos().x, backbone.getPos().y, 0));
            }

            mesh.processDelaunay();// run delaunay algorithm
            List<DEdge> edges = mesh.getEdges(); // hold edges
            for (DEdge e : edges) {
                if (!backboneLines.contains(e)) {
                    backboneLines.add(new Line(e));
                }
            }

        } catch (DelaunayError delaunayError) {
            delaunayError.printStackTrace();
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

    public Noise.NoiseType getCurrentBackground() {
        return currentBackground;
    }

    public void setCurrentBackground(Noise.NoiseType currentBackground) {
        this.currentBackground = currentBackground;
    }
}
