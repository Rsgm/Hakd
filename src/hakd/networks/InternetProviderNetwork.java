package hakd.networks;

import hakd.internet.Internet;
import hakd.internet.Internet.Protocol;
import hakd.internet.Port;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;

/** Provides internet access to networks. */
public final class InternetProviderNetwork extends DefaultNetwork implements
	ProviderNetwork {
    private Dns masterDns;

    @Deprecated
    public InternetProviderNetwork(Internet internet) {
	super(NetworkType.ISP, internet); // this is ok

	this.owner = IspName.values()[(int) (Math.random() * IspName.values().length)]
		.toString();
	masterDns = dnss.get(0);

    }

    @Override
    public short[] register(Router router, int speed) {
	Port p = new Port(Protocol.DNS.toString(), Protocol.DNS.portNumber,
		Protocol.DNS);
	router.setSpeed(speed);

	router.openPort(p);
	masterDns.openPort(p);
	masterDns.Connect(router, p);

	router.getNode().connectBoth(masterDns.getParentRouter().getNode(),
		speed);

	return masterDns.assignIp();
    }

    public void unregister() {

    }

    public enum IspName {
	/*
	 * these are just some names I came up with, they are in no way
	 * referencing real companies, infinity LTD. for now I will use the
	 * greek alphabeta until I think of company names
	 */
	Alpha(), Beta(), Gamma(), Delta(), Epsilon(), Zeta(), Eta(), Theta(), Iota(), Kappa(), Lambda(), Mu(), Nu(), Xi(), Omnicron(), Pi(), Rho(), Sigma(), Tau(), Upsilon(), Phi(), Chi(), Psi(), Omega();

	/*
	 * public int price; // I think I can use gooeyFiber(70, 99), maybe name
	 * all fiber isp's after liquids public int level;
	 */

	IspName(/* , int price in $, int level 0-00 */) {
	    // this.price = price;
	    // this.level = level;
	}
    }

    @Override
    public Dns getMasterDns() {
	return masterDns;
    }

    @Override
    public void setMasterDns(Dns masterDns) {
	this.masterDns = masterDns;
    }
}
