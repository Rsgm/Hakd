package hakd.networks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import hakd.game.Internet;
import hakd.game.gameplay.Character;
import hakd.game.gameplay.City;
import hakd.game.gameplay.Company;
import hakd.game.gameplay.Player;
import hakd.gui.Assets;
import hakd.networks.devices.Device;
import hakd.networks.devices.DeviceFactory;
import hakd.networks.devices.Server;
import hakd.other.Util;

import java.util.ArrayList;

public class NetworkFactory {

    /**
     * Create a network, and populate it, based on the type
     */
    public static Network createNetwork(Network.NetworkType type, City city, Internet internet) {
        Gdx.app.debug("Network Added", "Normal");

        Network network = createNetwork();
        int level = (int) (Math.random() * 8);
        Network.IpRegion ipRegion;
        Character owner;
        int deviceLimit;
        Network.Stance stance = Network.Stance.NEUTRAL; // TODO move stances to the npc/player classes
        Sprite mapIcon;

        switch (type) {
            case NPC:
                ipRegion = Network.IpRegion.PRIVATE;
                owner = new Character(network, Util.ganerateName(), city);
                deviceLimit = 4;
                stance = Network.Stance.NEUTRAL;
                mapIcon = Assets.linearTextures.createSprite("network");
                mapIcon.setSize(50, 50);
                break;
            case TEST:
                ipRegion = Network.IpRegion.PRIVATE;
                owner = new Character(network, Util.ganerateName(), city);
                deviceLimit = 32;
                stance = Network.Stance.NEUTRAL;
                mapIcon = Assets.linearTextures.createSprite("network");
                mapIcon.setSize(50, 50);
                break;
            case BUSINESS: // company // random company
                ipRegion = Network.IpRegion.BUSINESS;
                owner = new Company(network, Util.ganerateName(), city);
                deviceLimit = (int) ((level + 1) * (Math.random() * 3 + 1));
                mapIcon = Assets.linearTextures.createSprite("network");
                mapIcon.setSize(50, 50);
                break;
            default: // copied from the npc case
                ipRegion = Network.IpRegion.PRIVATE;
                owner = new Character(network, Util.ganerateName(), city);
                deviceLimit = 4;
                stance = Network.Stance.NEUTRAL;
                mapIcon = Assets.linearTextures.createSprite("network");
                mapIcon.setSize(50, 50);
                break;
        }

        // used to add randomness to the amount of servers to make given serverLimit
        int d = (int) Math.round(deviceLimit * (Math.random() * 0.35 + 0.65));

        for (int i = 0; i < d; i++) { // create servers on the network
            Server server = (Server) DeviceFactory.createDevice(Device.DeviceType.SERVER);
            network.addDevice(server);
            server.setNetwork(network);

        }

        network.setLevel(level);
        network.setStance(stance); // TODO move stances to the npc/player classes
        network.setType(type);
        network.setCity(city);
        network.setMapIcon(mapIcon);
        network.setOwner(owner);
        network.setInternet(internet);

        network.placeNetwork(Network.networkRegionSize);

        return network;
    }

    /**
     * Creates a network and assigns it to the player. It also sets up the network's properties for the new game. Only used at the beginning(for now).
     *
     * @param player - The player to assign the network to.
     */
    public static Network createPlayerNetwork(Player player, City city, Internet internet) {
        Gdx.app.debug("Network Added", "Player");
        Network network = createNetwork();

        network.setType(Network.NetworkType.PLAYER);
        network.setLevel(0);
        network.setIpRegion(Network.IpRegion.PRIVATE);
        network.setStance(Network.Stance.FRIENDLY);
        network.setCity(city);
        network.setInternet(internet);

        Sprite s = Assets.linearTextures.createSprite("playerNetwork");
        network.setMapIcon(s);
        network.getMapIcon().setSize(50, 50);
        network.placeNetwork(Network.networkRegionSize);

        network.setOwner(player);
        player.setNetwork(network);

        return network;
    }

    /**
     * Most basic network constructor.
     */
    public static Network createNetwork() {
        return new Network();
    }

    /**
     * Most basic ISP constructor.
     */
    public static InternetProviderNetwork createISP(Internet internet, City city) {
        Gdx.app.debug("Network Added", "ISP");
        InternetProviderNetwork isp = new InternetProviderNetwork(internet);

        isp.setOwner(new Company(isp, "ISP", city));

        isp.setType(Network.NetworkType.ISP);
        isp.setLevel((int) (Math.random() * 3 + 5));
        isp.setDeviceLimit((int) ((isp.getLevel() + 1) * (Math.random() * 3 + 1)));
        isp.setCity(city);
        //		isp.setOwner(InternetProviderNetwork.IspName.values()[(int) (Math.random() * InternetProviderNetwork.IspName.values().length)].toString());

        Sprite s = Assets.linearTextures.createSprite("ispNetwork");
        isp.setMapIcon(s);
        isp.getMapIcon().setSize(75, 75);

        boolean containsNetworks = false;
        for (Network n : internet.getIpNetworkHashMap().values()) {
            if (!(n instanceof BackboneProviderNetwork || n instanceof InternetProviderNetwork)) {
                containsNetworks = true;
                break;
            }
        }

        if (containsNetworks) {
            isp.placeNetwork(Network.networkRegionSize);
        } else {
            isp.placeNetwork(Network.ispRegionSize);
        }

        return isp;
    }

    /**
     * Most basic backbone constructor.
     */
    public static BackboneProviderNetwork createBackbone(Internet internet, City city) {
        Gdx.app.debug("Network Added", "Backbone");
        BackboneProviderNetwork backbone = new BackboneProviderNetwork(internet);

        final String name = BackboneProviderNetwork.BackboneName.values()[(int) (Math.random() * BackboneProviderNetwork.BackboneName.values().length)].toString();
        backbone.setOwner(new Company(backbone, name, city));

        backbone.setType(Network.NetworkType.BACKBONE);
        backbone.setDeviceLimit((int) ((backbone.getLevel() + 1) * (Math.random() * 3 + 1)));
        backbone.setLevel(7);
        backbone.setCity(city);

        backbone.parent = null;
        backbone.mapIcon = Assets.linearTextures.createSprite("backboneNetwork");
        backbone.mapIcon.setSize(100, 100);
        backbone.backboneConnectionLines = new ArrayList<Sprite>();

        boolean containsNetworks = false;
        for (Network n : internet.getIpNetworkHashMap().values()) {
            if (!(n instanceof BackboneProviderNetwork || n instanceof InternetProviderNetwork)) {
                containsNetworks = true;
                break;
            }
        }

        if (containsNetworks) {
            backbone.placeNetwork(Network.networkRegionSize);
        } else {
            backbone.placeNetwork(Network.backboneRegionSize);
        }

        backbone.ipRegion = Network.IpRegion.none;
        return backbone;
    }


}
