#@help:start [name] - Starts a new game and gives the player the specified name.
from game.pythonapi.menu import PyMenu

#@parse_start
def getParser():
    p = OptionParser()
    p.acceptsAll(["h", "help"], "show help" ).forHelp # not sure why this doesn't work with the parenthesis: .forHelp()
    p.accepts("n", "sets the player name").requiredUnless("h").withRequiredArg().describedAs("name")
    return p
#@parse_end

# while True:
#     a = 0

print "test"
if options.has('n'):
    menu.setScreen(options.valueOf('n'))
