from hakd.game.pythonapi import PyDisplay

PyDisplay.write(device, "Old Storage Amount: " + str(device.getTotalStorage()))
device.totalStorage = int(parameters[0])
PyDisplay.write(device, "New Storage Amount: " + str(device.getTotalStorage()))
