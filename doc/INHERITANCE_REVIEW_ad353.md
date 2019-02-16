# Simulation
Anshu Dwibhashi (ad353)
Makeup Labwork

## Part 1

### What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?

AD: The simulation rules are completely isolated from the visualisation controller classes, and the grid classes are completely isolated from the simulation classes. There's a heirarchy established, wherein contained classes completely isolate their functionality from container classes, and are unaware of the existance of the container classes. Our heirarchy is as follows: `Grid `is contained by `Simulation` is contained by `SimulationScreen` / `SimulationShell`

### What inheritance hierarchies are you intending to build within your area and what behavior are they based around?

AD: Our simulation rules are declared in a generic, abstract `Simulation` class that that are inherited and actually implemented in the various child classes for each specific simulation. We also have a generic `Grid` class that defines how a generic grid works that is inherited and implemented by the specific grids (square, hexagonal, triangular). In our visualisation package, our generic `Simulation` class is inherited by `SimulationScreen` and `SimulationShell` classes.

### What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?

AD: Closed- how the simulation shells and screens implement visualisation. Open- the ability to control the simulation from methods called by control objects, outside of the `visualization.Simulation` classes.

### What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?

AD: Invalid configuration files specified, Logical inconsistencids (negative probabilities, probabilities that don't add up to 1, etc).


### Why do you think your design is good (also define what your measure of good is)?

AD: I think my design is good because the heirarchy that has been established allows for seamless addition of new simulations, new type of visalualisation screens (as opposed to just `SimulationShells` and `SimulationScreens`) etc.

## Part 2

### How is your area linked to/dependent on other areas of the project?

AD: The visualisation classes contain the simulation classes' instances and depend on their correct execution. The simulation classes contain the grid classes' instances and depend on their correct execution. The grid classes are initialised and populated by the parser class (which is called ultimately by the visualisation classes) depend on the configuration files being written properly adhering to the agreed upon schema.

### Are these dependencies based on the other class's behavior or implementation?

AD: No. Due to the the nature of abstraction, as long as the dependencies are correctly doing what they want to do, the classes don't depend on each other's implementation.

### How can you minimize these dependencies?

AD: These dependencies are already minimal, and by implementing abstraction, all dependencies get to do whatever they want as long as they're abiding by the contract with other dependencies.

### Go over one pair of super/sub classes in detail to see if there is room for improvement. 

AD: `visualisation.Simulation` defines a generic UI to display a simulation. `SimulationScreen` inherits from that and is a `visualisation.Simulation` that has controls instead of being a simple shell like `SimulationShell`.

## Part 3

### Come up with at least five use cases for your part (most likely these will be useful for both teams).

AD:
* Be able to define new simulations, and specify them in `sim_list.xml` so the UI can update itself automatically with no new code.
* Compare any kind of simulation with any other kind of simulation.
* Open up configuration files of the wrong type of simulation and still be able to run them, by having the GUI realise that a different simulation has been loaded, and update itself.
* Change parameters of the simulation on the fly irrespective of the initial configuration.
* Save current state of the grid in a configuration file to use as a new initial configuration.


### What feature/design problem are you most excited to work on?

AD: Being able to compare different simulations (same type or not) in separate windows, and synchronise their executions.

### What feature/design problem are you most worried about working on?

AD: Nothing in particular.