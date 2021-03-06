Design Exercise
===
### 24 January 2019

Team members: 

Januario Carreiro (jjc70@duke.edu)

Anshu Dwibhashi (ad353@duke.edu)

Jonathan Yu (jy178@duke.edu)

### Exercise 1: RPS
We can have one superclass called _Destroyer_ that has generic functions that are common to all destroyers.
These include: _attack()_ which returns a boolean value corresponding to whether or not the attack was successful,
_toString()_ which desribes what kind of a destroyer this is etc.

Then we'll have 3 different classes that inherit from the _Destroyer_ class:
_Rock_, _Paper_, _Scissors_. Each of these classes will override the attack() method
and return true or false based on the following rules:

- Rock attacks paper: false
- Rock attacks scissors: true
- Rock attacks rock: true (tie breakers will be decided by the _Supervisor_ class)
- Paper attacks rock: true
- Paper attacks scissors: false
- Paper attacks paper: true
- Scissors attacks paper: true
- Scissors attacks rock: false
- Scissors attacks scissors: true 

To decide winners, losers, and settle tiebreakers, there will be the main _Supervisor_ class
that contains instances of rock, paper and scissors, and monitors all attacks.

### Exercise 3: utils.Cell Society

**How does a utils.Cell know what rules to apply for its simulation?**

Each simulation (game of life, segregation, predator-prey, ...) has its own config file that holds
the rules for that specific simulation. Then, our primary class can get 
the rules from the each config file..

**How does a utils.Cell know about its neighbors? How can it update itself without effecting its neighbors update?**

The _Simulation_ superclass contains a grid of all the cells, which are represented by objects of 
the _Cell_ class. Each cell class can call the _getCellInfo()_ method that will be provided by
the _Simulation_ class, as a method that returns info on cells at the requested index. To avoid affecting its
neighbor's update, each cell object would store its un-updated state, allowing other cells to access it.

**What is the grid? Does it have any behaviors? Who needs to know about it?**

The grid itself does not have any behaviors, it is just a 2-d array of utils.Cell objects. Only the simulation
and visualization classes know what's inside the grid.

**What information about a simulation needs to be the configuration file?**

We'll have all the rules for each different simulations in different config files.
Then, depending on the simulation we want, we can just read a different config file.
This includes frequency of updates and the action done with each update.

**How is the GUI updated after all the cells have been updated?**

The simulation class will call the GUI class to update. The GUI doesn't know any rules
about the simulation. It only contains things that the user will see and has a visual for
the current state of the simulation.

**Basic Map of Project**

Classes: simulation.Simulation, Visualization, utils.Cell

utils.Cell
* One class for each game (extending abstract superclass)
* Holds rules
* Holds current and future (calculated) state

simulation.Simulation- handles grid, acts as "main" class (runs configuration and visualization)
* Handles grid
* "main class" (calls configuration and visualization)
* iterates through grid once to calculate future states of each cell
    * beforehand, assigns "previous" future value to current
* calls each cell's update function and passes in its neighbors
* gives the most information needed among games (better abstraction)