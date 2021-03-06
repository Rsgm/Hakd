#Update
As much as I don't want to give up on this game, I kind of have to.
My plan was to finish it before college and maybe workon it when I had time, boy was I wrong.
It is nowhere near finished and I have no time to work on it. I don't think I ever will either, not this version at least.
I have always hated how the code worked and constantly refactored the code how I liked it. It is just a huge mess though.
I have also grown to hate java. I may work on this game again, but not this project.
I love the idea and have no similar games to play. I may remake it in another language/framework at some point.


-----------------

#Hakd
Hakd is a single player game about hacking, with a realistic, simulated, and procedurally generated internet. This game simulates an entire internet for you to explore and mess around with all you want, legally.
 * Procedurally generated Internet(at game start, random seed) with thousands of networks to hack.
 * Completely open world, you can hack into/mess with any server on any network you can find.
 * The player controls a server farm. For the end game, think of google's or the nsa's data center. (not that I have seen either in person)

[My to-do list,](https://trello.com/b/aPgl1zpo/hak-d) This also has some of my ideas. (This is out of date)

[Skip down to Compiling and Building Hakd.](https://github.com/Rsgm/Hakd#building-and-running)

###Pictures

 * [The map of the internet](https://pbs.twimg.com/media/Be28I2MCcAARgYY.png:large) - You can kind of see the networks and cities. Cities are black dots, ISPs are yellow and networks are green.
 * [My most recent terminal testing](https://pbs.twimg.com/media/Bfmh5K2CIAAw7xY.png:large)
 * [Silly recursion testing](https://pbs.twimg.com/media/BZziL6iCYAA2dE9.png:large) - The numbers at the bottom is how deep the interpreters are nested. Yo dawg, I hear you like to see a python eating its self(this is actually fairly accurate)
 * [Player server room](https://pbs.twimg.com/media/BPKDzizCAAM8yrx.png:large) - This is a secondary room, that I haven't used in a while. Right now there is only a room with two servers. I might end up changing the graphics to vector art. Somewhat due to the recent comic on indie games, and also because it doesn't fit.
 * [The old 3d Internet map](https://pbs.twimg.com/media/BSP9-SxCMAALpyk.png:large) - I went away from this because it was tougher to make it look nice. I have been considering making a 2d map on a 3d sphere for the map.


###Instructions

To run:
 * [Download the latest release.](https://github.com/Rsgm/Hakd/releases)
 * Extract the Hakd/ folder in the zip archive anywhere.
 * Run either Hakd/bin/Hakd(Linux/Mac) or Hakd/bin/Hakd.bat(Windows) depending on your OS.
 * Optionally, run the file in command prompt or a terminal so you can see what is happening, and if there are any errors.

In the menu, type 'help' for a list of commands. Type start [name] to start the game. The game world will begin to generate, but there is no graphics, so check the terminal. It should take a few seconds to a few minutes depending on your processor.

Once the game starts:
Map:
 * Tab to open the map, tab again to close it.
 * Click and drag to move.
 * Mouse wheel to zoom.
 * 1-(6 or 7, I forget) to view the other noise maps. Only 1 and 2 have data I think.
 * Left control to view the noise value at the cursor in the terminal.

Player room:
 * Mouse wheel to zoom.
 * Click on a server to open the desktop.
 * Click and drag the app icons to move them. Please don't move them off the screen, you will not get them back.
 * Click the app icon to open the app.
 * Drag the title bar on the app window to move it. Moving it outside of the screen will snap it back inside.

Terminal:
 * Tab completion, can be used in some places. This can cause crashes if used with special characters like quotes.
 * 'help.py' to see a list of programs.
 * Programs must have .py at the end.
 * The file system is like the unix filesystem. Use 'ls' to list files and folders, and 'ls -h' for help.
 * Press control-c to stop a program. Warning, this is very dangerous because it does not tell the program it is stopping it.

I think those are all, but I could be wrong. Check the input processors(menu, map, and game) in .../gui/input/ if you want.


###Game world

The game world consists of thousands of networks all connected to the central Internet. The Internet and the rest of the world is randomly/procedurally generated down to the cpu speed on a server.
There is nothing stopping you from hacking into the ISP of the town over and sending logs of every packet to a storage based data center that you can remotely connect to.


###GamePlay

The main screen is a server room. The player can go up to any of his/her servers and access their terminals. Through these terminals, they can access and interact with the Internet. I will probably have more apps with guis though, like a web browser.

Players run commands from within a terminal. Seems simple enough, but the terminal is a python interpreter. This means that players can write their own scripts/programs to hack with and edit existing, pre-built programs.
All commands and programs are written in python. You will be able to buy and download default software from the stores or torrent sites in the game I write myself.


###Game Depth - All of this is fully implemented

If you are this far down, then you know about the player written hacking programs.

The internet is made up of networks, Backbones and ISPsThe Internet structure lets Internet Backbones and Internet Service Providers handle smaller networks and their data. All networks connect to regional ISPs. ISPs connect to Backbones to have access to other regions and their ISPs and networks.

Network hierarchy:
```
              Network
                 V
     __________Server___________
     V            V            V
FileSystem     Terminal      Parts -> | - CPU
                                      | - GPU
                                      | - Memory
                                      | - Storage
```


###Setting up the Project

This project uses gradle to build and optionally run.
These are all meant to be run in the command prompt/terminal, in the Hakd source directory. Any of these will download gradle if it has not yet been downloaded.

####Before gradle can be used, you must run this command first:
windows: ```./gradlew.bat cloneGdxShaders cloneLibNoise```  
linux/mac: ```./gradlew cloneGdxShaders cloneLibNoise```  
This will download the dependencies, not on maven, from github.
You will need to import the project after running this to add these dependencies to the classpath.

View gradle tasks and help:  
windows: ```./gradlew.bat tasks```  
linux/mac: ```./gradlew tasks```  
You have to let gradle build the project files for eclipse/intelij to know that it is a project. This will also download the dependencies.

For Intellij projects run:  
windows: ```./gradlew.bat idea```  
linux/mac: ```./gradle idea```

For Eclipse projects run:  
windows: ```./gradlew.bat eclipse```  
linux/mac: ```./gradle eclipse```


###Building and Running

To build Hakd, run:  
Windows: ```./gradlew.bat clean distZip```
Linux/Mac: ```./gradlew clean distZip```  
A packaged Hakd-{version}.zip will be placed in desktop/build/distributions/. This is can be used and released without any modification.

You can also run Hakd through gradle, if you don't want to package it:  
windows: ```./gradlew.bat run```
linux/mac: ```./gradlew run```  
This is faster than distZip, but it will have the same asset paths as in an IDE, which is different from the distZip asset paths and may raise compatibility issues between them.
