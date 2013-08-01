package hakd.gui.screens;

import hakd.gui.input.MapInput;
import hakd.internet.Internet;
import hakd.networks.Network;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;

public class MapScreen extends HakdScreen {
    // Player player;
    // Network network;
    Internet internet;

    public Model model;
    public ModelInstance instance;
    private final ModelBatch modelBatch;
    public Lights lights;
    public MapInput mapInput;

    List<ModelInstance> modelInstances;

    public MapScreen(Game game, Internet internet) {
	super(game);

	this.internet = internet;

	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());
	cam.position.set(10f, 10f, 10f);
	cam.lookAt(0, 0, 0);
	cam.near = 0.1f;
	cam.far = 300f;
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
	    n.getInstance().transform.setToTranslation(
		    (float) (Math.random() * 5 * internet.getPublicNetworks()
			    .size()), (float) (Math.random() * 1 * internet
			    .getPublicNetworks().size()),
		    (float) (Math.random() * 5 * internet.getPublicNetworks()
			    .size()));
	    modelInstances.add(n.getInstance());
	}
    }

    @Override
    public void show() {
	super.show();
	mapInput = new MapInput(cam);
	Gdx.input.setInputProcessor(mapInput);
    }

    @Override
    public void render(float delta) {
	System.out.println(delta);

	mapInput.update();

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
	model.dispose();
    }

    @Override
    public void hide() {
	Gdx.input.setInputProcessor(null);
    }
}
