package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import hakd.game.Internet;
import hakd.game.NetworkFactory;
import hakd.game.gameplay.Player;
import hakd.gui.Assets;
import hakd.gui.EmptyDeviceTile;
import hakd.gui.Room;
import hakd.gui.Room.RoomMap;
import hakd.gui.input.GameInput;
import hakd.gui.windows.BuyDeviceWindow;
import hakd.gui.windows.WindowStage;
import hakd.networks.Network;
import hakd.networks.devices.Device;

public final class GameScreen extends HakdScreen {
	private Player player;
	// TODO Sometime make this an array and have other people in the game with different skills and personalities private int arraylist<npc> npcs = new arraylist<npc>(); maybe

	public static Internet internet = null;
	public static final float tileSize = 64;

	private String name;
	private Room room;
	private WindowStage openWindow = null;
	private MapScreen map;
	private IsometricTiledMapRenderer renderer; // it says this is experimental, but it was an old article

	public GameScreen(Game game, String name) {
		super(game);
		this.name = name;
	}

	@Override
	public void show() {
		super.show();

		if(internet == null) {
			for(short i = 1; i < 256; i++) {
				Internet.ipNumbers.add(i);
			}

			internet = new Internet();

			player = new Player(name, this);
			Network n = NetworkFactory.createPlayerNetwork(player);
			internet.addNetworkToInternet(n);

			room = new Room(player, this, RoomMap.room1);

			Sprite sprite = player.getSprite();
			sprite.setSize(sprite.getWidth() / tileSize, sprite.getHeight() / tileSize);

			cam = new OrthographicCamera();
			((OrthographicCamera) cam).setToOrtho(false, room.getFloor().getWidth(), room.getFloor().getHeight());
			cam.update();

			renderer.setView((OrthographicCamera) cam);

			cam.position.x = room.getFloor().getWidth() / 2;
			cam.position.y = 0;

			map = new MapScreen(game, this, internet);
		}

		Gdx.input.setInputProcessor(new GameInput(game, (OrthographicCamera) cam, player));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		SpriteBatch rBatch = renderer.getSpriteBatch();
		renderer.setView((OrthographicCamera) cam);
		renderer.render();

		rBatch.begin();
		for(Device d : player.getNetwork().getDevices()) {
			d.getTile().draw(rBatch);
		}
		for(EmptyDeviceTile e : player.getNetwork().getEmptyDeviceTiles()) {
			e.getTile().draw(rBatch);
		}

		if(openWindow == null) {
			updateMovement();
			checkPosition(rBatch);
		}

		// update display();
		// update game
		// update other, I don't know

		player.getSprite().draw(rBatch);
		rBatch.end();

		if(openWindow != null) {
			openWindow.render();
		}
	}

	private void checkPosition(SpriteBatch batch) {
		int x = player.getIsoX();
		int y = player.getIsoY();

		Object o = room.getObjectAtTile(x, y - 1);

		if(o != null) {
			Device d = null;
			String code;
			if(o instanceof Device) {
				d = (Device) o;
			} else if(o instanceof String) {
				code = (String) o;
			}

			Sprite s = new Sprite(Assets.linearTextures.findRegion("spaceBarIcon"));
			s.setPosition(player.getSprite().getX(), player.getSprite().getY() + 32 / tileSize);
			s.setSize(16 / tileSize, 16 / tileSize);

			s.draw(batch);

			if(Gdx.input.isKeyPressed(Keys.SPACE) && openWindow == null) {

				if(o instanceof EmptyDeviceTile) {
					openWindow = new BuyDeviceWindow(player.getNetwork(), 4, 0, Device.DeviceType.SERVER, (EmptyDeviceTile) o);
					openWindow.setScreen(this);
					openWindow.open();
				} else {
					d.getWindow().setScreen(this);
					openWindow = d.getWindow();
					d.getWindow().open();
				}
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		room.dispose();

		Internet.getThread().interrupt();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	private void updateMovement() {
		Input i = Gdx.input;

		float x = 0;
		float y = 0;
		if((i.isKeyPressed(Keys.W) || i.isKeyPressed(Keys.UP))) {
			x += 1;
			y += 1;
		}
		if(i.isKeyPressed(Keys.A) || i.isKeyPressed(Keys.LEFT)) {
			x += -1;
			y += 1;
		}
		if(i.isKeyPressed(Keys.S) || i.isKeyPressed(Keys.DOWN)) {
			x += -1;
			y += -1;
		}
		if(i.isKeyPressed(Keys.D) || i.isKeyPressed(Keys.RIGHT)) {
			x += 1;
			y += -1;
		}

		if(x < -1 || x > 1) {
			x /= 2; // x does not equal 2
		}

		player.move(1.41f * x / tileSize, y / tileSize / 1.41f);
		// these were at1.5f each, they may need to be changed to account for
		// screen size
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

	public WindowStage getOpenWindow() {
		return openWindow;
	}

	public void setOpenWindow(WindowStage openWindow) {
		this.openWindow = openWindow;
	}

	public MapScreen getMap() {
		return map;
	}
}
