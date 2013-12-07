package hakd.game.gameplay;

import hakd.networks.Network;

public class Agency extends Company { // maybe only extend character
    // let these keep hackers on their network in a way that they don't interfere with the agency network maliciously(unless they turn against them I guess)

    public Agency(Network network, String name) {
        super(network, name);
    }
}
