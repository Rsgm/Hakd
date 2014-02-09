#@help:cheat.py {program} [arguments] - A script full of cheaty debug tools.
from hakd.game.pythonapi import PyDisplay
from hakd.game.pythonapi import PyNetworking
from hakd.connection import Port

def help():
	PyDisplay.write(terminal, 'Programs available:' +
	'\n   open_port {target IP} {program} {port} - opens a port on the device' +
	'\n   close_port {target IP} {port} - connects to the device')

def open_port():
	if len(parameters) != 3:
		PyDisplay.write(terminal, 'Missing arguments.')
	else:
		ip = parameters[0]
		program = parameters[1]
		port = int(parameters[2])
		
		d = PyNetworking.get_device(ip)
		d.openPort(Port(program, port))

def close_port():
	if len(parameters) != 2:
		PyDisplay.write(terminal, 'Missing arguments.')
	else:
		ip = parameters[0]
		port = int(parameters[1])
		
		d = PyNetworking.get_device(ip)
		try:
			d.closePort(port)
		except IOError:
			PyDisplay.write(terminal, 'Port does not exist.')

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
	elif p == 'close_port':
		close_port()
