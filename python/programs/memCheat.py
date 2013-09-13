from hakd.game.pythonapi import PyDisplay

PyDisplay.write(terminal, "Old Storage Amount: " + str(device.getTotalStorage()))
device.totalStorage = int(parameters[0])
PyDisplay.write(terminal, "New Storage Amount: " + str(device.getTotalStorage()))
