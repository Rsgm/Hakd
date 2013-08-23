package hakd.game.gameplay;

import hakd.networks.DefaultNetwork;

public final class Character {

	int		money	= 0;
	String	name;			// does not really change
	DefaultNetwork	home;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public DefaultNetwork getHome() {
		return home;
	}

	public void setHome(DefaultNetwork home) {
		this.home = home;
	}

}
