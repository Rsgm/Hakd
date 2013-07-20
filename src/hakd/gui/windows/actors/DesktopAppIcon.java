package hakd.gui.windows.actors;

import hakd.gui.windows.server.ServerWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class DesktopAppIcon extends ImageButton {
    private boolean dragged = true;

    public DesktopAppIcon(Drawable imageUp, Drawable imageDown,
	    final ServerWindow o) {
	super(imageUp, imageDown);

	addListener(new InputListener() {
	    @Override
	    public boolean touchDown(InputEvent event, float x, float y,
		    int pointer, int button) {
		// down texture shade is black with alpha of 8 applied six times
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y,
		    int pointer, int button) {
		super.touchUp(event, x, y, pointer, button);
		if (!dragged) {
		    o.open();
		} else {
		    dragged = false;
		}
		setColor(Color.WHITE);
	    }

	    @Override
	    public void touchDragged(InputEvent event, float x, float y,
		    int pointer) {
		super.touchDragged(event, x, y, pointer);
		setColor(1, 1, 1, .6f);
		setPosition(
			(int) ((Gdx.input.getX() + 10 - getWidth() / 2) / 20) * 20,
			(int) ((Gdx.graphics.getHeight() + 10
				- Gdx.input.getY() - getHeight() / 2) / 20) * 20);
		dragged = true;
	    }
	});
	setSize(30, 30);
	setPosition(20, 20);
    }

}
