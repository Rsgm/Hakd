package hakd.gui.screens;

import hakd.game.gameplay.Player;
import hakd.gui.Room;
import hakd.gui.input.GameInput;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
// player = new Player(name, n, terminal);
// room = new Room(n);
	}

	@Override
	public void show() {
		super.show();

		Gdx.input.setInputProcessor(new GameInput(game, cam)); // I guess this has to be set in the show method

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		cam.update();

// System.out.println(cam.position.x + "	" + cam.position.y);

		renderer.setView(cam);
		renderer.render();

		batch.begin();
		batch.draw(textures.findRegion("loading"), 0, 0); // this is always set to 0,0 so it will not move
		batch.end();

// cam.translate(-.01f, -.01f);
	}

	@Override
	public void dispose() {
		super.dispose();
		map.dispose();
	}
}
