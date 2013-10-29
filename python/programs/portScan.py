#@help:portScan [ip] - Scans for open ports on the specified device.
from hakd.game.pythonapi import PyNetworking as net
from hakd.game.pythonapi import PyDisplay as gfx
from hakd.internet.Internet import Protocol

ip_string = parameters[0]
ip_short = net.ip_from_string(ip_string)
d = net.get_device(ip_short)

gfx.write(terminal, "Ports Open on the device with the ip " + ip_string + ":")

ports = d.getPorts()
if len(ports)==0:
	gfx.write(terminal, "No ports are open.")

for p in ports:
	s = str(p.getPortNumber()) + " - "
	s+= str(p.getProgram())
	s+= " (" + str(p.getProtocol()) + ")"
	gfx.write(terminal, s)

#def help():
	# port_number - program (protocol)

