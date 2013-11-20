#@help:port_scan [ip] - Scans for open ports on the specified device.
from hakd.game.pythonapi import PyNetworking
from hakd.game.pythonapi import PyDisplay

ip = parameters[0]
d = PyNetworking.get_device(ip)

PyDisplay.write(terminal, "Ports Open on the device with the ip " + ip + ":")

ports = d.getPorts()
if len(ports)==0:
	PyDisplay.write(terminal, "No ports are open.")

for p in ports:
	s = str(p.getPortNumber()) + " - "
	s+= str(p.getProgram())
	s+= " (" + str(p.getProtocol()) + ")"
	PyDisplay.write(terminal, s)

#def help():
	# port_number - program (protocol)

