package simulation;

import javafx.scene.paint.Color;
import utils.Cell;

import java.util.*;

/**
 * Class that represents the Predator-Prey simulation
 * <p>
 * States: empty (0), fish (1), shark (2)
 *
 * @author Jonathan Yu
 */
public class PredatorPrey extends Simulation {

    /**
     * The possible states of each cell in the PredatorPrey simulation
     */
    private static final int EMPTY = 0;
    private static final int FISH = 1;
    private static final int SHARK = 2;
    private static final int UNDETERMINED = -1;    // only possible for next states

    /**
     * The color of an empty cell
     * <p>
     * Used to minimize array accesses
     */
    private final Color EMPTY_COLOR;

    /**
     * If a fish survives for this number of turns, it will breed.
     */
    private final int NUM_TURNS_TO_BREED_FISH;

    /**
     * If a shark goes this number of turns without eating a fish, it starves and dies.
     */
    private final int NUM_TURNS_TO_STARVE;

    /**
     * If a shark survives for this number of turns, it will breed.
     */
    private final int NUM_TURNS_TO_BREED_SHARK;

    /**
     * Tracks the number of turns each fish and shark has survived since being born or breeding
     * <p>
     * Key is the cell that currently holds the fish. Values are the number of turns survived.
     */
    private final Map<Cell, Integer> animalTurnTracker;

    /**
     * Tracks the number of turns each shark has gone since last eating a fish
     * <p>
     * Key is the cell that currently holds the shark. Values are the number of turns since last eating.
     */
    private final Map<Cell, Integer> sharkHungerTracker;

    /**
     * Creates the simulation and calls the super constructor to create the grid
     *
     * @param sideSize        the length of one side of the grid
     * @param states          the possible states of the cells in the simulation grid
     * @param populationFreqs the population frequencies of the states (not exact percentages)
     * @param stateColors     the cell colors of each state in the simulation
     * @param simData         the string containing the PredatorPrey-specific parameters (NUM_TURNS_TO_BREED_FISH,
     *                        NUM_TURNS_TO_STARVE, NUM_TURNS_TO_BREED_SHARK) each separated by a comma (",")
     */
    public PredatorPrey(int sideSize, Integer[] states, Double[] populationFreqs, Color[] stateColors, String simData) {
        super(sideSize, states, populationFreqs, stateColors);
        EMPTY_COLOR = colors[EMPTY];

        String[] data = simData.split(",");
        NUM_TURNS_TO_BREED_FISH = Integer.valueOf(data[0]);
        NUM_TURNS_TO_STARVE = Integer.valueOf(data[1]);
        NUM_TURNS_TO_BREED_SHARK = Integer.valueOf(data[2]);

        animalTurnTracker = new HashMap<>();
        sharkHungerTracker = new HashMap<>();
        initializeTrackers();
    }

