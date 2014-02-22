package hakd.game.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import hakd.gui.Assets;
import hakd.gui.screens.GameScreen;
import hakd.gui.windows.device.GameScene;
import hakd.other.Util;

public final class Player extends Character {
    private GameScene openWindow;
    private final GameScreen screen;
    private Sprite sprite;
    private int isoX;
    private int isoY;

    // --------methods--------
    public Player(String name, GameScreen screen, City city) {
        super(null, name, city);
        this.name = name;
        this.screen = screen;

        sprite = new Sprite(Assets.nearestTextures.findRegion("player0"));
    }

    public void move(float deltaX, float deltaY) {
        sprite.setX(sprite.getX() + deltaX);
        sprite.setY(sprite.getY() + deltaY);

        int[] coords = Util.orthoToIso(sprite.getX() - (sprite.getWidth()), sprite.getY(), screen.getRoom().getFloor().getHeight());
        isoX = coords[0];
        isoY = coords[1];
    }

    @Override
    public void update() {
    }

    public GameScene getOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(GameScene openWindow) {
        this.openWindow = openWindow;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getIsoX() {
        return isoX;
    }

    public void setIsoX(int isoX) {
        this.isoX = isoX;
    }

    public int getIsoY() {
        return isoY;
    }

    public void setIsoY(int isoY) {
        this.isoY = isoY;
    }
}
