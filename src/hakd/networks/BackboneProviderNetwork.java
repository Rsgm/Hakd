package hakd.networks;

import hakd.connection.Port;
import hakd.game.Internet;
import hakd.game.Internet.Protocol;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;

/**
 * Backbones are the infrastructure that make up the internet. Backbones connect
 * ISPs to the internet, as well as connecting other backboneProviderNetworks
 * together.
 */
public final class BackboneProviderNetwork extends Network implements ProviderNetwork {
    private Dns masterDns;

    @Deprecated
    @SuppressWarnings("deprecation")
    public BackboneProviderNetwork(Internet internet) {
        super(NetworkType.BACKBONE, internet); // this is ok

        this.owner = BackboneName.values()[(int) (Math.random() * BackboneName.values().length)].toString();
        masterDns = dnss.get(0);
    }

    @Override
    public short[] register(Router router, int speed) {
        Port p = new Port(Protocol.DNS.toString(), Protocol.DNS.portNumber, Protocol.DNS);
        router.setSpeed(speed);

        router.openPort(p);
        masterDns.openPort(p);
        masterDns.Connect(router, p);

        router.getNode().connectBoth(masterDns.getParentRouter().getNode(), speed);

        return masterDns.assignIp();
    }

    @Override
    public Dns getMasterDns() {
        return masterDns;
    }

    @Override
    public void setMasterDns(Dns masterDns) {
        this.masterDns = masterDns;
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
