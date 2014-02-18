package hakd.gui.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hakd.game.Hakd;
import hakd.gui.Assets;
import hakd.gui.screens.GameScreen;
import hakd.other.Util;

public final class TutorialWindow implements Scene {
    private GameScreen screen;
    private final Stage stage;

    private final Window window;
    private final ScrollPane scroll;
    private final Table table;

    public TutorialWindow(int pos) {
        Skin skin = Assets.skin;

        stage = new Stage();
        window = new Window("Tutorial", skin);
        table = new Table(skin);
        scroll = new ScrollPane(table);

        stage.addActor(window);
        window.add(scroll).expand().fill();

        window.setSize(400, 200);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);

        Button close = new Button(skin);
        close.add("close");

        Button neverShow = new Button(skin);
        neverShow.add("never show\nagain");

        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                close();
            }
        });

        neverShow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ((Hakd) screen.getGame()).getGamePlay().setTutorialEnabled(false);
                close();
            }
        });


        table.add(Util.tutorialText(pos));
        table.row();
        table.add(close);
        table.add(neverShow);
    }

    @Override
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void open() {
        Gdx.input.setInputProcessor(stage);
        screen.setOpenWindow(this);
        stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void close() {
        stage.clear();
        screen.setOpenWindow(null);
        Gdx.input.setInputProcessor(screen.getInput());
    }

    @Override
    public void setScreen(GameScreen screen) {
        this.screen = screen;
    }
}
