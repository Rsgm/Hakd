package gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import game.GamePlay;
import game.Hakd;
import game.Internet;
import game.gameplay.Character;
import game.gameplay.Player;
import gui.Room;
import gui.input.GameInput;
import gui.windows.device.DeviceScene;

public class GameScreen extends HakdScreen {
    private String playerName;
    private Character player;
    private Room room;
    private DeviceScene deviceScene = null;
    private MapScreen map;
    private IsometricTiledMapRenderer renderer; // it says this is experimental, but it was an old article
    private GameInput input;

    private boolean firstTimeShown = true;
    private final Stage dialogStage = new Stage(); // TODO this will be the overlay

    public GameScreen(Hakd game, String playerName) {
        super(game);
        this.playerName = playerName;
    }

    @Override
    public void show() {
        super.show();

        // this should all be in the constructor, but there is a bug where the top right 1/4th of the screen is white and all textures are white, this somehow works
        if (firstTimeShown) {
            firstTimeShown = false;

            for (short i = 1; i < 256; i++) {
                Internet.ipNumbers.add(i);
            }

            GamePlay gamePlay = new GamePlay(this, playerName);
            (game).setGamePlay(gamePlay);

            cam = new OrthographicCamera();
            ((OrthographicCamera) cam).setToOrtho(false, room.getFloorLayer().getWidth(), room.getFloorLayer().getHeight());
            cam.update();

            renderer.setView((OrthographicCamera) cam);

            cam.position.x = room.getFloorLayer().getWidth() / 2;
            cam.position.y = 0;

            map = new MapScreen(game, gamePlay.getInternet());
            input = new GameInput(game, (OrthographicCamera) cam);

            Gdx.input.setInputProcessor(input);
        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        SpriteBatch rBatch = (SpriteBatch) renderer.getSpriteBatch();
        renderer.setView((OrthographicCamera) cam);
        renderer.render();

        rBatch.begin();
        for (Room.DeviceTile t : room.getDeviceTileMap().values()) {
            t.getTile().draw(rBatch);
        }
        rBatch.end();

        if (deviceScene != null) {
            deviceScene.render();
        }

        dialogStage.act(Gdx.graphics.getDeltaTime());
        dialogStage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        room.dispose();
        game.getGamePlay().dispose();
        game.setGamePlay(null);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public Character getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public IsometricTiledMapRenderer getRenderer() {
        return renderer;
    }

    public DeviceScene getDeviceScene() {
        return deviceScene;
    }

    public void setDeviceScene(DeviceScene deviceScene) {
        this.deviceScene = deviceScene;
    }

    public MapScreen getMap() {
        return map;
    }

    public GameInput getInput() {
        return input;
    }

    public void setInput(GameInput input) {
        this.input = input;
    }

    public void setRenderer(IsometricTiledMapRenderer renderer) {
        this.renderer = renderer;
    }
}
