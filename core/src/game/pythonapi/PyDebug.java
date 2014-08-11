package game.pythonapi;

import game.Hakd;
import game.Internet;
import game.gameplay.City;
import game.gameplay.Player;
import gui.windows.device.Terminal;
import networks.InternetProviderNetwork;
import networks.Network;
import networks.NetworkFactory;

public class PyDebug {
    private final Terminal terminal;

    public PyDebug(Terminal terminal) {
        this.terminal = terminal;
    }

    public Internet getInternet() {
        return terminal.getDevice().getNetwork().getInternet();
    }

    public Player getPlayer() {
        return Hakd.HAKD.getGamePlay().getPlayer();
    }

    public Network createNetwork(City city, Internet internet, InternetProviderNetwork isp) {
        return NetworkFactory.createNetwork(Network.NetworkType.TEST, isp);
    }


}
