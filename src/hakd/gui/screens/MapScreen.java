package hakd.gui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.math.Vector3;
import hakd.gui.input.MapInput;
import hakd.internet.Connection;
import hakd.internet.Internet;
import hakd.networks.Network;
import hakd.networks.devices.Router;

import java.util.ArrayList;
import java.util.List;

public class MapScreen extends HakdScreen {
	// private Player player;
	// private DefaultNetwork network;
	private final Internet internet;

	private final GameScreen gameScreen;
	private float time = 0;
	private InputProcessor tempInputProcessor = null;

	private final ModelBatch modelBatch;
	private List<ModelInstance> modelInstances;
	private final Lights lights;
	private MapInput mapInput;
	private Vector3 focus;

	public MapScreen(Game game, GameScreen gameScreen, Internet internet) {
		super(game);

		this.internet = internet;
		this.gameScreen = gameScreen;

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 3000f;
		cam.update();

		modelBatch = new ModelBatch();

		lights = new Lights();
		lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
		lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	private void drawMap() {
		modelInstances = new ArrayList<ModelInstance>();

		for(Network n : internet.getPublicNetworks()) {
			modelInstances.add(n.getInstance());
			for(Router r : n.getRouters()) {
				for(Connection c : r.getConnections()) {
					modelInstances.add(c.getInstance());
				}
			}
		}
	}

	@Override
	public void show() {
		super.show();
		mapInput = new MapInput(this);

		tempInputProcessor = Gdx.input.getInputProcessor();
		Gdx.input.setInputProcessor(mapInput);

		focus = new Vector3(0, 0, 0);
		cam.translate(focus);
		cam.lookAt(focus);

		drawMap();
	}

	@Override
	public void render(float delta) {
		System.out.println((int) (1 / delta));
		time += delta;

		if(!Gdx.input.isButtonPressed(Buttons.LEFT)) {
			cam.rotateAround(focus, new Vector3(0, 1, 0), .08f);
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		super.render(delta);

		if(time >= 1) {
			time = 0;
			drawMap();
		}

		modelBatch.begin(cam);
		for(ModelInstance i : modelInstances) {
			modelBatch.render(i, lights);
		}
		modelBatch.end();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(tempInputProcessor);
		tempInputProcessor = null;
		time = 0;
	}

	public Vector3 getFocus() {
		return focus;
	}

	public void setFocus(Vector3 focus) {
		this.focus = focus;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}
}
