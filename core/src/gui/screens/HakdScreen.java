package gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thesecretpie.shader.ShaderManager;
import game.Hakd;
import other.Util;

public class HakdScreen implements Screen {
    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();

    Hakd game;

    Camera cam;
    SpriteBatch batch;

    final ShaderManager shaders;
    final AssetManager assets;

    public HakdScreen(Hakd game) {
        this.game = game;

        batch = new SpriteBatch();

        assets = Hakd.assets;
        ShaderProgram.pedantic = false;
        shaders = new ShaderManager(Util.ASSETS + "shaders", assets);

        loadShaders();
    }

    private void loadShaders() {
        shaders.add("bloom", "default.vert", "bloom.frag");
        shaders.createFB("bloom_fb");
        shaders.add("default", "default.vert", "default.frag");
        shaders.createFB("default_fb");
        shaders.add("lines", "lines.vert", "lines.frag");
        shaders.createFB("lines_fb");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        cam.update();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        batch.dispose();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = (Hakd) game;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public Camera getCam() {
        return cam;
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public AssetManager getAssets() {
        return assets;
    }
}
