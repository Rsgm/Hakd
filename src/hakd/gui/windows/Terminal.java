package hakd.gui.windows;

import hakd.game.Command;
import hakd.gui.Assets;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Terminal {
    private final Window window;

    private final Table table;

    private final TextField input;
    private final Label display;
    private final ScrollPane scroll;

    private final List<String> history; // command history
    private int line = 0; // holds the position of the history
    private final Device device;

    private final ImageButton close;

    public Terminal(Device d, Window w) {
	device = d;
	window = w;

	Skin skin = Assets.skin;
	history = new ArrayList<String>();

	table = new com.badlogic.gdx.scenes.scene2d.ui.Window("Terminal", skin);
	table.setSize(window.getCanvas().getWidth() * .9f, window.getCanvas()
		.getHeight() * .9f);

	close = new ImageButton(new TextureRegionDrawable(
		Assets.linearTextures.findRegion("closeUp")));
	close.setPosition(table.getWidth() - close.getWidth(),
		table.getHeight() - close.getHeight() - 20);

	input = new TextField("", skin.get("console", TextFieldStyle.class));
	display = new Label("", skin.get("console", LabelStyle.class));
	scroll = new ScrollPane(display, skin);

	int deviceIndex = device.getNetwork().getDevices().indexOf(device);
	if (deviceIndex == -1) {
	    deviceIndex = 0;
	}

	display.setWrap(false);
	display.setAlignment(10, Align.left);
	display.setText("Terminal [Version 0." + ((int) Math.random() * 100)
		/ 10 + "]" + "\nroot @ " + device.getNetwork().getIp() + "/"
		+ deviceIndex + "\nMemory: " + device.getTotalMemory()
		+ "MB\nStorage: " + device.getTotalStorage()
		+ "GB\nCPU Cores: " + device.getTotalCores());

	table.addListener(new InputListener() {
	    @Override
	    public boolean touchDown(InputEvent event, float x, float y,
		    int pointer, int button) {
		// touch up will not work without this returning true
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y,
		    int pointer, int button) {
		if (y >= table.getHeight() - 20) {
		    if (table.getX() < 0) {
			table.setX(0);
		    }
		    if (table.getY() < 0) {
			table.setY(0);
		    }
		    if (table.getX() + table.getWidth() > Gdx.graphics
			    .getWidth()) {
			table.setX(Gdx.graphics.getWidth() - table.getWidth());
		    }
		    if (table.getY() + table.getHeight() > Gdx.graphics
			    .getHeight()) {
			table.setY(Gdx.graphics.getHeight() - table.getHeight());
		    }
		}
	    }
	});

	close.addListener(new InputListener() {
	    @Override
	    public boolean touchDown(InputEvent event, float x, float y,
		    int pointer, int button) {
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y,
		    int pointer, int button) {
		super.touchUp(event, x, y, pointer, button);
		close();
	    }
	});

	input.addListener(new InputListener() {
	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		if (keycode == Keys.ENTER) {
		    System.out.println(input.getText());
		    display.setText(display.getText() + "\n\nroot @ "
			    + device.getNetwork().getIp() + "/"
			    + device.getNetwork().getDevices().indexOf(device)
			    + "\n>" + input.getText());
		    history.add(input.getText());
		    new Command(input.getText(), device);

		    line = history.size();
		    input.setText("");
		} else if (keycode == Keys.C
			&& (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT)) {
		    input.setText("");
		} else if (keycode == Keys.DOWN && line < history.size() - 1) {
		    line++;
		    input.setText(history.get(line));
		    input.setCursorPosition(input.getText().length());
		} else if (keycode == Keys.UP && line > 0) {
		    line--;
		    input.setText(history.get(line));
		    input.setCursorPosition(input.getText().length());
		}
		return true;
	    }

	    @Override
	    public boolean keyUp(InputEvent event, int keycode) {
		if (keycode == Keys.ENTER) {
		    scroll.setScrollY(display.getHeight());
		}
		return super.keyUp(event, keycode);
	    }
	});

	table.add(scroll).expand().fill();
	table.row();
	table.add(input).left().fillX();

	table.addActor(close);
    }

    public void open() {
	window.getCanvas().addActor(table);
    }

    public void close() {
	window.getCanvas().removeActor(table);
    }

    public Label getDisplay() {
	return display;
    }

    public ScrollPane getScroll() {
	return scroll;
    }
}
