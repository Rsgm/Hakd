package hakd.networks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import hakd.game.Internet;
import hakd.gui.Assets;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Backbones are the infrastructure that make up the internet. Backbones connect
 * ISPs to the internet, as well as connecting other backboneProviderNetworks
 * together.
 */
public final class BackboneProviderNetwork extends Network {
    private ArrayList<Sprite> backboneConnectionLines;
    private final HashMap<String, InternetProviderNetwork> ipChildNetworkHashMap = new HashMap<String, InternetProviderNetwork>(255);

    public BackboneProviderNetwork(Internet internet) {
        super();

        this.internet = internet;
        parent = null;
        mapIcon = Assets.linearTextures.createSprite("backboneNetwork");
        mapIcon.setSize(100, 100);
        backboneConnectionLines = new ArrayList<Sprite>();

        // find open area
        float regionSize = 250;
        positionLoop:
        for (int k = 0; k < 30; k++) {
            for (int i = 0; i < 10; i++) {
                Circle c = new Circle(0, 0, regionSize / 2);
                Vector2 v = new Vector2();
                do {
                    v.x = (float) ((Math.random() * regionSize) - regionSize / 2);
                    v.y = (float) ((Math.random() * regionSize) - regionSize / 2);
                } while (!c.contains(v));
                v.x += mapIcon.getX();
                v.y += mapIcon.getY();
                mapIcon.setPosition(v.x, v.y);

                if (internet.getIpNetworkHashMap().values().size() == 0) {
                    break positionLoop;
                }

                int j = 0;
                for (Network n : internet.getIpNetworkHashMap().values()) {
                    j++;
                    if (v.dst2(n.getMapIcon().getX(), n.getMapIcon().getY()) <= BackboneRegionSize * BackboneRegionSize && n != this) {
                        break;
                    } else if (j >= internet.getIpNetworkHashMap().size()) {
                        Gdx.app.debug("Network added", "Found an open spot");
                        break positionLoop;
                    }
                }
            }

            regionSize += 250;
        }

        // create backbone lines
        for (BackboneProviderNetwork b : internet.getBackboneProviderNetworks()) {
            Vector2 v1 = new Vector2(mapIcon.getX() + (mapIcon.getWidth() / 2), mapIcon.getY() + (mapIcon.getHeight() / 2));
            Vector2 v2 = new Vector2(b.mapIcon.getX() + (b.mapIcon.getWidth() / 2), b.mapIcon.getY() + (b.mapIcon.getHeight() / 2));
            Sprite line = Assets.nearestTextures.createSprite("dashedLine");
            line.setOrigin(0, 0);
            line.setSize(v1.dst(v2), 3);
            line.setPosition(v1.x, v1.y);
            line.setRotation(v1.sub(v2).scl(-1).angle()); // I am proud of this
            backboneConnectionLines.add(line);
        }
    }

    /**
     * For ISP networks.
     */
    public void registerAnIsp(InternetProviderNetwork isp, int speed) {
        isp.setParent(this);
        isp.setIp(internet.assignIp(isp));
        isp.setSpeed(speed);
        ipChildNetworkHashMap.put(isp.getIp(), isp);

        // isp's mapIcon for the map
        float regionSize = BackboneRegionSize;
        positionLoop:
        for (int i = 0; i < 1000; i++) {
            Circle c = new Circle(0, 0, regionSize / 2);
            Vector2 v = new Vector2();
            do {
                v.x = (float) ((Math.random() * regionSize) - regionSize / 2);
                v.y = (float) ((Math.random() * regionSize) - regionSize / 2);
            } while (!c.contains(v));
            v.x += mapIcon.getX();
            v.y += mapIcon.getY();
            isp.getMapIcon().setPosition(v.x, v.y);

            int j = 0;
            for (Network n : internet.getIpNetworkHashMap().values()) {
                j++;
                if (v.dst2(n.getMapIcon().getX(), n.getMapIcon().getY()) <= ispRegionSize * ispRegionSize && n != isp) {
                    System.out.println("ISP: too close to another");
                    break;
                } else if (j >= internet.getIpNetworkHashMap().size()) {
                    System.out.println("ISP: Found an open spot");
                    break positionLoop;
                }
            }
        }

        Sprite t = Assets.linearTextures.createSprite("circle");
//        t.setColor(0, 0, 1, 1);
        t.setSize(Network.ispRegionSize * 1.1f, Network.ispRegionSize * 1.1f);
        t.setPosition(isp.getMapIcon().getX() + isp.getMapIcon().getWidth() / 2 - t.getWidth() / 2, isp.getMapIcon().getY() + isp.getMapIcon().getHeight() / 2 - t.getHeight() / 2);
        isp.setTerritory(t);


        // connection line between isp and backbone(this)
        Vector2 v1 = new Vector2(mapIcon.getX() + (mapIcon.getWidth() / 2), mapIcon.getY() + (mapIcon.getHeight() / 2));
        Vector2 v2 = new Vector2(isp.mapIcon.getX() + (isp.mapIcon.getWidth() / 2), isp.mapIcon.getY() + (isp.mapIcon.getHeight() / 2));
        Sprite line = Assets.nearestTextures.createSprite("dashedLine");
        line.setOrigin(0, 0);
        line.setSize(v1.dst(v2), 3);
        line.setPosition(v1.x, v1.y);
        line.setRotation(v1.sub(v2).scl(-1).angle());
        isp.setMapParentLine(line);
    }

    public enum BackboneName { // these may change
        /*
         * these are just some names I came up with, they are in no way
         * referencing real companies. for now I will use the greek alphabeta
         * until I think of company names
         *
         * Lynk(), T1Com(), Spirit(), Wnetwork(), KableTown(), WalkerInc(),
         * BlackHat();
         */
        Alpha(), Beta(), Gamma(), Delta(), Epsilon(), Zeta(), Eta(), Theta(), Iota(), Kappa(), Lambda(), Mu(), Nu(),
        Xi(), Omnicron(), Pi(), Rho(), Sigma(), Tau(), Upsilon(), Phi(), Chi(), Psi(), Omega();

        // I don't think I will get very indepth with these.

        BackboneName() {
        }
    }

    public ArrayList<Sprite> getBackboneConnectionLines() {
        return backboneConnectionLines;
    }

    public void setBackboneConnectionLines(ArrayList<Sprite> backboneConnectionLines) {
        this.backboneConnectionLines = backboneConnectionLines;
    }

    public HashMap<String, InternetProviderNetwork> getIpChildNetworkHashMap() {
        return ipChildNetworkHashMap;
    }
}
