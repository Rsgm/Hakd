#@help:nmap [options] {target} - A script full of powerful tools to scan networks.

def help():
	print 'help'

if (parameters[0].startswith('-') and 'h' in parameters[0]) or (parameters[1].startswith('-') and 'h' in parameters[1]):
	help()

"""
ip = parameters[0]
subnet = parameters[1]

if(subnet is None):
"""

