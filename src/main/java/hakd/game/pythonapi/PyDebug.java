package hakd.game.pythonapi;

import hakd.Main;
import hakd.game.Internet;
import hakd.game.gameplay.City;
import hakd.game.gameplay.Player;
import hakd.gui.windows.device.Terminal;
import hakd.networks.Network;
import hakd.networks.NetworkFactory;

public final class PyDebug {
    private final Terminal terminal;

    public PyDebug(Terminal terminal) {
        this.terminal = terminal;
    }

    public Internet getInternet() {
        return terminal.getDevice().getNetwork().getInternet();
    }

    public Player getPlayer() {
        return Main.HAKD.getGamePlay().getPlayer();
    }

    public Network createNetwork(City city, Internet internet) {
        return NetworkFactory.createNetwork(Network.NetworkType.TEST, city, internet);
    }


}
