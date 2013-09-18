package hakd.networks;

import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;

public interface ProviderNetwork extends Network {

	public short[] register(Router router, int speed);

	public Dns getMasterDns();

	public void setMasterDns(Dns masterDns);
}
