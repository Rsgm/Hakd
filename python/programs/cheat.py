#@help:cheat {program} [arguments] - A script full of cheaty debug tools.
from hakd.game.pythonapi import PyDisplay
from hakd.game.pythonapi import PyNetworking
from hakd.game.Internet import Protocol
from hakd.connection import Port
import random

def help():
	PyDisplay.write(terminal, 'Programs available:' +
	'\n   open_port {target IP} {target port} - opens the specified port of the device' +
	'\n   connect {target IP} {target port} - connects to the specified device on the port')

def open_port():
	ip = parameters[0]
	port = parameters[1]
	
	d = PyNetworking.get_device(ip_short)
	device.openPort(Port(Protocol.values()[]))

def connect()
	


try:
	parameters
except NameError:
	PyDisplay.write(terminal, 'Please provide some arguments. Type -h for help.')
	
else:
	p = parameters[0]
	del parameters[0]
	
	if p == '-h':
		help()
	elif p == 'open_port':
		open_port()
	elif p == 'connect':
		print 0
