1. The implementation of different grids is hidden away from the other parts of the code. 

2. We are building hierarchies such as a Grid.Grid super class with different grids sub classes
that extend the overall grid structure and methods. Another example would be a Nodes.Nodes super 
class with multiple node subclasses.

3. Trying to make the grid interface open such that it is able to be modified and extended,
and call an interface that is same across the multiple subclasses. Closed would be Simulations,
to some extent, because although they extend a simulation class, each simulation is extraordinarily
unique. 

5. I think our design successfully splits our code into meaningful and individual parts that
can be extended or added on to. 

6. I am most excited to work on the different simulations and the logic behind the 
simulation.

7. I'm most worried about figuring out XML parsing and how to get used to this new format
of input initialization.