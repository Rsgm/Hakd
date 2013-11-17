#@help:ftp {ip} - Connects to a server and downloads the file being served.
from hakd.game.pythonapi import PyDisplay
from hakd.game.pythonapi import PyNetworking
from hakd.connection import Port
import time

try:
	parameters
except NameError:
	PyDisplay.write(terminal, 'Please provide the IP of the ftp server.')
	
else:
	ip = parameters[0]
	server = PyNetworking.get_device(ip)
	
	server.connect(terminal.device, Port('ftp', 21, 'ftp'))
	connection = terminal.device.getConnections().get(ip)
	input_stream = connection.getClientSocket().getInputStream()
	
	for i in range(15):
		time.sleep(1)
		data = input_stream.read()
		PyDisplay.write(terminal, str(data))
	
	print 'done'
	PyDisplay.write(terminal, 'finished.')
	connection.close()
