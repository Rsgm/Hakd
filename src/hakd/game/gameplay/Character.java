package hakd.game.gameplay;

import hakd.networks.Network;

public class Character {
    String name;            // does not really change
    Network network; // meant to be used as the players network base
    City city;


    public Character(Network network, String name, City city) {
        this.network = network;
        this.name = name;
        this.city = city;
    }

    public void update() { // updates the character and runs its AI
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
