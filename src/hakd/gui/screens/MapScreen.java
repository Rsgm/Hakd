package hakd.gui.screens;

import hakd.gui.input.MapInput;
import hakd.internet.Connection;
import hakd.internet.Internet;
import hakd.networks.Network;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.math.Vector3;

public class MapScreen extends HakdScreen {
    // Player player;
    // Network network;
    Internet internet;

    private final ModelBatch modelBatch;
    private final Lights lights;
    private MapInput mapInput;

    private List<ModelInstance> modelInstances;

    private Vector3 focus;

    public MapScreen(Game game, Internet internet) {
	super(game);

	this.internet = internet;

	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());
	cam.position.set(10f, 10f, 10f);
	cam.lookAt(0, 0, 0);
	cam.near = 0.1f;
	cam.far = 1000f;
	cam.update();

	modelBatch = new ModelBatch();

	drawMap();

	lights = new Lights();
	lights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
	lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f,
		-0.2f));
    }

    private void drawMap() {
	modelInstances = new ArrayList<ModelInstance>();

	for (Network n : internet.getPublicNetworks()) {
	    modelInstances.add(n.getInstance());
	    for (Connection c : n.getConnections()) {
		modelInstances.add(c.getInstance());
	    }
	}
    }

    @Override
    public void show() {
	super.show();
	mapInput = new MapInput(this);
	Gdx.input.setInputProcessor(mapInput);

	focus = new Vector3(0, 0, 0);
	cam.translate(focus);
	cam.lookAt(focus);
    }

    @Override
    public void render(float delta) {
	// System.out.println(1 / delta);

	// cam.rotateAround(focus, new Vector3(0, 1, 0), .08f);

	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());
	super.render(delta);

	modelBatch.begin(cam);
	for (ModelInstance i : modelInstances) {
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
	Gdx.input.setInputProcessor(null);
    }

    public Vector3 getFocus() {
	return focus;
    }

    public void setFocus(Vector3 focus) {
	this.focus = focus;
    }
}
