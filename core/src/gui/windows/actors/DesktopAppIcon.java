package gui.windows.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import gui.windows.device.SceneWindow;

public class DesktopAppIcon extends ImageButton {
    private boolean dragged = true;

    public DesktopAppIcon(Drawable imageUp, Drawable imageDown, final SceneWindow window) {
        super(imageUp, imageDown);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (!dragged) {
                    window.open();
                } else {
                    dragged = false;
                }
                setColor(Color.WHITE);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                setColor(1, 1, 1, .6f);
                setPosition((int) ((Gdx.input.getX() + 10 - getWidth() / 2) / 20) * 20, (int) ((Gdx.graphics.getHeight() + 10 - Gdx.input.getY() - getHeight() / 2) / 20) * 20);
                dragged = true;
            }
        });
        setSize(30, 30);
    }
}
