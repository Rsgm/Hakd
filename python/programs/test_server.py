from hakd.game.pythonapi import PyDisplay
from hakd.game.pythonapi import PyNetworking
from hakd.connection import Port
import time
import random

device = terminal.device
connections = device.getConnections()

while 1==1 :
	time.sleep(1)
	
#	new = list(device.getConnections().values() - connections.values())
	print 'newlist'
#	for c in new:
#		print c
#		PyDisplay.write(terminal, 'New connection from: ' + c)
	connections = device.getConnections()
	print connections.values()
	
	for c in connections.values():
		out = c.getHostSocket().getOutputStream()
		out.write(random.randint(0, 255))
		out.flush()
