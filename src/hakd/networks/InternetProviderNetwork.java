package hakd.networks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import hakd.game.Internet;
import hakd.gui.Assets;

import java.util.HashMap;

/**
 * Provides internet access to networks.
 */
public final class InternetProviderNetwork extends Network {
	final private HashMap<String, Network> ipChildNetworkHashMap = new HashMap<String, Network>(255);

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
		for(int i = 0; i < internet.getIpNetworkHashMap().size() * 2; i++) {
			Vector2 v = new Vector2();
			v.x = mapIcon.getX() + (float) ((Math.random() * regionSize) - regionSize / 2);
			v.y = mapIcon.getY() + (float) ((Math.random() * regionSize) - regionSize / 2);
			network.getMapIcon().setPosition(v.x, v.y);

			int j = 0;
			for(Network n : internet.getIpNetworkHashMap().values()) {
				j++;
				if(v.dst2(n.getMapIcon().getX(), n.getMapIcon().getY()) <= networkRegionSize * networkRegionSize && n != network) {
					System.out.println("Network: too close to another");
					break;
				} else if(j >= internet.getIpNetworkHashMap().size()) {
					System.out.println("Network: Found an open spot");
					break positionLoop;
				}
			}
		}

		// connection line between isp and backbone(this)
		Vector2 v1 = new Vector2(mapIcon.getX() + (mapIcon.getWidth() / 2), mapIcon.getY() + (mapIcon.getHeight() / 2));
		Vector2 v2 = new Vector2(network.mapIcon.getX() + (network.mapIcon.getWidth() / 2), network.mapIcon.getY() + (network.mapIcon.getHeight() / 2));
		Sprite line = Assets.nearestTextures.createSprite("dashedLine");
		line.setOrigin(0, 0);
		line.setSize(v1.dst(v2), 1);
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
}
