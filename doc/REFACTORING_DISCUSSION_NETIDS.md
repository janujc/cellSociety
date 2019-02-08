#Static Code Review and Refactoring
##Duplication Reading
* CardGridGenerator
    * 3 repeated method call chains
        * Refactored the duplicate parts of these calls into a new helper method
            * Reduced duplication and made code more readable

No other duplicate code was found
##Checklist Refactoring and General Refactoring
* Controller class
    * Restricted access to instance variables where possible
        * Good practice to do so
    * Made constant static
        * Good practice, no reason not to
* Percolation class
    * isSimComplete() was too complex (too many nested if statements)
        * Removed the method entirely and moved checking for sim completion to the calculation method
            * Reduced complexity and saved an iteration through the grid
* Segregation class
    * Added curly braces to one line if statements
        * Good practice
* HomeScreen class
    * Removed unused imports
    * Removed unnecessary parentheses around parameter in lambda notation
* PredatorPrey
    * Made constants static
        * Good practice, no reason not to
    * Refactored initializeTrackers() to have less nested if/for statements
        * More readable
* ConfigParser class
    * Originally declared a local variable and immediately returned it
        * Just returned the expression
            * Good practice, no reason to create the local variable in the first place
* Fire class
    * Made constants static
        * Good practice, not reason not to
* Simulation class
    * Refactored populateGrid() into two methods
        * Avoids having too many nested if/for statements
* SimulationScreen class
    * Moved constructor above methods
        * Complies with standards
##Longest Method Refactoring
* Fire class
    * Refactored calculateNextStateOfCurrCell into two methods
        * Placed calculations for tree cell in its own method
            * Better logical division, shorter methods, more readable