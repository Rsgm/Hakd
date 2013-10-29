package hakd.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import hakd.game.gameplay.Player;
import hakd.networks.BackboneProviderNetwork;
import hakd.networks.InternetProviderNetwork;
import hakd.networks.Network;
import hakd.networks.devices.Device;
import hakd.networks.devices.Server;

public class NetworkFactory {

    /**
     * Create a network, and populate it, based on the type
     */
    public static Network createNetwork(Network.NetworkType type) {
        Network network = createNetwork();
        network.setLevel((int) (Math.random() * 8));
        network.setStance(Network.Stance.NEUTRAL); // TODO move stances to the npc/player classes
        network.setType(type);

        ModelBuilder modelBuilder = new ModelBuilder();

        // used to add randomness to the amount of servers to make given serverLimit
        int d = -1;

        switch (type) {
            case NPC:
                network.setIpRegion(Network.IpRegion.NA);
                network.setOwner("NPC");
                network.setDeviceLimit(4);
                network.setStance(Network.Stance.NEUTRAL);
                network.setSphere(modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
                break;
            case TEST:
                network.setIpRegion(Network.IpRegion.ASIA);
                network.setOwner("Test");
                network.setDeviceLimit(32);
                network.setStance(Network.Stance.NEUTRAL);
                network.setSphere(modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
                break;
            case BUSINESS: // company // random company
                network.setIpRegion(Network.IpRegion.BUSINESS);
                network.setDeviceLimit((int) ((network.getLevel() + 1) * (Math.random() * 3 + 1)));
                network.setOwner("company");
                network.setSphere(modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.ORANGE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
                break;
            default: // copied from the npc case
                network.setIpRegion(Network.IpRegion.NA);
                network.setOwner("some name");
                network.setDeviceLimit(4);
                network.setStance(Network.Stance.NEUTRAL);
                network.setSphere(modelBuilder.createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
                break;
        }

        d = (int) Math.round(network.getDeviceLimit() * (Math.random() * 0.35 + 0.65));

        for (int i = 0; i < d; i++) { // create servers on the network
            Server server = (Server) DeviceFactory.createDevice(Device.DeviceType.SERVER);
            network.addDevice(server);
            server.setNetwork(network);

        }

        return network;
    }

    /**
     * Creates a network and assigns it to the player. It also sets up the network's properties for the new game. Only used at the beginning(for now).
     *
     * @param player - The player to assign the network to.
     */
    public static Network createPlayerNetwork(Player player) {
        Network network = createNetwork();

        network.setType(Network.NetworkType.PLAYER);
        network.setLevel(0);
        network.setStance(Network.Stance.FRIENDLY);
        network.setSphere(new ModelBuilder().createSphere(5f, 5f, 5f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.BLUE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

        network.setPlayer(player);
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
    public static InternetProviderNetwork createISP(Internet internet) {
        InternetProviderNetwork isp = new InternetProviderNetwork(internet);

        isp.setType(Network.NetworkType.ISP);
        isp.setLevel((int) (Math.random() * 3 + 5));
        isp.setIpRegion(Network.IpRegion.values()[internet.getInternetProviderNetworks().size() % Network.IpRegion.values().length]);
        isp.setDeviceLimit((int) ((isp.getLevel() + 1) * (Math.random() * 3 + 1)));
        isp.setOwner(InternetProviderNetwork.IspName.values()[(int) (Math.random() * InternetProviderNetwork.IspName.values().length)].toString());
        isp.setSphere(new ModelBuilder().createSphere(15f, 15f, 15f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.CYAN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

        return isp;
    }

    /**
     * Most basic backbone constructor.
     */
    public static BackboneProviderNetwork createBackbone(Internet internet) {
        BackboneProviderNetwork backbone = new BackboneProviderNetwork(internet);

        backbone.setType(Network.NetworkType.BACKBONE);
        backbone.setIpRegion(Network.IpRegion.values()[internet.getBackboneProviderNetworks().size() % Network.IpRegion.values().length]);
        backbone.setDeviceLimit((int) ((backbone.getLevel() + 1) * (Math.random() * 3 + 1)));
        backbone.setLevel(7);
        backbone.setSphere(new ModelBuilder().createSphere(25f, 25f, 25f, 20, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

        backbone.setSphereInstance(new ModelInstance(backbone.getSphere()));
        backbone.getSphereInstance().transform.setToTranslation(backbone.getSpherePosition());

        return backbone;
    }


}