    /**
     * Adds all of the animals to the turn tracker and adds all of the sharks to the hunger tracker
     */
    private void initializeTrackers() {
        for (Cell[] column : grid) {
            for (Cell cell : column) {
                int currState = cell.getCurrState();
                if (currState == EMPTY) {
                    continue;
                }

                // grid population does not count as a turn survived
                else if (currState == SHARK) {
                    sharkHungerTracker.put(cell, 0);
                }
                animalTurnTracker.put(cell, 0);
            }
        }
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules
     */
    @Override
    protected void calculateNextStates() {
        for (Cell cell : getCellsInRandomOrder()) {

            // if an animal has already moved or eaten into this cell, don't calculate next state
            if (cell.getNextState() != UNDETERMINED) {
                continue;
            }

            int currState = cell.getCurrState();
            if (currState == EMPTY) {
                cell.setNextState(EMPTY, EMPTY_COLOR);
            } else if (currState == FISH) {
                moveIfAble(cell);
            } else {
                eatOrMove(cell);
            }
        }
    }

    /**
     * Helper method that gets all of the cells in the grid in random order
     * <p>
     * Used to create less predictable simulations as animals can affect each others' behavior within the same step
     *
     * @return the list of cells in random order
     */
    private List<Cell> getCellsInRandomOrder() {
        List<Cell> allCells = new ArrayList<>();

        for (Cell[] column : grid) {
            Collections.addAll(allCells, column);
        }
        Collections.shuffle(allCells, rand);
        return allCells;
    }

    /**
     * Moves animal to a randomly-chosen empty cardinal neighbor cell. If no such cells exist, leaves it in the current
     * cell. If a shark does not move, checks if it starves.
     *
     * @param mover the animal that will move, represented by its current cell
     */
    private void moveIfAble(Cell mover) {
        List<Cell> canMoveTo = getCardinalNeighbors(mover);

        canMoveTo = getCellsOfType(canMoveTo, EMPTY);
        if (canMoveTo.isEmpty()) {
            int moverState = mover.getCurrState();

            // don't leave shark in its current cell if it is going to starve anyways
            if (moverState == SHARK && killSharkIfStarved(mover)) {
                return;
            }
            mover.setNextState(moverState, colors[moverState]);
        } else {
            Cell willMoveTo = chooseRandomCellFromList(canMoveTo);
            moveAnimal(mover, willMoveTo);
        }
    }

    /**
     * Helper method that takes a list of cells and returns the cells with a certain state
     *
     * @param cells the list of cells to consider
     * @param type  the desired state
     * @return the list of cells with the desired state
     */
    private List<Cell> getCellsOfType(List<Cell> cells, int type) {
        List<Cell> cellsOfType = new ArrayList<>();

        for (Cell cell : cells) {
            if (getMostRecentState(cell) == type) {
                cellsOfType.add(cell);
            }
        }
        return cellsOfType;
    }

    /**
     * Helper method that gets the most recently set state of a cell. If the cell's next state has already been set,
     * returns that. Otherwise, returns the current state.
     *
     * @param cell the cell whose state is desired
     * @return the most recently set state of the cell
     */
    private int getMostRecentState(Cell cell) {
        int nextState = cell.getNextState();

        return (nextState == UNDETERMINED) ? cell.getCurrState() : nextState;
    }

    /**
     * Helper method that randomly chooses a cell from a given list
     *
     * @param chooseFrom the list of cells to choose from
     * @return the randomly chosen cell
     */
    private Cell chooseRandomCellFromList(List<Cell> chooseFrom) {
        return chooseFrom.get(rand.nextInt(chooseFrom.size()));
    }

    /**
     * Moves animal from one cell to another. Prior to moving, checks if animal is a starved shark. After moving, checks
     * if animal should breed and, if so, it breeds into the cell it just moved from. Finally, updates all trackers
     * accordingly.
     *
     * @param source the original cell of the animal being moved
     * @param dest   the empty cell where the animal is being moved to
     */
    private void moveAnimal(Cell source, Cell dest) {
        int sourceState = source.getCurrState();

        if (sourceState == SHARK) {

            // check for starvation first to avoid needlessly moving a dead shark
            if (killSharkIfStarved(source)) {
                return;
            }
            sharkHungerTracker.put(dest, sharkHungerTracker.get(source));
            sharkHungerTracker.remove(source);
        }

        dest.setNextState(sourceState, colors[sourceState]);

        // update here to avoid needing separate put calls to move the animal and increment the number of turns
        animalTurnTracker.put(dest, animalTurnTracker.get(source) + 1);

        if (!breedAnimalIfSurvivedLongEnough(dest, source)) {
            animalTurnTracker.remove(source);
            source.setNextState(EMPTY, EMPTY_COLOR);
        }
    }

    /**
     * Updates hunger tracker to account for a turn passing. Kills shark if it has gone too many turns without eating.
     * If the shark does starve, removes it from the trackers.
     *
     * @param shark the shark that is being checked for starvation
     * @return true if the shark starves and dies, false otherwise
     */
    private boolean killSharkIfStarved(Cell shark) {
        sharkHungerTracker.put(shark, sharkHungerTracker.get(shark) + 1);
        int turnsSinceLastEating = sharkHungerTracker.get(shark);

        if (turnsSinceLastEating >= NUM_TURNS_TO_STARVE) {
            animalTurnTracker.remove(shark);
            sharkHungerTracker.remove(shark);
            shark.setNextState(EMPTY, EMPTY_COLOR);
            return true;
        }
        return false;
    }

    /**
     * Breeds animal into cell that it moved from if it has survived enough turns. If the animal does breed, updates the
     * turn tracker and adds the newly bred animal to the appropriate trackers.
     *
     * @param animal    the animal to potentially breed, represented by its current cell (after moving)
     * @param breedInto the cell that the animal would breed into (the cell the animal just moved from)
     * @return true if the animal breeds, false otherwise
     */
    private boolean breedAnimalIfSurvivedLongEnough(Cell animal, Cell breedInto) {

        // as the animal just moved, need to get the cell's next state
        int animalState = animal.getNextState();
        int turnsSurvived = animalTurnTracker.get(animal);

        if (animalState == FISH && turnsSurvived >= NUM_TURNS_TO_BREED_FISH) {
            breedInto.setNextState(FISH, colors[FISH]);
        } else if (animalState == SHARK && turnsSurvived >= NUM_TURNS_TO_BREED_SHARK) {
            breedInto.setNextState(SHARK, colors[SHARK]);
            sharkHungerTracker.put(breedInto, 0);
        } else {
            return false;
        }

        animalTurnTracker.put(animal, 0);
        animalTurnTracker.put(breedInto, 0);
        return true;
    }

    /**
     * Makes shark randomly choose a fish in a cardinal neighbor cell to eat. If no such fish exists, makes the shark
     * move.
     *
     * @param shark the shark that will eat or move, represented by its current cell
     */
    private void eatOrMove(Cell shark) {
        List<Cell> fishEdible = getCardinalNeighbors(shark);

        fishEdible = getCellsOfType(fishEdible, FISH);
        if (!fishEdible.isEmpty()) {
            Cell fishEaten = chooseRandomCellFromList(fishEdible);

            animalTurnTracker.remove(fishEaten);

            // shark has not moved yet, so update hunger tracker based off of the current cell
            sharkHungerTracker.put(shark, 0);
            moveAnimal(shark, fishEaten);
        } else {
            moveIfAble(shark);
        }
    }
}