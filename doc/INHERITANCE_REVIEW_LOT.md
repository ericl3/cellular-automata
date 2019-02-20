Team Discussion (dwl23, lot, lbj7)
===
# XML Formatting
We discussed what kind of information should be included in the XML file. We decided on:
* Simulation type
* Rows and Columns

# Part One
1. We are hiding Grid.Grid functionality from Nodes.Nodes class, Rules from the Animation.
2. Nodes.Nodes, Grid.Grid and Simulation all have hierarchies. Simulation sub-classes determine the rules. Grid.Grid sub-classes determine the shape and size of the grid. Nodes.Nodes sub-classes determine the location and shape of each Nodes.Nodes.
3. Open the different properties applicable to each sub-class which allows the different sub-classes to have very general properties.
4. We are going to set all the values that don't meet criteria to default values.
5. Our design is good because it is very straight-forward and readable.

# Part Two
1. The entire project uses these different classes from different areas. Grid.Grid, Nodes.Nodes, Simulation, Animation.
2. Yes, the different sub-classes implement methods differently.
3. We are going to separate things as much as possible
4. So far all of our classes are well designed. The Nodes.Nodes class for example shares "shape", "location", and "Color" properties.

# Part Three
1. Five Use Cases:
    * Developing rule sets
    * Creating and remaking games
    * Simulating game
    * Moving Nodes
    * Developing new games

2. Most excited to do different games logic
3. XML Parsing