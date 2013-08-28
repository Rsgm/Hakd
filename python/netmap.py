from hakd.game.pythonapi.PyDisplay import rewrite as rw, write as w
import time

w(device, "time until scan, 5")
time.sleep(1)
rw(device, "time until scan, 4", 0)
time.sleep(1)
rw(device, "time until scan, 3", 0)
time.sleep(1)
rw(device, "time until scan, 2", 0)
time.sleep(1)
rw(device, "time until scan, 1", 0)
time.sleep(1)
w(device, "done")

while 1==1:
	print("0")
