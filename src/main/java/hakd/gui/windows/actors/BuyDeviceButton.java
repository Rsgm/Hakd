package hakd.gui.windows.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import hakd.gui.Assets;
import hakd.gui.EmptyDeviceTile;
import hakd.gui.windows.BuyDeviceWindow;
import hakd.gui.windows.deviceapps.GameScene;
import hakd.networks.Network;
import hakd.networks.devices.Device;

public final class BuyDeviceButton extends TextButton {
    private final Network network;
    private final Device newDevice;

    private final BuyDeviceWindow newDeviceWindow;
    private final EmptyDeviceTile emptyDeviceTile;

    public BuyDeviceButton(Network network, final BuyDeviceWindow newDeviceWindow, Skin skin, Device newDevice, EmptyDeviceTile emptyDeviceTile) {
        super("buy", skin);

        this.network = network;
        this.newDeviceWindow = newDeviceWindow;
        this.newDevice = newDevice;
        this.emptyDeviceTile = emptyDeviceTile;

        addListener();
    }

    private void addListener() {
        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                newDevice.setTile(emptyDeviceTile.getTile());
                newDevice.setIsoX(emptyDeviceTile.getIsoX());
                newDevice.setIsoY(emptyDeviceTile.getIsoY());
                newDevice.getTile().setRegion(Assets.nearestTextures.findRegion("d" + newDevice.getLevel()));
                newDevice.setWindow(new GameScene(newDevice));

                network.addDevice(newDevice);
                network.getEmptyDeviceTiles().remove(emptyDeviceTile);

                //TODO popup "congratulations" or something"?

                newDeviceWindow.close();
            }
        });
    }

    public Device getNewDevice() {
        return newDevice;
    }
}
