package hakd.networks;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import hakd.game.Internet;

/**
 * Provides internet access to networks.
 */
public final class InternetProviderNetwork extends Network {

    public InternetProviderNetwork(Internet internet) {
        super(NetworkType.ISP, internet); // this is ok

        this.owner = IspName.values()[(int) (Math.random() * IspName.values().length)].toString();
    }

    /**
     * For non-provider networks.
     */
    public void register(Network network, int speed) {
        network.setParent(this);
        network.setSphereInstance(new ModelInstance(sphere));

        float regionSize = ispRegionSize;
        final float x = parent.getSpherePosition().x + (float) ((Math.random() * regionSize) - regionSize / 2);
        final float y = parent.getSpherePosition().y + (float) ((Math.random() * regionSize) - regionSize / 2);
        final float z = parent.getSpherePosition().z + (float) ((Math.random() * regionSize) - regionSize / 2);
        network.setSpherePosition(new Vector3(x, y, z));

        network.getSphereInstance().transform.setToTranslation(spherePosition);

        network.setIp(internet.assignIp(this));
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
}
