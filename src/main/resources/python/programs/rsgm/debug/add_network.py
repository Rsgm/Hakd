player_network = DEBUG.getPlayer().getNetwork()
internet = DEBUG.getInternet()
city = player_network.getCity()

network = DEBUG.createNetwork(city, internet)
isp = player_network.getParent()

internet.addNetworkToInternet(network, isp)

