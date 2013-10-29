package hakd.gui.windows.deviceapps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hakd.connection.Port;
import hakd.game.Internet;
import hakd.gui.Assets;
import hakd.networks.devices.Device;

public final class Info implements ServerWindow {
    private final ServerWindowStage window;
    private final Device device;
    private final Window infoWindow;
    private final ScrollPane scroll;
    private final Table table;
    private final ImageButton close;

    public Info(ServerWindowStage w) {
        window = w;
        device = w.getDevice();

        Skin skin = Assets.skin;

        infoWindow = new Window(device.getType() + " info", skin);
        infoWindow.setSize(200, 300);
        table = new Table(skin);
        scroll = new ScrollPane(table);
        infoWindow.add(scroll);

        close = new ImageButton(new TextureRegionDrawable(Assets.linearTextures.findRegion("close")));
        close.setPosition(infoWindow.getWidth() - close.getWidth(), infoWindow.getHeight() - close.getHeight() - 20);

        infoWindow.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // touch up will not work without this returning true
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (y >= infoWindow.getHeight() - 20) {
                    if (infoWindow.getX() < 0) {
                        infoWindow.setX(0);
                    }
                    if (infoWindow.getY() < 0) {
                        infoWindow.setY(0);
                    }
                    if (infoWindow.getX() + infoWindow.getWidth() > Gdx.graphics.getWidth()) {
                        infoWindow.setX(Gdx.graphics.getWidth() - infoWindow.getWidth());
                    }
                    if (infoWindow.getY() + infoWindow.getHeight() > Gdx.graphics.getHeight()) {
                        infoWindow.setY(Gdx.graphics.getHeight() - infoWindow.getHeight());
                    }
                }
            }
        });

        close.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                close();
            }
        });

        infoWindow.addActor(close);
    }

    @Override
    public void open() {
        table.clear();
        add();
        window.getCanvas().addActor(infoWindow);
    }

    @Override
    public void close() {
        table.clear();
        window.getCanvas().removeActor(infoWindow);
    }

    private void add() {
        table.add("IP:");
        table.add(Internet.ipToString(device.getIp()));
        table.row();
        table.add("Brand:");
        // table.add(device.getBrand().toString());
        table.row();
        table.add("Model:");
        // table.add(device.getModel().toString());
        table.row();
        table.add("Device Type:");
        table.add(device.getType().toString());
        table.row();
        table.add("Device level:");
        table.add(device.getLevel() + "");
        table.row();
        table.add("Part Limit:");
        table.add(device.getPartLimit() + "");
        table.row();
        table.add("Total CPU speed:");
        table.add(device.getCpuSpeed() + "");
        table.row();
        table.add("Total GPU speed:");
        table.add(device.getGpuSpeed() + "");
        table.row();
        table.add("Total Memory:");
        table.add(device.getMemoryCapacity() + "");
        table.row();
        table.add("Total Storage:");
        table.add(device.getStorageCapacity() + "");
        table.row();
        table.add("Open Ports:");
        table.add("[");
        for (Port p : device.getPorts()) {
            table.add(p.getPortNumber() + ", ");
        }
        table.add("]");
        table.row();
    }
}