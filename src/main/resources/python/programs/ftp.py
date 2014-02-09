#@help:ftp.py {ip} - Connects to a server and downloads the file being served.
from hakd.game.pythonapi import PyDisplay
from hakd.game.pythonapi import PyNetworking
from hakd.connection import Port
import time
import random

try:
	parameters
	ip = parameters[0]
	server = PyNetworking.get_device(ip)
	device = terminal.device
	
	if device.getPorts().containsKey(21):
		port = device.getPorts().get(21)
		if port.isConnected:
			i = random.randint(40000, 65536)
			while device.getPorts().containsKey(i):
				i = random.randint(40000, 65536)
			port = Port('ftp', i)
			device.openPort(port)
	else:
		port = Port('ftp', 21)
		device.openPort(port)
	
	server.connect(terminal.device, port, 21)
#except NameError:
#	PyDisplay.write(terminal, 'Please provide the IP of the ftp server.')
#	
except IOError:
	terminal.device.closePort(21)
	PyDisplay.write(terminal, 'I/O Error - Port may be in use, or there was an error connecting.')
	
else:
	PyDisplay.write(terminal, '')
	connection = terminal.device.getConnections().get(ip)
	port = connection.getClientPort()
	input_list = port.getIn()
	PyDisplay.write(terminal, '\n')
	
	while input_list.isEmpty():
		time.sleep(.25)
	length = port.read()
	i = 0
	
	while i < length:
		i += 1
		if input_list.isEmpty():
			time.sleep(.25)
			i -= 1
		else:
			data = port.read()
			PyDisplay.over_write(terminal, str(data), 1)
			PyDisplay.over_write(terminal, 'Download ' + str(float(i)/float(length) * 100) + '% done.', 0)
	
	print 'done'
	PyDisplay.write(terminal, 'finished.')
	connection.close()

