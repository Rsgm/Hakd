package hakd.gui.windows;

import hakd.gui.Assets;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Desktop {
    Table table = new Table();

    TextField display;
    TextField input;

    public Desktop() {
	table = new Table();
	table.setFillParent(false);

	table.setBackground(new TextureRegionDrawable(Assets.nearestTextures
		.findRegion("black")));

	TextFieldStyle displayStyle = new TextFieldStyle();
	displayStyle.background = new TextureRegionDrawable(
		Assets.nearestTextures.findRegion("black"));
	displayStyle.font = Assets.consoleFont;
	displayStyle.fontColor = Assets.consoleFontColor;

	TextFieldStyle inputStyle = new TextFieldStyle();
	inputStyle.background = new TextureRegionDrawable(
		Assets.nearestTextures.findRegion("black"));
	inputStyle.font = Assets.consoleFont;
	inputStyle.fontColor = Assets.consoleFontColor;

	display = new TextField("", displayStyle);
	input = new TextField("", inputStyle);

	ScrollPane s = new ScrollPane(display);

	table.add(s).prefHeight(600f).prefWidth(400f);
	table.row();
	table.add(input);
    }

    public Table open() {
	return table;
    }
}
