package hakd.gameplay;

import hakd.internet.NetworkController;
import hakd.networks.Network;
import hakd.networks.ServiceProvider;
import hakd.other.enumerations.NetworkType;
import hakd.other.enumerations.names.Isp;

import java.util.ArrayList;
import java.util.Arrays;

public class GamePlay {
	public static void generateGame() {
		generateIsps();
		generateNetworks();
	}

	private static void generateIsps() {
		int amount = (int) (Math.random() * 6 + 6);
		ArrayList<Isp> names = new ArrayList<Isp>(Arrays.asList(Isp.values().clone()));

		for (int i = 0; i < amount; i++) {
			int random = (int) (Math.random() * names.size());
			ServiceProvider s = new ServiceProvider(names.get(random));
			s.setIsp(s);
			s.setIp(s.getIsp().register(s, 10));
			NetworkController.getServiceProviders().add(s);
			names.remove(random);
		}
	}

	private static void generateNetworks() {
		int amount = (int) (Math.random() * 6 + 30);
// ArrayList<Owner> names = (ArrayList<Owner>) Arrays.asList(Owner.values());

		for (int i = 0; i < amount; i++) {
// int random = (int) (Math.random() * names.size());
			Network s = new Network(NetworkType.NPC/*names.get(random)*/);
// names.remove(random);
		}
	}
}
