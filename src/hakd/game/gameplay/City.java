package hakd.game.gameplay;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import hakd.game.Noise;
import hakd.gui.Assets;
import hakd.networks.Network;
import hakd.other.Util;

import java.util.ArrayList;
import java.util.List;

public class City {
    public static final int width = 150;
    public static final int height = 150;

    private final Vector2 position;
    private final Sprite icon;
    private final String name;
    private final float density;
    private final List<Network> networks;

    public City(Vector2 position) {
        this.position = position;

        icon = Assets.linearTextures.createSprite("circle");
        icon.setBounds(position.x - width / 2, position.y - height / 2, width, height);

        name = Util.ganerateCityName();
        density = (float) Noise.DENSITY.getValue(position.x, 0, position.y);
        networks = new ArrayList<Network>();
    }

    public void addNetwork(Network n) {
        networks.add(n);
    }

    public void removeNetwork(Network n) {
        networks.remove(n);
    }

    @Override
    public String toString() {
        return name;
    }

    public Sprite getIcon() {
        return icon;
    }

    public Vector2 getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public float getDensity() {
        return density;
    }

    public List<Network> getNetworks() {
        return networks;
    }
}
