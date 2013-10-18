package hakd.networks;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import hakd.game.Internet;

/**
 * Backbones are the infrastructure that make up the internet. Backbones connect
 * ISPs to the internet, as well as connecting other backboneProviderNetworks
 * together.
 */
public final class BackboneProviderNetwork extends Network {

    private Model[] connectionLine;
    private ModelInstance[] connectionInstance;
    private Vector3[] connectionPosition;

    public BackboneProviderNetwork(Internet internet) {
        super(NetworkType.BACKBONE, internet); // this is ok

        this.owner = BackboneName.values()[(int) (Math.random() * BackboneName.values().length)].toString();

        parent = null;
        parent = null;
        ip = new short[]{internet.generateIpByte(ipRegion), internet.generateIpByte(IpRegion.none), internet.generateIpByte(IpRegion.none), 1};

        spherePosition = new Vector3((float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2));

        for (BackboneProviderNetwork b : internet.getBackboneProviderNetworks()) {
            //TODO put gui connection line code here
        }
    }

    /**
     * For non-provider networks.
     */
    public void register(Network network, int speed) {
        network.setParent(this);

        network.setSphereInstance(new ModelInstance(sphere));

        float regionSize = BackboneRegionSize;
        final float x = parent.getSpherePosition().x + (float) ((Math.random() * regionSize) - regionSize / 2);
        final float y = parent.getSpherePosition().y + (float) ((Math.random() * regionSize) - regionSize / 2);
        final float z = parent.getSpherePosition().z + (float) ((Math.random() * regionSize) - regionSize / 2);
        network.setSpherePosition(new Vector3(x, y, z));

        network.getSphereInstance().transform.setToTranslation(spherePosition);

        network.setIp(internet.assignIp(this));
    }

    public enum BackboneName { // these may change
        /*
         * these are just some names I came up with, they are in no way
         * referencing real companies. for now I will use the greek alphabeta
         * until I think of company names
         *
         * Lynk(), T1Com(), Spirit(), Wnetwork(), KableTown(), WalkerInc(),
         * BlackHat;
         */
        Alpha(), Beta(), Gamma(), Delta(), Epsilon(), Zeta(), Eta(), Theta(), Iota(), Kappa(), Lambda(), Mu(), Nu(),
        Xi(), Omnicron(), Pi(), Rho(), Sigma(), Tau(), Upsilon(), Phi(), Chi(), Psi(), Omega();

        // I don't think I will get very indepth with these.

        BackboneName() {
        }
    }
}
