package hakd.gui.screens;

import hakd.game.gameplay.GamePlay;
import hakd.game.gameplay.Player;
import hakd.gui.Assets;
import hakd.gui.Room;
import hakd.gui.input.GameInput;
import hakd.gui.windows.Window;
import hakd.networks.devices.Device;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class GameScreen extends HakdScreen {
    private Player player;
    // TODO Sometime make this an array and have other people in the game with
    // different skills and personalities
    // private int arraylist<npc> npcs = new arraylist<npc>(); maybe

    private Room room;
    private final float tileSize = 64;
    private IsometricTiledMapRenderer renderer; // it says this is experimental,
						// but it was an old article

    private Window openWindow = null;

    public GameScreen(Game game, String name) {
	super(game);

	GamePlay.generateGame();
	// Network n = NetworkController.addPublicNetwork(NetworkType.PLAYER);

	player = new Player(name, null, this);
	room = new Room(player, this);

	Sprite sprite = player.getSprite();
	sprite.setSize(sprite.getWidth() / tileSize, sprite.getHeight()
		/ tileSize);

	cam.setToOrtho(false, room.getFloor().getWidth(), room.getFloor()
		.getHeight());
	cam.update();

	renderer.setView(cam);

	cam.position.x = room.getFloor().getWidth() / 2;
	cam.position.y = 0;
    }

    @Override
    public void show() {
	super.show();
	Gdx.input.setInputProcessor(new GameInput(game, cam, player, this));
	// I guess this has to be set in the show method
    }

    @Override
    public void render(float delta) {
	super.render(delta);
	SpriteBatch rBatch = renderer.getSpriteBatch();
	renderer.setView(cam);
	renderer.render();

	rBatch.begin();
	if (openWindow == null) {
	    updateMovement();
	    checkPosition(rBatch);
	}

	// update display();
	// update game
	// update other, I don't know

	player.getSprite().draw(rBatch);
	rBatch.end();

	if (openWindow != null) {
	    openWindow.render();
	}
    }

    private void checkPosition(SpriteBatch batch) {
	int x = player.getIsoX();
	int y = player.getIsoY();

	Device d = room.getDeviceAtTile(x - 1, y);

	if (d == null) {
	    d = room.getDeviceAtTile(x, y - 1);
	}

	if (d != null) {
	    Sprite s = new Sprite(
		    Assets.linearTextures.findRegion("spaceBarIcon"));
	    s.setPosition(player.getSprite().getX(), player.getSprite().getY()
		    + 32 / tileSize);
	    s.setSize(16 / tileSize, 16 / tileSize);

	    s.draw(batch);

	    if (Gdx.input.isKeyPressed(Keys.SPACE) && openWindow == null) {
		d.getWindow().open(this);
	    }
	}
    }

    @Override
    public void dispose() {
	super.dispose();
	room.dispose();
    }

    private void updateMovement() {
	Input i = Gdx.input;

	float x = 0;
	float y = 0;
	if ((i.isKeyPressed(Keys.W) || i.isKeyPressed(Keys.UP))) {
	    x += 1;
	    y += 1;
	}
	if (i.isKeyPressed(Keys.A) || i.isKeyPressed(Keys.LEFT)) {
	    x += -1;
	    y += 1;
	}
	if (i.isKeyPressed(Keys.S) || i.isKeyPressed(Keys.DOWN)) {
	    x += -1;
	    y += -1;
	}
	if (i.isKeyPressed(Keys.D) || i.isKeyPressed(Keys.RIGHT)) {
	    x += 1;
	    y += -1;
	}

	if (x < -1 || x > 1) {
	    x /= 2; // x does not equal 2
	}

	player.move(1.5f * x / tileSize / 1.0f, y / tileSize / 1.5f);
    }

    public void changeMap(TiledMap map) { // TODO make a transition effect
	renderer = new IsometricTiledMapRenderer(map, 1 / tileSize);
	// tilesize has to be float, otherwise it will round
    }

    public Player getPlayer() {
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

    public void setRenderer(IsometricTiledMapRenderer renderer) {
	this.renderer = renderer;
    }

    public float getTileSize() {
	return tileSize;
    }

    public Window getOpenWindow() {
	return openWindow;
    }

    public void setOpenWindow(Window openWindow) {
	this.openWindow = openWindow;
    }
}
