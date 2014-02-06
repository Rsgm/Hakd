#Hakd
Hakd is a game about hacking, with a realistic, simulated, and procedurally generated internet. This game simulates an entire internet for you to explore and mess around with all you want, legally.
 * Procedurally generated Internet(at game start, random seed) with thousands of networks to hack.
 * Completely open world, you can hack into/mess with any server on any network you can find.
 * The player controls a server farm. For the end game, think of google's or the nsa's data center. (not that I have seen either in person)

[My to-do list.](https://trello.com/b/aPgl1zpo/hak-d) This also has some of my ideas.


###Pictures

 * [The map of the internet](https://pbs.twimg.com/media/Be28I2MCcAARgYY.png:large) - You can kind of see the networks and cities. Cities are black dots, ISPs are yellow and networks are green.
 * [My most recent terminal testing](https://pbs.twimg.com/media/Bfmh5K2CIAAw7xY.png:large)
 * [Silly recursion testing](https://pbs.twimg.com/media/BZziL6iCYAA2dE9.png:large) - The numbers at the bottom is how deep the interpreters are nested. Yo dawg, I hear you like to see a python eating its self(this is actually fairly accurate)
 * [Player server room](https://pbs.twimg.com/media/BPKDzizCAAM8yrx.png:large) - This is a secondary room, that I haven't used in a while. Right now there is only a room with two servers. I might end up changing the graphics to vector art. Somewhat due to the recent comic on indie games, and also because it doesn't fit.
 * [The old 3d Internet map](https://pbs.twimg.com/media/BSP9-SxCMAALpyk.png:large) - I went away from this because it was tougher to make it look nice. I have been considering making a 2d map on a 3d sphere for the map.


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

                 Network
                    V
        __________Server___________
        V            V            V
    FileSystem     Terminal      Parts -> | - CPU
                                          | - GPU
                                          | - Memory
                                          | - Storage



###Required libraries
 - [libgdx](http://libgdx.badlogicgames.com/) [under lib/libgdx/]
   * gdx.jar
   * gdx-backend-lwjgl.jar
   * gdx-backend-lwjgl-natives.jar
   * gdx-natives.jar
   * gdx-freetype.jar
   * gdx-freetype-natives.jar

* [Jython](http://www.jython.org/) - jython-standalone-2.7-b1.jar
