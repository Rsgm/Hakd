#@help:nmap [IPv4 address] - Scans the ports of the device with the given address. USE AT OWN RISK.
#Taken from http://www.pythonforbeginners.com/code-snippets-source-code/port-scanner-in-python/
#
#Eddited a bit by Rsgm123

from game.pythonapi.menu import PyMenu
import socket
import sys
from datetime import datetime

try:
    # Ask for input
    remoteServer    = parameters[0]
    remoteServerIP  = socket.gethostbyname(remoteServer)

    # Print a nice banner with information on which host we are about to scan
    screen.addText("-" * 60)
    screen.addText("Please wait, scanning remote host " + remoteServerIP)
    screen.addText("This could take a few minutes.")
    screen.addText("-" * 60)

    # Check what time the scan started
    t1 = datetime.now()

    # Using the range function to specify ports (here it will scans all ports between 1 and 1024)

    # We also put in some error handling for catching errors

    for port in range(1,1024):
        try:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            result = sock.connect_ex((remoteServerIP, port))
            if result == 0:
                screen.addText("Port " + str(port) + ": Open")
            sock.close()

        except socket.gaierror:
            screen.addText('Hostname could not be resolved. Exiting')
            sys.exit()

        except socket.error:
    	    print str(port) + " - closed"

    # Checking the time again
    t2 = datetime.now()

    # Calculates the difference of time, to see how long it took to run the script
    total =  t2 - t1

    # Printing the information to screen
    screen.addText('Scanning Completed in: ' + str(total))
except IndexError:
    PyMenu.write(screen, 'Please provide the IP of the ftp server.')

