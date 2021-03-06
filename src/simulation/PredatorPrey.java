package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents the Predator-Prey simulation
 *
 * @author Jonathan Yu
 */
public class PredatorPrey extends Simulation {

    /**
     * The possible states of each cell in the PredatorPrey simulation
     */
    private final int EMPTY;
    private final int FISH;
    private final int SHARK;

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
     * @param grid           the simulation grid
     * @param simStates      the possible states of the cells in the simulation grid
     * @param stateColors    the cell colors of each state in the simulation
     * @param populatingType designates how the grid should be populated (with a list, randomly, with set numbers of
     *                       each state, based on frequencies)
     * @param simData        the string containing the PredatorPrey-specific parameters (NUM_TURNS_TO_BREED_FISH,
     *                       NUM_TURNS_TO_STARVE, NUM_TURNS_TO_BREED_SHARK) each separated by a comma (",")
     */
    public PredatorPrey(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType,
                        Object populatingInfo, String simData) {
        super(grid, simStates, stateColors, populatingType, populatingInfo);

        EMPTY = states[0];
        FISH = states[1];
        SHARK = states[2];

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
        for (int x = 0; x < gridNumCols; x++) {
            for (int y = 0; y < gridNumRows; y++) {
                Cell currCell = myGrid.getCellAt(x, y);
                int currState = currCell.getCurrState();
                if (currState == EMPTY) {
                    continue;
                }

                // grid population does not count as a turn survived
                else if (currState == SHARK) {
                    sharkHungerTracker.put(currCell, 0);
                }
                animalTurnTracker.put(currCell, 0);
            }
        }
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules
     * <p>
     * Simulation rules: Fish move to a random, empty cardinal neighbor cell. Sharks eat a random cardinal neighbor fish.
     * If no such fish exists, the shark will move like a fish. If animals have survived enough turns since breeding or
     * being bred, they breed (reproduce) into the cell they just moved from. If sharks go a certain number of turns
     * without eating a fish, they starve and die.
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
     * Moves animal to a randomly-chosen empty cardinal neighbor cell. If no such cells exist, leaves it in the current
     * cell. If a shark does not move, checks if it starves.
     *
     * @param mover the animal that will move, represented by its current cell
     */
    private void moveIfAble(Cell mover) {
        List<Cell> canMoveTo = myGrid.getNeighbors(mover, true);

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
        List<Cell> fishEdible = myGrid.getNeighbors(shark, true);

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

    /**
     * Rotates the state of the cell at a certain location. If the cell is rotated to hold an animal, adds the animal to
     * the trackers.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    @Override
    public void rotateState(int x, int y) {
        Cell currCell = myGrid.getCellAt(x, y);
        int currState = currCell.getCurrState();
        int newState = (currState + 1) % states.length;

        animalTurnTracker.remove(currCell);
        sharkHungerTracker.remove(currCell);
        if (newState != EMPTY) {
            animalTurnTracker.put(currCell, 0);

            if (newState == SHARK) {
                sharkHungerTracker.put(currCell, 0);
            }
        }

        currCell.setState(newState, colors[newState]);
    }
}