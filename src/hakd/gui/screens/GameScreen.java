package hakd.gui.screens;

import hakd.game.gameplay.GamePlay;
import hakd.game.gameplay.Player;
import hakd.gui.Room;
import hakd.gui.windows.Terminal;
import hakd.internet.NetworkController;
import hakd.networks.Network;
import hakd.other.enumerations.NetworkType;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen extends HakdScreen {
	private Terminal			terminal;
	private Player				player;													// TODO Sometime make this an array and have other people
// in
// the game with different skills and personalities private int arraylist<npc> npcs = new arraylist<npc>();

	private Room				room;

	OrthographicCamera			cam;

	private TiledMap			map;
	OrthogonalTiledMapRenderer	renderer	= new OrthogonalTiledMapRenderer(map, 1 / 16f);

// IsometricTiledMapRenderer renderer = new IsometricTiledMapRenderer(map);

	public GameScreen(Game game, String name) {
		super(game);

		map = new TmxMapLoader().load("untitled.tmx");

		renderer.setView(cam);
		cam.setToOrtho(false, 30, 20);

		batch = new SpriteBatch();

		GamePlay.generateGame();
		Network n = NetworkController.addPublicNetwork(NetworkType.PLAYER);
		player = new Player(name, n, terminal);
		room = new Room(n);
	}

	@Override
	public void show() {
		super.show();

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();

		batch.setProjectionMatrix(cam.combined);
		batch.setTransformMatrix(matrix);
		batch.begin();
		for (int z = 0; z < 10; z++) {
			for (int x = 0; x < 10; x++) {
				sprites[x][z].draw(batch);
			}
		}
		batch.end();
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
}
