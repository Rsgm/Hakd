package hakd.gui.windows.newdevice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import hakd.gui.Assets;
import hakd.gui.input.GameInput;
import hakd.gui.screens.GameScreen;
import hakd.gui.windows.WindowStage;
import hakd.gui.windows.actors.BuyDeviceButton;
import hakd.networks.devices.Device;

public final class NewServerWindow implements WindowStage {
	private GameScreen screen;
	private final Stage stage;

	private final Window window;
	private final ScrollPane scroll;
	private final Table table;

	public NewServerWindow(Device device) {
		Skin skin = Assets.skin;

		stage = new Stage();
		window = new Window("Buy a new " + device.getType().toString().toLowerCase(), skin);
		table = new Table(skin);
		scroll = new ScrollPane(table);

		stage.addActor(window);
		window.add(scroll).expand().fill();

		window.setSize(350, Gdx.graphics.getHeight() * 0.8f);
		window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);

		BuyDeviceButton server1 = new BuyDeviceButton(device, 0, this, skin, null, null, null, null);
		BuyDeviceButton server2 = new BuyDeviceButton(device, 0, this, skin, null, null, null, null);
		BuyDeviceButton server3 = new BuyDeviceButton(device, 0, this, skin, null, null, null, null);
		BuyDeviceButton server4 = new BuyDeviceButton(device, 0, this, skin, null, null, null, null);

		table.add("0x" + Integer.toHexString((int) (Math.random() * 0xffffff)).toUpperCase() + "\nCPU Speed: " + server1.getCpu().getSpeed() + " MHz" + "\nGPU Speed: " + server1.getGpu().getSpeed() + " MHz" + "\nMemory: " + server1.getMemory().getCapacity() + "MB" + "\nStorage: " + server1.getStorage().getCapacity() + "GB").left().pad(20);
		table.add(server1);
		table.row();
		table.add("0x" + Integer.toHexString((int) (Math.random() * 0xffffff)).toUpperCase() + "\nCPU Speed: " + server2.getCpu().getSpeed() + " MHz" + "\nGPU Speed: " + server2.getGpu().getSpeed() + " MHz" + "\nMemory: " + server2.getMemory().getCapacity() + "MB" + "\nStorage: " + server2.getStorage().getCapacity() + "GB").left().pad(20);
		table.add(server2);
		table.row();
		table.add("0x" + Integer.toHexString((int) (Math.random() * 0xffffff)).toUpperCase() + "\nCPU Speed: " + server3.getCpu().getSpeed() + " MHz" + "\nGPU Speed: " + server3.getGpu().getSpeed() + " MHz" + "\nMemory: " + server3.getMemory().getCapacity() + "MB" + "\nStorage: " + server3.getStorage().getCapacity() + "GB").left().pad(20);
		table.add(server3);
		table.row();
		table.add("0x" + Integer.toHexString((int) (Math.random() * 0xffffff)).toUpperCase() + "\nCPU Speed: " + server4.getCpu().getSpeed() + " MHz" + "\nGPU Speed: " + server4.getGpu().getSpeed() + " MHz" + "\nMemory: " + server4.getMemory().getCapacity() + "MB" + "\nStorage: " + server4.getStorage().getCapacity() + "GB").left().pad(20);
		table.add(server4);
	}

	@Override
	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void open() {
		Gdx.input.setInputProcessor(stage);
		stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	@Override
	public void close() {
		stage.clear();
		screen.setOpenWindow(null);
		Gdx.input.setInputProcessor(new GameInput(screen.getGame(), (OrthographicCamera) screen.getCam(), screen.getPlayer()));
	}

	@Override
	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}

	public GameScreen getScreen() {
		return screen;
	}
}
