package hakd.game.gameplay;

import hakd.networks.DefaultNetwork;

public final class Npc {
    // hackers

    private int money;
    private String name;
    private DefaultNetwork home;

    public Npc(DefaultNetwork n, String name, int money) {
	this.money = money;
	this.name = name;
	this.home = n;
    }

    public enum People {

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

    public DefaultNetwork getHome() {
	return home;
    }

    public void setHome(DefaultNetwork home) {
	this.home = home;
    }
}
