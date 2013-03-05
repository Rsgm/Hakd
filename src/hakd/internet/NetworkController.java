package hakd.internet;

import hakd.networks.Network;
import hakd.networks.ServiceProvider;
import hakd.networks.devices.Dns;

import java.util.ArrayList;

public class NetworkController {
	private static ArrayList<Network>			networks			= new ArrayList<Network>();
	private static ArrayList<Dns>				publicDns			= new ArrayList<Dns>();
	private static ArrayList<ServiceProvider>	serviceProviders	= new ArrayList<ServiceProvider>();

	public static ArrayList<Network> getNetworks() {
		return networks;
	}

	public static void setNetworks(ArrayList<Network> networks) {
		NetworkController.networks = networks;
	}

	public static ArrayList<ServiceProvider> getServiceProviders() {
		return serviceProviders;
	}

	public static void setServiceProviders(ArrayList<ServiceProvider> serviceProviders) {
		NetworkController.serviceProviders = serviceProviders;
	}

	public static ArrayList<Dns> getPublicDns() {
		return publicDns;
	}

	public static void setPublicDns(ArrayList<Dns> publicDns) {
		NetworkController.publicDns = publicDns;
	}
}
