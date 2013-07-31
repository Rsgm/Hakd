from hakd.game.pythonapi import Display

Display.write(device, "Old Storage Amount: " + str(device.getTotalStorage()))
device.totalStorage = int(parameters[0])
Display.write(device, "New Storage Amount: " + str(device.getTotalStorage()))
