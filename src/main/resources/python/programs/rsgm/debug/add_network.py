player_network = DEBUG.getPlayer().getNetwork()
internet = DEBUG.getInternet()
city = player_network.getCity()

isp = player_network.getParent()
network = DEBUG.createNetwork(city, internet, isp)

