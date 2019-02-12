cell society
====

This project implements a cellular automata simulator.

Names: Jonathan Yu, Anshu Dwibhashi, Januario Carreiro

### Timeline

Start Date: 1/24/19

Finish Date: 2/12/19

Hours Spent: 150

### Primary Roles

Anshu- Frontend, GUI, Configuration (parsing)
Jonathan- Backend, Simulation, Fire, PredatorPrey, RockPaperScissors, Grid
Januario- Backend, Cell, Percolation, Segregation, GameOfLife, Grid, Triangular, Hexagonal

### Resources Used
Java Standard Library and API

### Running the Program

Main class: Controller

Data files needed: Fonts and images in /res, config xml files in /data

Interesting data files: PredatorPrey default config

Features implemented:
* GUI
    * Grid visualization
    * Main menu
    * Ability to load different config files
    * Save/load states
    * Play/pause button
    * Different simulation speeds
    * Error checking
    * Different initial configurations
    * Different grid stylings
    * Population graphs
    * Multiple simulation windows
    * Ability to interact with simulation dynamically (through mouse clicks)
* Backend
    * Grid class that is responsible for the ordering of the cells
        * Has different orderings/subclasses for square, triangular, and hexagonal grids
        * Finite and toroidal grid edges
    * Simulation class that is responsible for running the simulation and modifying cells
    * Cell class that holds current and next state
    * Different specific simulations: Fire, PredatorPrey, RockPaperScissors, Percolation, Segregation, GameOfLife
    * Relatively simple process for adding new simulations
    
Assumptions or Simplifications:
* PredatorPrey is not lock-step as allowing animals to react to each other's behavior provides the most realistic simulation.
* Hexagonal and Triangular grids are not "squares" for simpler visualization

Known Bugs:
* Hexagonal and triangular grids do not display properly at large grid sizes (ex: 100)
Extra credit:


### Notes
We're particularly proud of the quality and aesthetics of the GUI. Also, we're proud of the backend design. The Simulation
class can't access the Grid data structure, and the Grid class has no access to the simulation states/rules. Furthermore,
the visualization has very limited access to both the Simulation and Grid classes.

### Impressions
The project took quite a bit of work and, more importantly, communication.
