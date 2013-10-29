package hakd.game.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import hakd.gui.Assets;
import hakd.gui.screens.GameScreen;
import hakd.gui.windows.deviceapps.ServerWindowStage;
import hakd.networks.Network;
import hakd.other.Util;

public final class Player {
    // player stats
    private int money; // in $ //add redundancy to money // triple redundancy
    // with voting, maybe some rudimentary encryption, or no
    // redundancy with strong encryption
    private final String name;
    private Network network; // meant to be used as the players network base

    private final GameScreen screen;

    private Sprite sprite;
    private int isoX;
    private int isoY;

    private ServerWindowStage openWindow;

    // --------methods--------
    public Player(String name, GameScreen screen) {
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

    // --------getters/setters--------
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getName() {
        return name;
    }

    public ServerWindowStage getOpenWindow() {
        return openWindow;
    }

    public void setOpenWindow(ServerWindowStage openWindow) {
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
