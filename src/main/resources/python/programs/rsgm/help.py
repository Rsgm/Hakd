#@help:help - Shows this help text.
import os

def help():
    bin = terminal.getDevice().getBin()
    file_list = bin.getRecursiveFileList(bin)
    for f in file_list:
        if not f.isDirectory():
            data = f.getData()
            message = data[:data.find('\n')]
            if message.startswith('#@help:'):
                message = message[7:]
                Display.write("", terminal, message)

help()

