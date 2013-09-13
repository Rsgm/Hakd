from hakd.game.pythonapi import PyDisplay, PyNetworking
from hakd.internet.Internet import Protocol
from hakd.internet import Port
import random

p_list = Protocol.values()
protocol = p_list[random.randint(0,len(p_list))]


device = PyNetworking.get_device(PyNetworking.ip_from_string(parameters[0]))
device.openPort(Port(protocol))
