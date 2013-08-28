from hakd.game.pythonapi import PyDisplay
from hakd.internet.Internet import Protocol
from hakd.internet import Port

device.openPort(Port(Protocol.DNS.toString(), Protocol.DNS.portNumber,
		Protocol.DNS))
