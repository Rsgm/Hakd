package hakd.game.gameplay;

import hakd.networks.Network;

public interface Character {

	int		money	= 0;
	String	name;
	Network	home;

	public int getMoney();

	public void setMoney(int money);

	public Network getHome();

	public void setHome(Network home);
}
