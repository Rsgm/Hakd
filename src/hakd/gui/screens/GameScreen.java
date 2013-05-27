package hakd.gui.screens;

import hakd.game.gameplay.Player;
import hakd.gui.Room;
import hakd.gui.input.GameInput;
import hakd.networks.Network;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class GameScreen extends HakdScreen {
	private Player						player;			// TODO Sometime make this an array and have other people in the game with different
// skills and
// personalities private int arraylist<npc> npcs = new arraylist<npc>();
	private Room						room;

	private OrthographicCamera			cam;
	private IsometricTiledMapRenderer	renderer;			// it says this is experimental, but it was an old article

	private final int					tileSize	= 91;

	public GameScreen(Game game, String name) {
		super(game);

// GamePlay.generateGame();
		Network n = null;// NetworkController.addPublicNetwork(NetworkType.PLAYER);

		player = new Player(name, n, textures, this);
		room = new Room(player, this);

		Sprite sprite = player.getSprite();
		sprite.setSize(sprite.getWidth() / tileSize, sprite.getHeight() / tileSize);

		cam = new OrthographicCamera();

		cam.setToOrtho(false, room.getBackground().getWidth(), room.getBackground().getHeight());
		cam.update();
		renderer.setView(cam);

		batch = new SpriteBatch();

		cam.position.x = 5;
		cam.position.y = 1;

	}

	@Override
	public void show() {
		super.show();

		Gdx.input.setInputProcessor(new GameInput(game, cam, player)); // I guess this has to be set in the show method

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		updateMovement();
		// update display
		// update game
		// update other, I don't know

		cam.update();
		renderer.setView(cam);
		renderer.render();

		renderer.getSpriteBatch().begin();
		player.getSprite().draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();

		batch.begin();
		batch.draw(textures.findRegion("loading"), 0, 0); // this is always set to 0,0 so it will not move
		batch.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		room.dispose();
	}

	private void updateMovement() {
		float x = 0;
		float y = 0;
		if ((Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))) {
			x += 1;
			y += 1;
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			x += -1;
			y += 1;
		}
		if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			x += -1;
			y += -1;
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			x += 1;
			y += -1;
		}
		if (x < -1 || x > 1) {
			x /= 2; // x does not equal 2
		}
		player.move(2 * x / tileSize, y / tileSize); // it moves twice as fast horizontally relative to the map than it does vertically
	}

	public void changeMap(TiledMap map) { // TODO make a transition effect
		renderer = new IsometricTiledMapRenderer(map, 1 / tileSize); // TODO change this to 64 once I get the graphics
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

	public OrthographicCamera getCam() {
		return cam;
	}

	public void setCam(OrthographicCamera cam) {
		this.cam = cam;
	}

	public IsometricTiledMapRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(IsometricTiledMapRenderer renderer) {
		this.renderer = renderer;
	}

	public int getTileSize() {
		return tileSize;
	}
}
