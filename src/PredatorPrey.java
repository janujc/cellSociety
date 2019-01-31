import java.util.*;

// TODO Should I use Cell objects (current) or Fish/Shark objects?
/**
 * Class that represents the Predator-Prey simulation
 * <p>
 * States: empty (0), fish (1), shark (2)
 *
 * @author Jonathan Yu
 */
public class PredatorPrey extends Simulation {

    /**
     * If a fish or shark survives for this number of turns, it will breed.
     */
    private final int NUM_TURNS_TO_BREED;

    /**
     * The empty state is the only state that needs to a be an instance variable.
     */
    private final int EMPTY = 0;


    /**
     * Tracks the number of turns each fish and shark has survived since being born or breeding.
     * <p>
     * Key is the cell that currently holds the fish (prior to step). Values are the number of turns survived.
     */
    private Map<Cell, Integer> animalTurnTracker;

    /**
     * Creates the simulation and calls the super constructor to create the grid
     * @param sideSize the length of one side of the grid
     * @param populationFreqs the population frequencies of the states (not exact percentages)
     * @param numTurnsToBreed number of turns survived needed to breed
     */
    public PredatorPrey(int sideSize, double[] populationFreqs, int numTurnsToBreed) {
        super(sideSize, new int[]{0, 1, 2}, populationFreqs);    // hard-coded b/c states are pre-determined
        NUM_TURNS_TO_BREED = numTurnsToBreed;
        initializeAnimalTurnTracker();
    }

    /**
     * Finds all of the fish and sharks in the initial grid and adds them to the turn tracker
     */
    private void initializeAnimalTurnTracker() {
        animalTurnTracker = new HashMap<>();
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                if (cell.getCurrState() != EMPTY) {
                    animalTurnTracker.put(cell, 0);    // grid population doesn't count as a turn
                }
            }
        }
    }

    // TODO Is it okay that eating, moving, and breeding don't follow lock-step synchronization?
    // TODO Should for-each loop in calculateNextStates() be in separate method? Pro of that design is shorter methods. Con is need instance variables (toEat, toMove).
    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules
     */
    protected void calculateNextStates() {
        final int FISH = 1;

        // keys are sharks that will eat, values are fish to be eaten (all represented by their current cells)
        Map<Cell, Cell> toEat = new HashMap<>();

        // list of animals that will move (all represented by their current cells)
        List<Cell> toMove = new ArrayList<>();

        Random rand = new Random();

        // determine type of behavior for each cell (move or eat)
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {

                // TODO This might be overwritten when animals move/breed. Can this be avoided?
                // empty cells stay empty (unless they are moved/bred into)
                if (cell.getCurrState() == EMPTY) {
                    cell.setNextState(EMPTY);
                }

                // fish attempt to move every turn
                else if (cell.getCurrState() == FISH) {
                    toMove.add(cell);
                }

                // shark will randomly choose a fish in a cardinal neighbor cell to eat, will move if none exist
                else {

                    // fish that the shark can eat
                    List<Cell> fishEdible = getNeighborsOfType(cell, FISH, true);
                    if (!fishEdible.isEmpty()) {

                        // randomly choose from fishEdible
                        Cell randomFish = fishEdible.get(rand.nextInt(fishEdible.size()));
                        toEat.put(cell, randomFish);
                    } else {
                        toMove.add(cell);
                    }
                }
            }
        }

        // perform animal behaviors
        makeSharksEat(toEat, toMove);
        moveAllAnimals(toMove);
        breedAnimals();
    }

    // TODO What to do if two sharks try to eat the same fish? Currently, one shark just eats the other.
    /**
     * Make all sharks that will eat eat
     * @param toEat the map of all eating that will occur, where the key is the shark that will eat and value is the
     *              fish being eaten.
     * @param toMove the list of all animals that are suppose to move (used to acknowledge the fact that eaten fish no
     *               longer move)
     */
    private void makeSharksEat(Map<Cell, Cell> toEat, List<Cell> toMove) {
        for (Map.Entry<Cell, Cell> eat : toEat.entrySet()) {
            Cell eater = eat.getKey();
            Cell eaten = eat.getValue();

            // remove the eaten fish from the simulation data
            toMove.remove(eaten);
            animalTurnTracker.remove(eaten);

            moveAnimal(eater, eaten);
        }
    }

    // TODO Look at other ways to handle move conflicts. Right now, moves aren't parallel.
    /**
     * Move all animals that are supposed to move (sharks that don't eat, fish that aren't eaten and have empty cardinal
     * neighbor cells)
     * @param toMove the list of all animals that are supposed to move
     */
    private void moveAllAnimals(List<Cell> toMove) {
        Random rand = new Random();

        for (Cell mover : toMove) {
            List<Cell> canMoveTo = getNeighborsOfType(mover, EMPTY, true);

            // if no empty cardinal neighbor cells, don't move
            if (canMoveTo.isEmpty()) {
                mover.setNextState(mover.getCurrState());
            }

            // otherwise, randomly choose an empty cardinal neighbor cell to move to
            else {
                Cell willMoveTo = canMoveTo.get(rand.nextInt(canMoveTo.size()));
                moveAnimal(mover, willMoveTo);
            }
        }
    }

    /**
     * Moves animal from one cell to another and updates animalTurnTracker accordingly
     * @param source the original cell of the animal being moved
     * @param dest the cell where the animal is being moved to
     */
    private void moveAnimal (Cell source, Cell dest) {

        // place animal in destination
        dest.setNextState(source.getCurrState());

        // make animal's original location empty
        source.setNextState(EMPTY);

        // update animalTurnTracker
        animalTurnTracker.put(dest, animalTurnTracker.get(source));
        animalTurnTracker.remove(source);
    }

    // TODO Look at other ways to handle breed conflicts. Right now, breeding isn't parallel.
    // TODO Should I make a helper function for this method? For example: breedAnimal()
    /**
     * Have all animals that can breed (survived enough turns, have empty cardinal neighbor cell to breed into) breed
     */
    private void breedAnimals() {

        // list of all animals that are bred during the current simulation step (represented by their cells)
        List<Cell> bred = new ArrayList<>();

        Random rand = new Random();

        // have all animals that can breed breed
        for (Map.Entry<Cell, Integer> animalTracked : animalTurnTracker.entrySet()) {
            Cell animal = animalTracked.getKey();

            // only breed if animal has survived enough turns after being born or breeding
            if (animalTracked.getValue() >= NUM_TURNS_TO_BREED) {

                // if there is at least one empty cardinal neighbor cell, randomly choose one to breed into
                List<Cell> canBreedInto = getNeighborsOfType(animal, EMPTY, true);
                if (!canBreedInto.isEmpty()) {
                    Cell willBreedInto = canBreedInto.get(rand.nextInt(canBreedInto.size()));
                    willBreedInto.setNextState(animal.getCurrState());
                    bred.add(willBreedInto);
                }
            }

            // update the turn tracker by incrementing all animal's turns survived
            animalTurnTracker.put(animal, animalTurnTracker.get(animal) + 1);
        }

        // add all the newly bred animals to the animalTurnTracker
        for (Cell animal : bred) {
            animalTurnTracker.put(animal, 0);    // being bred does not count as having survived a turn
        }
    }
}