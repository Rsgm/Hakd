package gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This is exactly the same as the sprite class, except that it holds an object.
 * This will be used for sprites that need to be clicked on to get the object they represent.
 * This should save many loop iterations that would otherwise be spent trying to find the object that holds this sprite.
 */
public class HakdSprite extends Sprite {
    private Object object;

    public HakdSprite() {
        super();
    }

    public HakdSprite(Texture texture) {
        super(texture);
    }

    public HakdSprite(Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
    }

    public HakdSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
    }

    public HakdSprite(TextureRegion region) {
        super(region);
    }

    public HakdSprite(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(region, srcX, srcY, srcWidth, srcHeight);
    }

    public HakdSprite(Sprite sprite) {
        super(sprite);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
