Design Exercise
===
## 24th January, 2019

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