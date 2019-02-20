Cell Society : Inheritance Review Questions

Part 1
1. Our grid class is encapsulated, it cannot be directly added to without working with the getter and setter methods that it uses.
2. We are intending to build an inheritance hierarchy for the simulator, grid, and nodes classes. The simulator classes will have subclasses for each game, running the rules and interactions for each game.
3. We are trying to make the animation open to outside XML input, but everything else should be closed.
4. I might get concurrent modification errors for trying to remove elements from a loop I am looping through, so I will have to use a secondary removal loop to remove elements.
5. My design is good because it is quite modular, the animation only sees the grid and the simulator only sees the grid, and neither interacts at all.

Part 2
1. My area is linked to the rest of the project because the rest of the project uses my animation and visualization to display the backend of the project. It is very important.
2. The dependencies are based on the class's behavior, as it must use all the other classes to animate no matter what.
3. We can minimize these dependencies by properly placing functions that belong with each class in the correct location, instead of passing in unnecessary variables.
4. The simulator/Spreading fire class/sub class is what we went over. Basically, we can have the simulator call all the common aspects of the class, while spreading fire will call the aspects specific to spreading fire.

Part 3
1. We came up with 5 use cases - such as simulating games and cellular automata.
2. I am most excited to work on the animation of the class - I love doing frontend work like this, and had fun doing this with breakout.
3. I am most worried about working with a team - I know my ideas are good, and I could implement the whole project well on my own, but implementing with others will be a challenge as I am not responsible for the whole project.
