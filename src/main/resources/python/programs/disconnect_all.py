from hakd.game.pythonapi import PyDisplay
from hakd.game.pythonapi import PyNetworking

connections = terminal.device.getConnections()
PyDisplay.write(terminal, 'Disconnecting from ' + str(len(connections)) + ' device(s).')
for c in connections.values():
	c.close()
