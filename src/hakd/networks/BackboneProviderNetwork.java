package hakd.networks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
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
        super();

        this.internet = internet;
        this.owner = BackboneName.values()[(int) (Math.random() * BackboneName.values().length)].toString();

        parent = null;

        spherePosition = new Vector3((float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2), (float) ((Math.random() * worldSize) - worldSize / 2));

        for (BackboneProviderNetwork b : internet.getBackboneProviderNetworks()) {
            //TODO put gui connection line code here
        }
    }

    /**
     * For ISP networks.
     */
    public void registerAnIsp(Network isp, int speed) {
        isp.setParent(this);

        // isp's sphere for the 3d map
        isp.setSphereInstance(new ModelInstance(isp.getSphere()));
        float regionSize = BackboneRegionSize;
        final float x = spherePosition.x + (float) ((Math.random() * regionSize) - regionSize / 2);
        final float y = spherePosition.y + (float) ((Math.random() * regionSize) - regionSize / 2);
        final float z = spherePosition.z + (float) ((Math.random() * regionSize) - regionSize / 2);
        isp.setSpherePosition(new Vector3(x, y, z));
        isp.getSphereInstance().transform.setToTranslation(isp.getSpherePosition());

        // connection line between isp and backbone(this)
        Vector3 v1 = this.getSpherePosition().cpy();
        Vector3 v2 = isp.getSpherePosition().cpy();
        float distance = v1.dst(v2);

        isp.setParentConnectionLine(new ModelBuilder().createCylinder(1.2f, distance, 1.2f, 5, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

        isp.setParentConnectionInstance(new ModelInstance(isp.getParentConnectionLine()));

        Vector3 v3 = v1.cpy().add(v2).scl(.5f);
        Vector3 v4 = v2.cpy().sub(v3).nor();
        Vector3 v5 = v4.cpy().nor().crs(Vector3.Y).nor();

        final ModelInstance lineInstance = isp.getParentConnectionInstance();
        lineInstance.transform.translate(v3);
        lineInstance.transform.rotate(v5, -(float) Math.toDegrees(Math.acos(v4.dot(Vector3.Y))));


        isp.setIp(internet.assignIp(isp));
        isp.setSpeed(speed);
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
}
