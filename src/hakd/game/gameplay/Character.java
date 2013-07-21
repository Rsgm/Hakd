package hakd.game.gameplay;

import hakd.networks.Network;

public final class Character {

	int		money	= 0;
	String	name;			// does not really change
	Network	home;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public Network getHome() {
		return home;
	}

	public void setHome(Network home) {
		this.home = home;
	}

}
