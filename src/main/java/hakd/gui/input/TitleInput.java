package hakd.gui.input;

import com.badlogic.gdx.InputProcessor;
import hakd.game.Hakd;
import hakd.gui.screens.MenuScreen;

public class TitleInput implements InputProcessor {
    private final Hakd game;

    public TitleInput(Hakd game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        game.setScreen(new MenuScreen(game));
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.setScreen(new MenuScreen(game));
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
