#Adventure Game

#Andrew D. Sapolnick, David Lear, Suved Adkar (C)2010

    #This program is free software: you can redistribute it and/or modify
    #it under the terms of the GNU General Public License as published by
    #the Free Software Foundation, either version 3 of the License, or
    #(at your option) any later version.

    #This program is distributed in the hope that it will be useful,
    #but WITHOUT ANY WARRANTY; without even the implied warranty of
    #MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    #GNU General Public License for more details.

    #You should have received a copy of the GNU General Public License
    #along with this program.  If not, see <http://www.gnu.org/licenses/>.


#I did edit this enough to run with hakd. the orriginal can be found here http://sourceforge.net/projects/pythontextadven/files/?source=navbar
#-rsgm

from game.pythonapi import PyDisplay

PyDisplay.write(terminal, "Welcome to the Adventure Game, here you will embark on a dangerous journey to defeat the evil Dragon named Kibbles, and rescue Princess Catherine, You're story begins in the small town of Jersey, type the commands north, south, east, and west in order to navigate,type the command use followed by the item name to use an item, type the command inventory to look at the items in your posession, type the command look to look around you, type the command search followed by a target to search for people or items, type the command commands to repeat these directions. Good luck")


items=[]

place=1

town=2

forest=3

mine=4

lair=5

dead=6

game=0

on=90

win=91

game=on

place=town

lose=92


