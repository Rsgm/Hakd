#@help:help.py - Shows this help text.
from hakd.game.pythonapi import PyDisplay
from hakd.other import Util
import os

def help():
    bin = terminal.getDevice().getBin()
    file_list = bin.getRecursiveFileList(bin)
    for f in file_list:
        if not f.isDirectory() and f.getName().endswith('.py'):
            data = f.getData()
            message = data[:data.find('\n')]
            if message.startswith('#@help:'):
                message = message[7:]
                PyDisplay.write(terminal, message)

help()

