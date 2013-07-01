package hakd.gui.windows.actors;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button extends Sprite {
    public static java.util.List<Button> buttons = new ArrayList<Button>();

    private Type type;

    public Button(TextureRegion region) {
	super(region);
	buttons.add(this);
    }

    // all of the places that use this.
    public static enum Type {

	Type() {

	}
    }

    public static enum SpecificType {

	SpecificType() {

	}
    }

    public void click() {
	switch (type) {
	// case shop;
	// shop.buttonClick(specificType);
	// break;
	}
    }
}