while game==on:


    while place==town:

        direction=PyDisplay.input(terminal,"What would you like to do?\n")

        if direction=="west":
            if "iron ore" and "wood" and "3 Gold Pieces" in items:
                PyDisplay.write(terminal, "the blacksmith greets you, and you tell him that you have the items and money he requires, you also give him the saw to make up for some of the difference, he then forges you a battleaxe and wishes you luck on the rest of your quest")
                items.remove ("saw")
                items.remove ("3 Gold Pieces")
                items.remove ("iron ore")
                items.remove ("wood")
                items.append ("battleaxe")
            else:
              PyDisplay.write(terminal, "You are at the blacksmith shop, many different kinds of weapons decorate the walls, the blacksmith is a tall, hairy man who smiles as you enter the door.  You tell him that you need a weapon to kill Kibbles the Evil Dragon. He laughs and says  'Mah boy! Killing Kibbles is what all true warriors strive for! But you can't do it with any of my weapons, you need Atari, the magic sword that lies in the cave east of the forest.  Many have tried to get it, but all have failed as it is guarded by the evil wizard Gwonam!  If you're looking to fight Gwonam, I can make you an axe, but you need to bring me iron ore, wood, and some gold for my troubles.'  You then decide to head into the forest to seek out the materials for the blacksmith")


        elif direction=="north":
            PyDisplay.write(terminal, "You walk up the gates of the king's castle, the guards stop and ask you to state your business, you tell them that you want to rescue Princess Catherine.  They laugh and tell you that you should probably obtain a weapon first, you head back to the center of town")

        elif direction=="east":
            PyDisplay.write(terminal, "You head into the residential district of town, a few huts line the streets, but there isn't much else of note here, you decide to head back to the center of town")

        elif direction=="south":
            PyDisplay.write(terminal, "You head deep into the forest")
            place=forest

        elif direction=="commands":
            PyDisplay.write(terminal, "type the commands north, south, east, and west in order to navigate,type the command use followed by the item name to use an item, type the command inventory to look at the items in your posession, type the command look to look around you, type the command search followed by a target to search for people or items, type the command commands to repeat these directions")

        elif direction=="inventory":
            PyDisplay.write(terminal, items)

        elif direction=="look":
            PyDisplay.write(terminal, "You're located in the small town of Jersey, here you see a blacksmith shop to the west, the king's castle to the north, houses to the east, and the town's exit to the forest to your south")

        elif "use" in direction:
            PyDisplay.write(terminal, "You have nothing to use")

        elif "search" in direction:
            PyDisplay.write(terminal, "There's nothing of importance to search")

        else:
            PyDisplay.write(terminal, "Please type a command")

    while place==forest:

        direction=PyDisplay.input(terminal,"What would you like to do?\n")

        if direction=="west":
            PyDisplay.write(terminal, "You head into the mine")
            place=mine

        elif direction=="south":
            PyDisplay.write(terminal, "The mountains look too treacherous to try and pass through.  It might not hurt to try and look for that man though.")

        elif direction=="east":
            if "battleaxe" in items:
                PyDisplay.write(terminal, "You head into Gwonam's Lair")
                place=lair
            else:
                PyDisplay.write(terminal, "it's not a good idea to go to Gwonam's lair unprotected")

        elif direction=="north":
            PyDisplay.write(terminal, "You head back to Jersey")
            place=town

        elif direction=="look":
            PyDisplay.write(terminal, "You are at the center of a vast forest, surrounded by many tall trees, you could defintiely obtain some wood from some of them, but you would need the proper tools.  To your west lies a mine, to your south a group of impassable mountains, but you can hear a person in the distance, to your east lies the evil gwonam's lair, and to the north lies Jersey")

        elif "use" in direction:
            if "pickaxe" in direction:
                if "iron ore" in items:
                    if "man" or "person" in directions:
                        PyDisplay.write(terminal, "The man looks at the pickaxe with sorrow and tells you that this was his brothers pickaxe, he offers you his saw for it and you accept.")
                        items.append ("saw")
                        items.remove ("pickaxe")

            if "saw" in direction:
                if "saw" in items:
                    if "tree" in direction:
                        PyDisplay.write(terminal, "you use the saw to cut some wood off of a nearby tree")
                        items.append ("wood")
                else:
                   PyDisplay.write(terminal, "you can't use that")


        elif direction=="inventory":
            PyDisplay.write(terminal, items)

        elif "search" in direction:
            if direction== "search person":
                PyDisplay.write(terminal, "You find the man, he appears to be a lumberjack and is carrying a large saw.  You tell him about your quest and the items you are looking for.  He directs you to the mine for the iron ore and tells you that he's always wanted to be a miner like his brother.  He tells you that his brother is in the mines right now if you should need any help.")
            else:
                PyDisplay.write(terminal, "You can't search for that")

        elif direction=="commands":
            PyDisplay.write(terminal, "type the commands north, south, east, and west in order to navigate,type the command use followed by the item name to use an item, type the command inventory to look at the items in your posession, type the command look to look around you, type the command search followed by a target to search for people or items, type the command commands to repeat these directions")


    while place==mine:

        direction=PyDisplay.input(terminal,"What would you like to do?\n")

        if direction=="west":
            PyDisplay.write(terminal, "The cavern is too dark to travel down, you head back to the center of the mine")

        elif direction=="east":
            PyDisplay.write(terminal, "You head back to the forest")
            place=forest

        elif direction=="south":
            PyDisplay.write(terminal, "You are at a desposit of rich iron, this is perfect for the blacksmith, the only problem is you don't have a way to mine it")

        elif direction=="north":
            PyDisplay.write(terminal, "You are in a small cavern with a dead body on the floor, you are not sure how he died.  You see a pickaxe underneath him and a bag around his waist")

        elif direction=="look":
            PyDisplay.write(terminal, "You find yourself in the center of a large mine.  To your east lies the exit back to the forest, to your north lies a cavern with a dead body, to your south lies an iron deposit, and to your west lies a very dark cavern.")

        elif "search" in direction:
            if "body" in direction:
                if "pickaxe" not in items:
                    PyDisplay.write(terminal, "You take the pickaxe and the bag which contained 3 Gold Pieces")
                    items.append ("pickaxe")
                    items.append ("3 Gold Pieces")
            else:
                PyDisplay.write(terminal, "You cannot search that")

        elif "use" in direction:
            if "pickaxe" in direction:
                if "pickaxe" in items:
                    if "iron" in direction:
                        PyDisplay.write(terminal, "You use the pickaxe to mine the iron ore")
                        items.append ("iron ore")
            else:
                PyDisplay.write(terminal, "You cannot use that")

        elif direction=="inventory":
           PyDisplay.write(terminal, items)

        elif direction=="commands":
            PyDisplay.write(terminal, "type the commands north, south, east, and west in order to navigate,type the command use followed by the item name to use an item, type the command inventory to look at the items in your posession, type the command look to look around you, type the command search followed by a target to search for people or items, type the command commands to repeat these directions")


    while place==lair:

        direction=PyDisplay.input(terminal,"What would you like to do?\n")

        if direction=="west":
            PyDisplay.write(terminal, "You head back into the forest")
            place==forest

        elif direction=="south":
            if "Atari" not in items:
                PyDisplay.write(terminal, "You see Gwoman's scary looking 'Faces of Evil' paintings hanging on the wall, you head down the corridor and come face to face with the evil wizard.  He screams 'Zreep!' and tries to attack you with a magic spell!")

                action=PyDisplay.input(terminal,"What will you do?")

                if "axe" in action:
                    PyDisplay.write(terminal, "You manage to dodge the spell, you lunge forward and cut off Gwonam's head.  He screams 'Squadalllllllllahhhhhhhhhhh' as he dies as if it's some sort of spell, but who cares, you did it!  Atari the magic sword hangs on the wall, it's finally yours now, time to fight Kibbles!")
                    items.append ("Atari")
                    PyDisplay.write(terminal, "You head back to the center of the lair")
                    place=lair

                else:
                    PyDisplay.write(terminal, "You get hit with the spell, you can hear Gwonam laughing as you feel immense pain and everything begins to fade to black.  You realize that this is it, and you do not feel bad about losing your own life, only that you could not save Princess Catherine.  Here is where your Adventure ends.")
                    game==lose
                    place=dead

        elif direction=="east":
            if "key" in items:
                PyDisplay.write(terminal, "You run down the long hallway and reach Princess Catherine's cell.")
                cell=1
                locked=2
                cell=locked
                while cell==locked:
                    action=PyDisplay.input(terminal,"What would you like to do?")
                    if "key" in action:
                        cell=1
                        PyDisplay.write(terminal, "You unlock the cell door and rescue the Princess!  She hugs you and thanks you for saving her.  You escort her back to Jersey and become a legendary hero!")
                        game=win
                        place=dead
            else:
                PyDisplay.write(terminal, "You head down a long hallway and reach a jail cell holding Princess Catherine, she screams to you for help, but the door is locked.  She tells you the key is in the room and guarded by Kibbles the Evil Dragon.  You promise her that you will save her and head back to the center of the lair")

        elif direction=="north":
            if "Atari" in items:
                PyDisplay.write(terminal, "You run down the hallway and reach a giant room.  You finally come face to face with the Evil Kibbles the Dragon.  He towers over you and begins to roar when you enter the room.  He charges at you and breathes fire in your direction")
                action=PyDisplay.input(terminal,"What will you do?")
                if "Atari" or "Sword" in action:
                    PyDisplay.write(terminal, "You unsheate Atari, it begins to glow and surrounds you with a protective shield.  The fire bounces off the shield and you swing it at Kibbles' neck, decapitating him.  You did it!  You pick up the key on the table that Kibbles' was guarding, now all that's left is to save the Princess!")
                    items.append ("key")
                else:
                    PyDisplay.write(terminal, "You get hit by the fire, it scorches your body and you feel excrutiating pain all over.  A deep sense of regret fills you as you realize that you were unable to save Princess Catheine.  Kibbles picks you up and beings to devour you whole.  Your adventure ends here.")
                    game=lose
                    place=dead

            else:
                PyDisplay.write(terminal, "You do not have Atari yet, it would be suicide to try and fight Kibbles")

        elif direction=="look":
            PyDisplay.write(terminal, "You are at the center of the evil Gwonam's lair.  To the north lies Kibbles the evil dragon, to the south lies Gwonam's room, to the east lies the jail, and to the west is the exit back to the forest.")

        elif "use" in direction:
            PyDisplay.write(terminal, "I would worry about using that now, let's just try and rescue the Princess")

        elif direction=="commands":
            PyDisplay.write(terminal, "type the commands north, south, east, and west in order to navigate,type the command use followed by the item name to use an item, type the command inventory to look at the items in your posession, type the command look to look around you, type the command search followed by a target to search for people or items, type the command commands to repeat these directions")

        elif direction=="inventory":
            PyDisplay.write(terminal, items)

        elif "search" in direction:
            PyDisplay.write(terminal, "Let's not search that now, we have to save the Princess!")

while game==lose:
    PyDisplay.write(terminal, "Thank you for playing, Try again and see if you can rescue Princess Catherine!")
    game=99
    PyDisplay.input(terminal,"Press enter to exit the program")

while game==win:
    PyDisplay.write(terminal, "Congratulations! You beat the Adventure game! Thank you for playing!")
    game=99
    PyDisplay.input(terminal,"Press enter to exit the program")

