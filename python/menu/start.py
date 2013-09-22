#@help:start [name] - Starts a new game and gives the player the specified name.
from hakd.gui.screens import GameScreen
from hakd.game.pythonapi.menu import PyMenu

if not len(parameters) == 0:
    PyMenu.setScreen(screen, GameScreen(screen.game, parameters[0]))
else:
    PyMenu.write(screen, 'Please specify a name.')
