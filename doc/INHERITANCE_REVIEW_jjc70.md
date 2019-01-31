Inheritance Review
===

author: Januario Carreiro (jjc70)

## Part 1

**What is an implementation decision that your design is encapsulating (i.e., hiding) from other areas of the program?**

Our `Simulation` subclasses are hiding how the next state is calculated from the other classes in the program. The
different subclasses implement how the next state is calculated in multiple different ways, and the other classes of
the program don't need to know that.

**What inheritance hierarchies are you intending to build within your area and what behavior are they based around?**

The main inheritance hierarchy is the Simulation. There is a `Simulation` superclass and multiple subclasses with names
such as `GameOfLife` and `Segregation`.

**What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism 
you are creating?**

There are not many parts within the simulation that are closed. The major closed portions of this project are the
config and resources files.

**What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?**

The main errors that we are running into are how to calculate the next state/position of cells in the `Segregation` and
`PredatorPrey` subclasses, because the cells can move to a new position with every step of the simulation, and we
have to make sure that no two cells move to the same grid location.

**Why do you think your design is good (also define what your measure of good is)?**

I think that our design is good because each class has a specific function within the whole program. In other words,
the cell knows all it has to know and function as a cell, the simulation only simulates each step, and it is the
visualization class's sole job to show the current state of the simulation. The classes do not do more than is needed.
This makes everything easier to debug and keeps the project well-organized, and I believe that is good design.