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
    private final int EMPTY = 0;
    private final int FISH = 1;
    private final int SHARK = 2;

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
     * Used for chooseRandomCellFromList(). Implemented as an instance variable to avoid initializing multiple times in
     * a short time period, resulting in similar seeds.
     */
    private final Random rand;
    /**
     * The list of sharks in the grid on each step, represented by their current cells
     */
    private List<Cell> willEat;
    /**
     * The list of animals that are set to move on each step, represented by their current cells
     */
    private List<Cell> willMove;
    /**
     * The list of cell that are set to be empty after each step, represented by their current cells
     */
    private List<Cell> willBeEmpty;

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

        String[] data = simData.split(",");
        NUM_TURNS_TO_BREED_FISH = Integer.valueOf(data[0]);
        NUM_TURNS_TO_STARVE = Integer.valueOf(data[1]);
        NUM_TURNS_TO_BREED_SHARK = Integer.valueOf(data[2]);

        animalTurnTracker = new HashMap<>();
        sharkHungerTracker = new HashMap<>();
        initializeTrackers();

        rand = new Random();
    }

    /**
     * Adds all of the animals to the turn tracker and adds all of the sharks to the hunger tracker
     */
    private void initializeTrackers() {
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                if (cell.getCurrState() != EMPTY) {

                    // grid population does not count as a turn survived
                    animalTurnTracker.put(cell, 0);
                    if (cell.getCurrState() == SHARK) {
                        sharkHungerTracker.put(cell, 0);
                    }
                }
            }
        }
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules
     */
    @Override
    protected void calculateNextStates() {
        determineCellBehaviors();
        makeSharksEat();
        moveAbleAnimals();
        stayEmpty();
    }

    /**
     * Determine the type of behavior for each cell (stay empty, move, or eat)
     */
    private void determineCellBehaviors() {
        willEat = new ArrayList<>();
        willMove = new ArrayList<>();
        willBeEmpty = new ArrayList<>();

        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {

                // empty cells stay empty unless they are moved/bred into
                if (cell.getCurrState() == EMPTY) {
                    willBeEmpty.add(cell);
                }

                // fish attempt to move every turn
                else if (cell.getCurrState() == FISH) {
                    willMove.add(cell);
                }

                // sharks attempt to eat every turn
                else {
                    willEat.add(cell);
                }
            }
        }
    }

    /**
     * Make each shark randomly choose a fish in a cardinal neighbor cell to eat or move if no such fish exists. Do not
     * eat a fish that another shark has already eaten.
     */
    private void makeSharksEat() {

        // sharks eat in random order as opposed to the order they appear in the grid
        Collections.shuffle(willEat);
        for (Cell shark : willEat) {
            List<Cell> fishEdible = getNeighborsOfType(shark, FISH, true);
            fishEdible = removeAlreadyEatenFish(fishEdible);

            if (!fishEdible.isEmpty()) {
                Cell fishEaten = chooseRandomCellFromList(fishEdible);

                // remove the eaten fish from the simulation
                animalTurnTracker.remove(fishEaten);
                willMove.remove(fishEaten);

                // shark has not moved yet, so update hunger tracker based off of the current cell
                sharkHungerTracker.put(shark, 0);
                moveAnimal(shark, fishEaten);
            } else {
                willMove.add(shark);
            }
        }
    }

    /**
     * Helper method that takes a list of fish and removes all fish that have already been eaten by a shark
     * <p>
     * This ensures no conflicts occur where sharks "overwrite" each other after eating the same fish
     *
     * @param fish the list of fish to be modified
     * @return the list of fish from the parameter list that have not already been eaten
     */
    private List<Cell> removeAlreadyEatenFish(List<Cell> fish) {
        List<Cell> noAlreadyEatenFish = new ArrayList<>();

        for (Cell potentialDest : fish) {

            // no animal there if next state is -1 (undetermined) or 0 (empty)
            if (potentialDest.getNextState() != SHARK) {
                noAlreadyEatenFish.add(potentialDest);
            }
        }
        return noAlreadyEatenFish;
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
     * Move each animal that is supposed to move (sharks that don't eat, fish that aren't eaten) to a randomly-chosen
     * empty cardinal neighbor cell. If no such cells exist, leave it in the current cell. If a shark does not move,
     * check if it starves.
     */
    private void moveAbleAnimals() {

        // animals move in random order
        Collections.shuffle(willMove);
        for (Cell mover : willMove) {
            List<Cell> canMoveTo = getCardinalNeighbors(mover);

            // only consider neighbor cells that are currently set to be empty
            canMoveTo.retainAll(willBeEmpty);

            if (canMoveTo.isEmpty()) {
                int moverState = mover.getCurrState();
                mover.setNextState(moverState, colors[moverState]);
                if (moverState == SHARK) {
                    killSharkIfStarved(mover);
                }
            } else {
                Cell willMoveTo = chooseRandomCellFromList(canMoveTo);
                moveAnimal(mover, willMoveTo);
            }
        }
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
        willBeEmpty.remove(dest);

        if (!breedAnimalIfSurvivedLongEnough(dest, source)) {
            animalTurnTracker.remove(source);
            willBeEmpty.add(source);
        }
    }

    /**
     * Update hunger tracker to account for a turn passing. Kill shark if it has gone too many turns without eating.
     * If the shark does starve, remove it from the trackers.
     *
     * @param shark the shark that is being checked for starvation
     * @return true if the shark starved and died, false otherwise
     */
    private boolean killSharkIfStarved(Cell shark) {
        sharkHungerTracker.put(shark, sharkHungerTracker.get(shark) + 1);
        int turnsSinceLastEating = sharkHungerTracker.get(shark);

        if (turnsSinceLastEating >= NUM_TURNS_TO_STARVE) {
            animalTurnTracker.remove(shark);
            sharkHungerTracker.remove(shark);
            willBeEmpty.add(shark);
            return true;
        }
        return false;
    }

    /**
     * Breed animal into cell that it moved from if it has survived enough turns. If the animal does breed, update the
     * turn tracker and add the newly bred animal to the appropriate trackers.
     *
     * @param animal    the animal to potentially breed, represented by its current cell (after moving)
     * @param breedInto the cell that the animal would breed into (the cell the animal just moved from)
     * @return true if the animal bred, false otherwise
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
     * Have each cell that does not contain an animal stay empty
     */
    private void stayEmpty() {
        for (Cell empty : willBeEmpty) {
            empty.setNextState(EMPTY, colors[EMPTY]);
        }
    }
}