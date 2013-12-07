package hakd.networks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import hakd.game.Internet;
import hakd.gui.Assets;

import java.util.HashMap;

/**
 * Provides internet access to networks.
 */
public final class InternetProviderNetwork extends Network {
    private final HashMap<String, Network> ipChildNetworkHashMap = new HashMap<String, Network>(255);

    private Sprite territory;

    public InternetProviderNetwork(Internet internet) {
        super();
        this.internet = internet;
    }

    /**
     * For non-provider networks.
     */
    public void registerANetwork(Network network, int speed) {
        network.setParent(this);
        network.setIp(internet.assignIp(network));
        network.setSpeed(speed);
        ipChildNetworkHashMap.put(network.getIp(), network);


        // network's mapIcon for the map
        float regionSize = ispRegionSize;
        positionLoop:
        for (int i = 0; i < 5000; i++) {
            Circle c = new Circle(0, 0, regionSize / 2);
            Vector2 v = new Vector2();
            do {
                v.x = (float) ((Math.random() * regionSize) - regionSize / 2);
                v.y = (float) ((Math.random() * regionSize) - regionSize / 2);
            } while (!c.contains(v));
            v.x += mapIcon.getX();
            v.y += mapIcon.getY();
            network.getMapIcon().setPosition(v.x, v.y);

            int j = 0;
            for (Network n : internet.getIpNetworkHashMap().values()) {
                j++;
                if (v.dst2(n.getMapIcon().getX(), n.getMapIcon().getY()) <= networkRegionSize * networkRegionSize && n != network) {
                    break;
                } else if (j >= internet.getIpNetworkHashMap().size()) {
                    Gdx.app.debug("Network added", "Found an open spot");
                    break positionLoop;
                }
            }
        }

        // connection line between isp and backbone(this)
        Vector2 v1 = new Vector2(mapIcon.getX() + (mapIcon.getWidth() / 2), mapIcon.getY() + (mapIcon.getHeight() / 2));
        Vector2 v2 = new Vector2(network.mapIcon.getX() + (network.mapIcon.getWidth() / 2), network.mapIcon.getY() + (network.mapIcon.getHeight() / 2));
        Sprite line = Assets.nearestTextures.createSprite("dashedLine");
        line.setOrigin(0, 0);
        line.setSize(v1.dst(v2), 3);
        line.setPosition(v1.x, v1.y);
        line.setRotation(v1.sub(v2).scl(-1).angle());
        network.setMapParentLine(line);
    }

    public void unregister() {

    }

    public enum IspName {
        /*
         * these are just some names I came up with, they are in no way
         * referencing real companies, infinity LTD. for now I will use the
         * greek alphabet until I think of company names
         */
        Alpha(), Beta(), Gamma(), Delta(), Epsilon(), Zeta(), Eta(), Theta(), Iota(), Kappa(), Lambda(), Mu(), Nu(),
        Xi(), Omnicron(), Pi(), Rho(), Sigma(), Tau(), Upsilon(), Phi(), Chi(), Psi(), Omega();

		/*
         * public int price; // I think I can use gooeyFiber(70, 99), maybe name
		 * all fiber isp's after liquids public int level;
		 */

        IspName(/* , int price in $, int level 0-00 */) {
            // this.price = price;
            // this.level = level;
        }
    }

    public HashMap<String, Network> getIpChildNetworkHashMap() {
        return ipChildNetworkHashMap;
    }

    public Sprite getTerritory() {
        return territory;
    }

    public void setTerritory(Sprite territory) {
        this.territory = territory;
    }
}
