Design Plan
===
27 January 2019

#### Team members:

Januario Carreiro (jjc70@duke.edu)

Anshu Dwibhashi (ad353@duke.edu)

Jonathan Yu (jy178@duke.edu)

### Introduction
The problem we're trying to solve by writing this program is that of building a program to efficiently simulate various natural or abstract phenomena. We need to be able to build a program that is abstract enough to serve as a simulation engine for whatever phenomenon we wish to simulate, by reading in rules for whatever phenomenon it might be, and execute them. The primary design goal is to provide a generic simulation class that can be used by developers to build more concrete simulation classes that inherit from this main simulation class, so that new simulations can be added easily without having to touch the rest of the program.

The program, will be architected in a way that the main driver class simply reads an XML Manifest file (borrowed the idea from Android manifest) to see what classes are available to show as simulations, and then show a selector for users to pick a simulation. Each simulation is isolated from all other simulations, and the driver class. The class that's intended to show visualizations and update the UI simply obtains instructions to show changes in the UI, but is otherwise isolated completely from the simulations.

### Overview

Classes: `Driver`, `Simulation`, `Visualization`, `Cell`

The `Driver` class will be the main entry point to the program, where the user picks a simulation to be run. The `Simulation` class is an abstract class that will be inherited by all the individual, specific, concrete simulations, and contains methods to run the simulation. The simulation contains and maintains a grid of `Cell` objects. The `Visualization` class includes methods to take information from the `Simulation` class regarding what has to be rendered and where, and then do the rendering process. The `Cell` class contains display information about a single cell, as dictated by the `Simulation` class.

![CellSocietyUML](https://i.imgur.com/0H3oSOe.png)

### User Interface
The user will interact with the program through two avenues: the main driver class and the visualization class. Before the user has picked a simulation, the driver class will display all available simulations, and respond to user inputs on general help on how to use the program, or when the user chooses a certain simulation to be displayed. Once the user picks a certain simulation to be displayed, all of its outputs will be displayed by the visualization class, and all of the user inputs will be handled by that class and relayed to the simulation class.

The main driver class will have a grid like structure showing the user icons for each simulation that will respond to clicks by starting that particular simulation. The simulation will just be a grid of cells that collectively display the simulation. There won't be any UI elements in the simulation class in order to make it abstract enough for other simulation classes to be easily created. Any initial condition information will be present in the Manifest file. Any errors will be displayed to the user using a design element known as a snackbar (borrowed from Android UI principles).

![Main Driver](https://i.imgur.com/tvMisdr.png)
![Simulation](https://i.imgur.com/IrLEbCo.png)

### Design Details 
The following are the descriptions for the components introduced in the Overview section:

`Driver`:
* Main driver class that serves as the entry point to the program.
* Shows an app-store-like display of various simulations that may be selected by the user to be played.
* Then passes control to the main `Simulation` that is being played.

`Simulation`:
* Handles grid, acts as "main" class (runs configuration and visualization).
* Main class for a specific simulation (calls configuration and visualization).
* Iterates through grid once to calculate future states of each cell beforehand, assigns "previous" future value to current.
* Calls each cell's update function and passes in its neighbors and/or other relevant information such as user-inputs.
* Gives the information needed for simulation to cell in an abstract manner. 
* This class itself will be a generic, potentially-abstract parent class that will be inherited by each new Simulation child class that developers choose to build.

`Cell`:
* Generic cell class that is populated and controlled by the `Simulation` child-class.
* Renders information as dictated by a `Simulation` child-class.
* Holds past and current state.


`Visualization`:
* Serves as an interface for the rest of the program to access the GUI.
* Has no idea what's going on in the simulation; takes in input from the `Simulation` class and displays it as instructed.
* We can add a delay between steps of the simulation depending on what the user wants the speed to be

We also have subclasses of sumulation—`GameOfLife`, `Segregation`, `PredatorPrey`, `Fire`, `Percolation`—that extend the abstract simulation class and have their own private methods required to implement the different simulations.

### Design Considerations
The primary design considerations that our team needs to undertake include decions regarding how abstractions and encapsulations are implemented. For example, our decisions on whether the visualization class will be in charge of controlling the rate of simulation, or the simulation class, will change which of those classes 'knows' what information about the other class. This affects how we choose to encapsulate various details. We had lengthy discussions in our group to determine which classes have which responsibilities. The pros of the visualization class being completely blinded from all simulation aspects and only rendering what it's asked to render by the simulation class are that it implements abstraction quite well and becomes modular. However, the cons outweigh the pros, in that the simulation class should actually be isolated from the rate at which the simulation occurs, and the visualization class should call the simulation class at that frequency.
### Team Responsibilities
Anshu will take primary responisibility of the `Driver` and `Visualization` classes. Jonathan will take
primary responsibility of `Simulation`, `PredatorPrey`, and `Fire` classes. Januario will take primary
responsibility of the `Cell`, `Segregation`, `GameOfLife`, and `Percolation` classes.

The plan is to work on all of the classes simultaneously, and to be consistent on how they interact with one another.