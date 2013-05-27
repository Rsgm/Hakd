package hakd.gui.input;

import hakd.game.Hakd;
import hakd.game.gameplay.Player;
import hakd.gui.screens.GameScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameInput implements InputProcessor {
	private final Hakd			game;

	private OrthographicCamera	cam;

	private Player				player;
	private GameScreen			screen;

	private int					lastMouseX;
	private int					lastMouseY;

	public GameInput(Game game, OrthographicCamera cam, Player player) {
		this.game = (Hakd) game;
		this.cam = cam;
		this.player = player;
	}

	@Override
	public boolean keyDown(int keycode) { // space or enter to interact with an object
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float deltaX = (lastMouseX - screenX) / (game.getWidth() / 15f) * cam.zoom;
		float deltaY = (screenY - lastMouseY) / (game.getHeight() / 15f) * cam.zoom;

		System.out.println(deltaX + "	" + deltaY + "	" + cam.position.x + "	" + cam.position.y);

		cam.translate(deltaX, deltaY);

		lastMouseX = screenX;
		lastMouseY = screenY;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		lastMouseX = screenX;
		lastMouseY = screenY;
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		if ((cam.zoom > 0.25 || amount > 0) && (cam.zoom < 5 || amount < 0)) {
			cam.zoom += amount / 20f;
		}

		cam.zoom = Math.round(cam.zoom * 100) / 100f; // round the zoom to the hundredths place
		System.out.println(cam.zoom);
		return true;
	}
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ormat">RGBA8888</enum>
        <key>contentProtection</key>
        <struct type="ContentProtection">
            <key>key</key>
            <string></string>
        </struct>
        <key>autoAliasEnabled</key>
        <true/>
        <key>trimSpriteNames</key>
        <false/>
        <key>globalSpriteSettings</key>
        <struct type="SpriteSettings">
            <key>scale</key>
            <double>1</double>
            <key>scaleMode</key>
            <enum type="ScaleMode">Smooth</enum>
            <key>innerPadding</key>
            <uint>0</uint>
            <key>extrude</key>
            <uint>0</uint>
            <key>trimThreshold</key>
            <uint>1</uint>
            <key>trimMode</key>
            <enum type="SpriteSettings::TrimMode">None</enum>
            <key>heuristicMask</key>
            <false/>
        </struct>
        <key>fileList</key>
        <array>
            <filename>graphics/draw.png</filename>
            <filename>graphics/loading.png</filename>
            <filename>graphics/loadingbackground.png</filename>
            <filename>graphics/test.png</filename>
            <filename>graphics/test3.png</filename>
            <filename>graphics/title.png</filename>
            <filename>graphics/untitled.png</filename>
            <filename>graphics/player0.png</filename>
        </array>
        <key>ignoreFileList</key>
        <array/>
        <key>replaceList</key>
        <array/>
        <key>ignoredWarnings</key>
        <array/>
        <key>commonDivisorX</key>
        <uint>1</uint>
        <key>commonDivisorY</key>
        <uint>1</uint>
    </struct>
</data>
