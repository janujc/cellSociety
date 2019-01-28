Design Plan
===
28 January 2019

#### Team members:

Januario Carreiro (jjc70@duke.edu)

Anshu Dwibhashi (ad353@duke.edu)

Jonathan Yu (jy178@duke.edu)

### Introduction
The problem we're trying to solve by writing this program is that of building a program to efficiently simulate various natural or abstract phenomena. We need to be able to build a program that is abstract enough to serve as a simulation engine for whatever phenomenon we wish to simulate, by reading in rules for whatever phenomenon it might be, and executing them. The primary design goal is to provide a generic simulation class that can be used by developers to build more concrete simulation classes that inherit from this main simulation class, so that new simulations can be added easily without having to touch the rest of the program.

The program, will be architected in a way that the main driver class simply reads an XML Manifest file (borrowed the idea from Android manifest) to see what classes are available to show as simulations, and then show a selector for users to pick a simulation. Each simulation is isolated from all other simulations, and the driver class. The class that's intended to show visualizations and update the UI simply obtains instructions to show changes in the UI, but is otherwise isolated completely from the simulations.

### Overview

Classes: `Driver`, `Simulation`, `Visualization`, `Cell`

The `Driver` class will be the main entry point to the program, where the user picks a simulation to be run. The `Simulation` class is an abstract class that will be inherited by all the individual, specific, concrete simulations, and contains methods to run the simulation. The simulation contains and updates a grid of `Cell` objects, one step at a time. Each of these objects will store a state and its location within the grid. The `Visualization` class includes methods to take information from the `Simulation` class regarding what has to be rendered and where, and then do the rendering process.

![CellSocietyUML](https://i.imgur.com/Akrvfci.png)

### User Interface
The user will interact with the program through two avenues: the main driver class and the visualization class. Before the user has picked a simulation, the driver class will display all available simulations, respond to user inputs in general, help on how to use the program, and act when the user chooses a certain simulation to be displayed. Once the user picks a certain simulation to be displayed, all of its outputs will be displayed by the visualization class, and all of the user inputs will be handled by that class and relayed to the simulation class.

The main driver class will have a grid like structure showing the user icons for each simulation that will respond to clicks by starting that particular simulation. The simulation will just be a grid of cells that collectively display the simulation. There won't be any UI elements in the simulation class in order to make it abstract enough for other simulation classes to be easily created. Any initial condition information will be present in the Manifest file. Any errors will be displayed to the user using a design element known as a snackbar (borrowed from Android UI principles).

![Main Driver](https://i.imgur.com/tvMisdr.png)
![Simulation](https://i.imgur.com/IrLEbCo.png)

### Design Details 
The following are the descriptions for the components introduced in the Overview section:

`Driver`:
* Main driver class that serves as the entry point to the program.
* Shows an app-store-like display of various simulations that may be selected by the user to be played.
* Creates the Simulation and Visualization instances, then gives them control to run and display the simulation

`Simulation`:
* Creates and updates grid (step())
* Subclasses of this abstract class will represent specific simulations with their own states and rules
    * Future developers can easily create their own subclass by understanding the parent Simulation class
* Iterates through grid to **calculate** future states of each cell first, updates cells after (allowing for lock-step synchronization)
* To update cells, calls each Cell object's updateState() method

`Cell`:
* Generic cell class that is placed into the grid and controlled by the Simulation subclasses
* Holds a state and its location within the grid
    * Location allows Simulation to finds its neighbors
* Has method to update state and give public access to the cell's location

`Visualization`:
* Serves as an interface for the rest of the program to access the GUI.
* Has no idea what's going on in the simulation; takes in input from the `Simulation` class and displays it as instructed.
* We can add a delay between steps of the simulation depending on what the user wants the speed to be

We also have subclasses of simulation—`GameOfLife`, `Segregation`, `PredatorPrey`, `Fire`, `Percolation`—that extend the abstract simulation class and have their own private methods required to implement the different simulations with their unique states and rules.

####Use Cases
* Case 1: Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)
    * Get all 8 of the cell's neighbors by calling the Simulation classes' method
    * Count the number of living neighbors
    * Based on the rules for the number counted, calculate the cell's next state (will not be updated until after all cells have been processed)
* Case 2: Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)
    * Get only 6 of the cell's neighbors by calling the Simulation classes' method
        * The method in Simulation should have logic to not access cells that are not in the grid, thus returning only 6 neighbor cells)
    * Follow Steps 2 and 3 from Case 1
* Case 3: Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically
    * After calculating the next state of each cell (without making any changes), iterate through the grid and set each cell's state to the next state
        * Alternatively, could store calculated next states in a second grid during the first iteration
        * Would simply have to reassign grid to update
* Case 4: Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based on the value given in an XML fire
    * Upon the user selecting the Fire simulation, the Driver class will read the XML file to get the probCatch value
    * Then, the Driver class will create a new instance of the Fire class, passing in that read value
    * From there, the Fire object will assign that value to its corresponding attribute
* Case 5: Switch simulations: use the GUI to change the current simulation from Game of Life to Wator
    * Upon receiving the appropriate user input for changing during a simulation, stop the current Game of Life simulation
    * Then, create a new instance of the PredatorPrey class (Wator) and load it into the Visualizer
    * From there, wait for the correct user input to start the new simulation

### Design Considerations
The primary design considerations that our team needs to undertake include decisions regarding how abstractions and encapsulations are implemented. <p> For example, our decisions on whether the visualization class will be in charge of controlling the rate of simulation, or the simulation class, will change which of those classes 'knows' what information about the other class. This affects how we choose to encapsulate various details. We had lengthy discussions in our group to determine which classes have which responsibilities. The pros of the visualization class being completely blinded from all simulation aspects and only rendering what it's asked to render by the simulation class are that it implements abstraction quite well and becomes modular. However, the cons outweigh the pros, in that the simulation class should actually be isolated from the rate at which the simulation occurs, and the visualization class should call the simulation class at that frequency. This is our current agreed-upon implementation.
### Team Responsibilities
Anshu will take primary responisibility of the `Driver` and `Visualization` classes. Jonathan will take
primary responsibility of `Simulation`, `PredatorPrey`, and `Fire` classes. Januario will take primary
responsibility of the `Cell`, `Segregation`, `GameOfLife`, and `Percolation` classes.

Furthermore, we have divided out secondary responsibilities by having a second person "back up" each of these sections of the project. Anshu will help with Januario's parts, Januario will help Jonathan, and Jonathan will help Anshu.

The plan is to work on all of the classes simultaneously, and to be consistent on how they interact with one another. Constant, clear communication is necessary, so that we can all understand the interaction between our classes. This way we should be able to develop the programs on our own and be able to integrate them somewhat seamlessly.