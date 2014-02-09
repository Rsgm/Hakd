package hakd.gui.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import hakd.gui.Assets;
import hakd.gui.EmptyDeviceTile;
import hakd.gui.screens.GameScreen;
import hakd.gui.windows.actors.BuyDeviceButton;
import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.networks.devices.DeviceFactory;

public final class BuyDeviceWindow implements WindowStage {
    private GameScreen screen;
    private final Stage stage;

    private final Window window;
    private final ScrollPane scroll;
    private final Table table;

    public BuyDeviceWindow(Network network, int amountToShow, int level, Device.DeviceType typeToBuy, EmptyDeviceTile o) {
        Skin skin = Assets.skin;

        stage = new Stage();
        window = new Window("Buy a new " + typeToBuy.toString().toLowerCase(), skin);
        table = new Table(skin);
        scroll = new ScrollPane(table);

        stage.addActor(window);
        window.add(scroll).expand().fill();

        window.setSize(350, Gdx.graphics.getHeight() * 0.8f);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);

        for (int i = 0; i < amountToShow; i++) {
            BuyDeviceButton deviceButton = new BuyDeviceButton(network, this, Assets.skin, DeviceFactory.createDevice(0, Device.DeviceType.SERVER), o);

            table.add("0x" + Integer.toHexString((int) (Math.random() * 0xffffff)).toUpperCase() + "\nCPU Speed: " + deviceButton.getNewDevice().getCpuSpeed() + " MHz" + "\nGPU Speed: " + deviceButton.getNewDevice().getGpuSpeed() + " MHz" + "\nMemory: " + deviceButton.getNewDevice().getMemoryCapacity() + "MB" + "\nStorage: " + deviceButton.getNewDevice().getStorageCapacity() + "GB").left().pad(20);
            table.add(deviceButton);
            table.row();
        }
    }

    @Override
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void open() {
        Gdx.input.setInputProcessor(stage);
        stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void close() {
        stage.clear();
        screen.setOpenWindow(null);
        Gdx.input.setInputProcessor(screen.getInput());
    }

    @Override
    public void setScreen(GameScreen screen) {
        this.screen = screen;
    }

    public GameScreen getScreen() {
        return screen;
    }
}
