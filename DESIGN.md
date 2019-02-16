Design
===

#### Team members:

Anshu Dwibhashi (ad353@duke.edu)

Januario Carreiro (jjc70@duke.edu)

Jonathan Yu (jy178@duke.edu)

## Overview of Design
Our project has a generic `Simulation` class that each new simulation 
inherits from and defines specific rules for each simulation in.

The grid that is contained by these simulations is also defined by a generic
`Grid` class that is inherited by three different types of grids that we've built:
`Square`, `Hexagonal`, `Triangular`. 

The UI of the app is controlled by a main `Controller` class
that creates and loads various screens including `HomeScreen` and 
`SimulationScreen`. Both the `SimulationScreen` and `SimulationShell` classes
inherit from `visualization.Simulation` which defines a generic Simulation visualiser.

The Controller class reads a standard file known as `sim_list.xml` which defines
all available simulations that the program can load. This way, adding new simulations
requires absolutely no code to be changed apart from defining a new
simulation and adding an entry in `sim_list.xml` corresponding to that
new simulation so the controller knows that it exists.

## Design Goals

We had the following high level design goals for our project:

* Be able to add new simulations with absolutely no changes to
the other parts of our program.
* Define generic types of simulation configurations in XML files and be able
to load all of them with generic code.
* Separate visualisation code from grid maintenance code and simulation code.
* Be able to compare simulations of all kind.
* Be able to load all kinds of configurations in all kinds of simulations.

## Adding new features
* To add new simulations:
    * Define your new simulation in its own simulation class that extends `simulation.Simulation`
    * Add an entry in `sim_list.xml` to let the controller know of its existence.
    * Define a default configuration file in `data/your_new_simulation/default.xml`
    * Done!
* To add new types of grids:
    * Create a class implementing `grid.Grid`
    * Implement all required methods
    * Done!
* To add other new features:
    * Extend `visualization.Simulation` or `simulation.Simulation` and make your new features! 

## Major design choice-justifications
* The Visualization part is separated from Simulation
    * Only can create Simulation instances and access through limited getter methods
    * Allows for proper OOP and encapsulation
    * Visualization does not need to know that much about how the simulations work
        * Just needs the result in order to display it
* Configuration is only called/accessed by Visualization
    * Visualization passes the appropriate data read from the config files to Simulation
    * Allows for simpler instantiation of simulations
        * Only need to create simulation objects once (in Visualization)
* The Grid object is initialized separately from the Simulation instance and is instead passed to the Simulation object
    * This simplified the constructor signatures for the Simulation class
    * Additionally, this makes sense logically as the grid is generic and can be used by any simulation

## Assumptions:
* All cells have the same shape
* Grid-universe is finite
