#@help:history {show|clear} - Scans for open ports on the specified device.
from hakd.game.pythonapi import PyDisplay

try:
	parameters
except NameError:
	PyDisplay.write(terminal, 'Please specify either show or clear.')
	
else:	
	if parameters[0] == 'clear':
		terminal.getHistory().clear()
		terminal.setLine(0)
	else:
		history = terminal.getHistory()
		PyDisplay.write(terminal, 'Command history:')
		
		for s in history:
			PyDisplay.write(terminal, '   ' + s)

