from hakd.game.pythonapi import PyDisplay
from hakd.game.pythonapi import PyNetworking
from hakd.connection import Port
import time
import random

device = terminal.device
device.openPort(Port('ftp server', 21, 'ftp'))

while 1==1:
	connections = device.getConnections().values()	
	print len(connections)
	while len(connections) > 0 :
		time.sleep(1)
		
		#new = list(device.getConnections().values() - connections.values())
		print 'newlist'
		#for c in new:
		#	print c
		#	PyDisplay.write(terminal, 'New connection from: ' + c)
		connections = device.getConnections().values()
		print connections
		
		for c in connections:
			c.getHostPort().write(random.randint(0, 255))
