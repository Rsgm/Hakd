package gui.windows.device;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import game.Hakd;
import other.Util;

public class Login { // PyTest window, I am not sure I will use this one. I may
    // use this before you can access some "websites".
    final com.badlogic.gdx.scenes.scene2d.ui.Window table;

    private final TextField username;
    private final TextField password;
    private final TextButton button;

    private final Skin skin = Hakd.assets.get("core/assets/skins/uiskin.json", Skin.class);

    public Login() {
        table = new com.badlogic.gdx.scenes.scene2d.ui.Window("Web Login", skin);
        table.setFillParent(true);

        // table.setBackground(new TextureRegionDrawable(Assets.nearestTextures
        // .findRegion("black")));

        username = new TextField("", skin);
        password = new TextField("", skin);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');

        button = new TextButton("Login", skin);
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(username.getText());
                System.out.println(password.getText());

                if (username.getText().equals("rsgm") && password.getText().equals("rsgm")) {
                    System.out.println("Login Accepted");
                }
                return true;
            }
        });

        table.add(new Label("Username:", skin));
        table.add(username);
        table.row();
        table.add(new Label("Password:", skin));
        table.add(password);
        table.row();
        table.add();
        table.add(button).left();

    }

    public Table open() {
        return table;
    }
}
