# Simulation
Sachal Dhillon (ssd27)
Jonathan Yu (jy178)

## Part 1

### What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?

JY: Our designs encapsulates the functionality to actually update the grid.
Comments from SD: Maybe hide exact data structure of the grid

SD: My design uses encapsulation to hide the particular rules for any one game. Instead, it offers a general Grid to the front-end so that game-specific optimizations do not have to be made. 
### What inheritance hierarchies are you intending to build within your area and what behavior are they based around?

JY: Simulation super class with simulation-specific subclasses. They are based around the specific, divergent rulesets for each of the simulations.
Comments from SD: Potentially implement a Grid hierarchy for different-shaped grids.

SD: I have seperate inheritance hierchacies for each of the following major components: The Game, the Cell, and the Grid. For each of them, I have an abstract parent class that defines the core behavior and then a descending series of children that add on game-specific utilities.

### What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?

JY: Closed- specific implementation of rulesets; Open- access to the grid for visualizing (might change)

SD: I am making most of the underlying behavior in each of the cell and grid hierchies hidden. I use collections parameterized by Cell, Grid, and Game classes to enclose lists and collections of each of their various children. 


### What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?

JY: invalid grid side size (handle when reading xml file), population frequencies don't add to 100% (handle when reading xml file)

SD: An example of an exception would be somebody passing in a state for a cell that isn't recognized as part of its respective game. 


### Why do you think your design is good (also define what your measure of good is)?

JY: I think my design is good because it allows simple implementation of new simulation subclasses with relatively few requirements for functionality.

SD: I think my design is good because it allows me to concisely create new games with very disparate designs or mechanisms without having to significantly alter my codebase.

## Part 2

### How is your area linked to/dependent on other areas of the project?

JY: Dependent on configuration for reading the xml file, user input for population breakdown

SD: My portion is dependent on the data controller for reading in and saving XML data as well as the front-end to pass events and make requests on behalf of the user.

### Are these dependencies based on the other class's behavior or implementation?

JY: No it just needs certain data values

SD: For the most part, no. My design was to essentially implement an API that can be called by other portions of code. So long as the input matches the specified inputs in my gateway classes, my design is agnostic to the rest of their code. 

### How can you minimize these dependencies?

JY: I think these dependencies are minimal by only needing data values that are already set.

SD: I do not believe that tare any major dependencies outside of event handling. 

### Go over one pair of super/sub classes in detail to see if there is room for improvement. 

JY: Simulation superclass with Fire subclass. Superclass has base simulation behavior. Fire implements the specific rules.

SD: I have a Game class to control base behavior and a GameOfLife subclass implement game-specific behavior such as what happens on every step. 


## Part 3

### Come up with at least five use cases for your part (most likely these will be useful for both teams).

JY:
* Have a shark choose from 2 neighboring fish to eat in PredatorPrey
* Have a burning tree spread the fire to 4 neighboring trees
* Populate the grid with an equal split amongst states
* Have a fish breed into a cell when it has 2 empty neighbors
* Have a fire burn out without spreading when it has no neighboring trees

SD:
* Handles the back-end abstraction for the game and its underlying mechanics
* Defines data calls for event handlers on the front-end
* Communicates with a data-controller for load/save behavior
* Hides unnecessary information from the front-end
* Defines the logicical inheritanc hierarchy for each component 

### What feature/design problem are you most excited to work on?

JY: Same as SD

SD: I am most interested in creating different types of grids for different cell shapes
### What feature/design problem are you most worried about working on?

JY: I am worried about how to implement the exact rules of PredatorPrey

SD: I am most worried about updating the grid when more complicated behavior for each game is introduced. 
