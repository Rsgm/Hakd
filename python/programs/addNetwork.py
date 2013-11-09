from hakd.networks import NetworkFactory
from hakd.networks import Network

network = NetworkFactory.createNetwork(Network.NetworkType.TEST)
internet = terminal.device.network.internet
isp = internet.getInternetProviderNetworks().get(0)

internet.addNetworkToInternet(network, isp)
