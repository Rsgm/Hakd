package hakd.gui.screens;

import hakd.game.gameplay.Player;
import hakd.gui.Room;
import hakd.gui.input.GameInput;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class GameScreen extends HakdScreen {
	private Player						player;			// TODO Sometime make this an array and have other people
// in the game with different skills and personalities private int arraylist<npc> npcs = new arraylist<npc>();

	private Room						room;

	private OrthographicCamera			cam;

	private TiledMap					map;				// TODO move this to the room class
	private IsometricTiledMapRenderer	renderer;			// it says this is experimental, but it was an old article

	private final float					tileSize	= 91;	// I have no idea if this is height width, or being isometric, diagonal

	public GameScreen(Game game, String name) {
		super(game);

		map = new TmxMapLoader().load("src/hakd/gui/resources/maps/untitled.tmx");
		cam = new OrthographicCamera();
		renderer = new IsometricTiledMapRenderer(map, 1 / tileSize); // can only use the map specified in the constructor
		TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);

		cam.setToOrtho(false, layer0.getWidth(), layer0.getHeight());
		cam.update();
		renderer.setView(cam);

		batch = new SpriteBatch();

		cam.position.x = 5;
		cam.position.y = 1;

// GamePlay.generateGame();
// Network n = NetworkController.addPublicNetwork(NetworkType.PLAYER);

		Sprite sprite = new Sprite(textures.findRegion("player0"));
		sprite.setSize(sprite.getWidth() / tileSize, sprite.getHeight() / tileSize);
		player = new Player(name, null, sprite);

// room = new Room(n);
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
		map.dispose();
	}

	private void updateMovement() {
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			player.move(1 / 46f, 1 / 91f);
		} else if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			player.move(-1 / 46f, 1 / 91f);
		} else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			player.move(-1 / 46f, -1 / 91f);
		} else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			player.move(1 / 46f, -1 / 91f);
		}
	}
}
