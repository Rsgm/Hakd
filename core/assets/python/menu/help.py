#@help:help - Shows this help text. Also [...] are optional arguments and {...} are required arguments.
from game.pythonapi.menu import PyMenu
from other import Util
import os

def help(path):
    file_list = os.listdir(path)
    for name in file_list:
        url = path + '/' + name
        if os.path.isfile(url) and name.endswith('.py'):
            f = open(url, 'r')
            message = f.readline()
            if message.startswith('#@help:'):
                message = message[7:]
                if message.endswith('\n'):
                    message = message[:len(message)-1]
                PyMenu.write(screen, message)
        elif os.path.isdir(url):
            help(url)

help(Util.RESOURCES.getPath() + '/python/menu')

