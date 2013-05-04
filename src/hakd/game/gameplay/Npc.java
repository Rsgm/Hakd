package hakd.game.gameplay;

import hakd.networks.Network;

public class Npc {

	private int		money;
	private String	name;
	private Network	home;

	public Npc(Network n, String name, int money) {
		this.money = money;
		this.name = name;
		this.home = n;
	}

}
