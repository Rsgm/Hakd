from game.pythonapi.PyDisplay import over_write as o, write as w
import time

w(terminal, "time until scan, 5")
time.sleep(1)
o(terminal, "time until scan, 4", 0)
time.sleep(1)
o(terminal, "time until scan, 3", 0)
time.sleep(1)
o(terminal, "time until scan, 2", 0)
time.sleep(1)
o(terminal, "time until scan, 1", 0)
time.sleep(1)
w(terminal, "done")

while 1==1:
	print("0")
