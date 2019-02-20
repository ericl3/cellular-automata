cell society
====

This project implements a cellular automata simulator.

We are going to have 4 main classes, a Nodes.Nodes superclass, a simulator superclass, a GUI class, and a grid class. The Nodes.Nodes superclass will have children classes for each type of block, setting the block types and colors. The node superclass itself will have all of the general node action functionality, such as dying, moving, checking the validity of moves, and swapping. The simulator superclass will determine the type of game being played. The children class of the simulator class will determine general game interactions (burning of trees, sharks and fish dying, etc.) as well as store the specific instance variables of the game. The GUI class will control all graphical aspects of the game, and will update by looking at the grid class. The grid class will organize the location of all blocks, and act as individual frames to be updated from the gui.