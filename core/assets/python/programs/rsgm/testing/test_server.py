from game.pythonapi import PyDisplay
from game.pythonapi import PyNetworking
from connection import Port
import time
from threading import Thread
import random

server_running = True

def server_thread(c):
	c.getHostPort().write(30)
	for i in range(30):
		if not server_running:
			break
		time.sleep(.5)
		c.getHostPort().write(random.randint(0, 255))


device = terminal.device
if device.getPorts().containsKey(21):
	port = device.getPorts().get(21)
else:
	device.openPort(Port('ftp server', 21, 'ftp'))
connections = list(device.getConnections().values())

while server_running:
	time.sleep(1)

	new_set = set(list(device.getConnections().values()))
	old_set = set(connections)

	print new_set
	print old_set

	new_connections = list(new_set - old_set)
	print new_connections

	connections = list(device.getConnections().values())

	for c in new_connections:
		PyDisplay.write(terminal, 'New connection from: ' + c.getClient().getIp())
		try:
			t = Thread(target=server_thread, args=(c,))
			#t.daemon = True
			t.start()
		except:
		   PyDisplay.write(terminal, 'Error: unable to start thread')
		   print 'Error: unable to start thread on server ' + str(device.getIp())
		   c.close()
	new_connections = []

