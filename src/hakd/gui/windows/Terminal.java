package hakd.gui.windows;

import hakd.gui.Assets;
import hakd.networks.devices.Device;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Terminal {
    private final Table table;

    private final TextField input;
    private final Label display;
    private final ScrollPane scroll;

    private final List<String> history; // command history
    private int line = 0; // holds the position of the history
    private Device device;

    public Terminal(/* Device d */) {
	Skin skin = Assets.skin;
	history = new ArrayList<String>();

	table = new com.badlogic.gdx.scenes.scene2d.ui.Window("Terminal",
		Assets.skin);
	table.setSize(Gdx.graphics.getWidth() * .9f,
		Gdx.graphics.getHeight() * .9f);

	input = new TextField("", skin.get("console", TextFieldStyle.class));
	display = new Label("", skin.get("console", LabelStyle.class));
	scroll = new ScrollPane(display, skin);

	display.setWrap(false);
	display.setAlignment(12, Align.left);
	// scroll.setTouchable(Touchable.disabled);

	input.addListener(new InputListener() {
	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		if (keycode == Keys.ENTER) {
		    System.out.println(input.getText());
		    display.setText(display.getText() + "\n" + input.getText());
		    history.add(input.getText());
		    // new Command(input.getText(), terminal.getDevice());

		    input.setText("");
		} else if (keycode == Keys.ESCAPE) {
		    close();
		} else if (keycode == Keys.C
			&& (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT)) {
		    input.setText("");
		} else if (keycode == Keys.DOWN && line < history.size() - 1) {
		    line++;
		    input.setText(history.get(line));
		} else if (keycode == Keys.UP && line > 0) {
		    line--;
		    input.setText(history.get(line));
		}
		return true;
	    }
	});

	table.add(scroll).expand().fill();
	table.row();
	table.add(input).left().fillX();
    }

    public Table open() {
	return table;
    }

    public void close() {

    }
}
