package hakd.game.gameplay;

import hakd.networks.Network;

public class Npc {
    // hackers

    private int money;
    private String name;
    private Network home;

    public Npc(Network n, String name, int money) {
	this.money = money;
	this.name = name;
	this.home = n;
    }

    public int getMoney() {
	return money;
    }

    public void setMoney(int money) {
	this.money = money;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Network getHome() {
	return home;
    }

    public void setHome(Network home) {
	this.home = home;
    }
}
