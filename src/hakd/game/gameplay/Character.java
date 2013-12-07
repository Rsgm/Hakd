package hakd.game.gameplay;

import hakd.networks.Network;

public class Character {

    int money = 0;       // in $ //add redundancy to money // triple redundancy with voting, maybe some rudimentary encryption, or no redundancy with strong encryption
    String name;            // does not really change
    Network network; // meant to be used as the players network base

    public Character(Network network, String name) {
        this.network = network;
        this.name = name;
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

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
