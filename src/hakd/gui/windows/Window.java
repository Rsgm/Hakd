package hakd.gui.windows;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public interface Window { // these are little windows that pop up when you interact with stuff, even the settings

	public void render(Camera cam, SpriteBatch batch, float delta); // what to render and what to return?

	public void open(TextureAtlas textures);

	public void close();
}
