package simulation;

import javafx.scene.paint.Color;
import utils.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO Should I use Cell objects (current) or Fish/Shark objects?
// TODO Does this class even make sense?
/**
 * Class that represents the Predator-Prey simulation
 * <p>
 * States: empty (0), fish (1), shark (2)
 *
 * @author Jonathan Yu
 */
public class PredatorPrey extends Simulation {

    // TODO Shark is never used, but it's more readable to initialize here. Is that ok?
    /**
     * The possible states of each cell in the PredatorPrey simulation
     */
    private final int EMPTY = 0;
    private final int FISH = 1;
    private final int SHARK = 2;

    /**
     * The colors of the empty state (the other colors are only used by accessing the color array)
     * <p>
     * Used to minimize array accesses
     */
    private final Color COLOR_EMPTY;

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

    // TODO How to justify these lists being instance variables? They allow the step() method to be broken up into multiple, more readable ones.
    /**
     * The list of sharks in the grid, represented by their current cells
     */
    private List<Cell> willEat;

    /**
     * The list of animals that will move on a given step, represented by their current cells
     */
    private List<Cell> willMove;

    /**
     * The list of empty cells that will stay empty on a given step, represented by their current cells
     * <p>
     * This avoids unnecessarily setting the next state of empty cells as empty if they are ultimately going to have an
     * animal move or breed into them
     */
    private List<Cell> willStayEmpty;

    // TODO Is this way of avoiding conflicts ok?
    /**
     * The list of cells that have each already had an animal eat, move, or breed into it
     * <p>
     * This is used to prevent conflicts where animals "overwrite" each other after eating, moving, or breeding into the
     * same cell
     */
    private List<Cell> animalAlreadyHere;

    // TODO Do we need to pass in states since it's hard-coded for each simulation?
    /**
     * Creates the simulation and calls the super constructor to create the grid
     * @param sideSize the length of one side of the grid
     * @param populationFreqs the population frequencies of the states (not exact percentages)
     * @param metadata the string containing the PredatorPrey-specific parameters (NUM_TURNS_TO_BREED_FISH,
     *                 NUM_TURNS_TO_STARVE, NUM_TURNS_TO_BREED_SHARK) separated by a comma (",")
     */
    public PredatorPrey(int sideSize, Integer[] states, Double[] populationFreqs, Color[] stateColors, String metadata) {
        super(sideSize, states, populationFreqs, stateColors, metadata);    // hard-coded b/c states are pre-determined
        COLOR_EMPTY = colors[EMPTY];
        String[] data = metadata.split(",");
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

    // TODO Is this method being made up of five method calls w/o parameters ok?
    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules
     */
    @Override
    protected void calculateNextStates() {
        animalAlreadyHere = new ArrayList<>();

        determineCellBehaviors();
        makeSharksEat();
        moveAbleAnimals();
        breedAbleAnimalsAndKillStarvedSharksAndUpdateTracker();
        stayEmpty();
    }

    /**
     * Determine the type of behavior for each cell (stay empty, move, or eat)
     */
    private void determineCellBehaviors() {
        willEat = new ArrayList<>();
        willMove = new ArrayList<>();
        willStayEmpty = new ArrayList<>();

        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {

                // empty cells stay empty unless they are moved/bred into
                if (cell.getCurrState() == EMPTY) {
                    willStayEmpty.add(cell);
                }

                // fish attempt to move every turn
                else if (cell.getCurrState() == FISH) {
                    willMove.add(cell);
                }

                // shark attempt to eat every turn
                else {
                    willEat.add(cell);
                }
            }
        }
    }

    /**
     * Make each shark randomly choose a fish in a cardinal neighbor cell to eat or move if no such fish exists
     */
    private void makeSharksEat() {
        for (Cell shark : willEat) {
            List<Cell> fishEdible = getNeighborsOfType(shark, FISH, true);
            fishEdible = removeCellsWithAnimalsAlreadyThere(fishEdible);

            if (!fishEdible.isEmpty()) {
                Cell fishEaten = chooseRandomCellFromList(fishEdible);

                // remove the eaten fish from the simulation data
                willMove.remove(fishEaten);
                animalTurnTracker.remove(fishEaten);

                moveAnimal(shark, fishEaten);
            }
            else {
                willMove.add(shark);
            }
        }
    }

    /**
     * Move each animal that is supposed to move (sharks that don't eat, fish that aren't eaten) into a randomly chosen
     * empty cardinal neighbor cell or leave it in the current cell if no such neighbor exists
     */
    private void moveAbleAnimals() {
        for (Cell mover : willMove) {
            List<Cell> canMoveTo = getNeighborsOfType(mover, EMPTY, true);
            canMoveTo = removeNewEmptyCells(canMoveTo);
            canMoveTo = removeCellsWithAnimalsAlreadyThere(canMoveTo);

            if (canMoveTo.isEmpty()) {
                int curr = mover.getCurrState();
                mover.setNextState(curr, colors[curr]);
            }
            else {
                Cell willMoveTo = chooseRandomCellFromList(canMoveTo);
                moveAnimal(mover, willMoveTo);

                // as this empty cell has had an animal move into it, it will no longer stay empty
                willStayEmpty.remove(willMoveTo);
            }
        }
    }

    /**
     * Moves animal from one cell to another and updates animalTurnTracker and animalAlreadyHere accordingly
     * @param source the original cell of the animal being moved
     * @param dest the cell where the animal is being moved to
     */
    private void moveAnimal (Cell source, Cell dest) {
        int curr = source.getCurrState();

        // place animal in destination
        dest.setNextState(curr, colors[curr]);

        // make animal's original location empty
        source.setNextState(EMPTY, COLOR_EMPTY);

        animalTurnTracker.put(dest, animalTurnTracker.get(source));
        animalTurnTracker.remove(source);

        animalAlreadyHere.add(dest);
    }

    /**
     * Have all animals that can breed (survived enough turns, have empty cardinal neighbor cell to breed into) breed.
     * Also, kill sharks that have starved. Finally, update the animalTurnTracker.
     * <p>
     * All of these operations are done here to save having to iterate through the map another time.
     */
    private void breedAbleAnimalsAndKillStarvedSharksAndUpdateTracker() {

        // the map of new animals that are bred, used to add them to the tracker without affecting iteration
        Map<Cell, Integer> bredAnimals = new HashMap<>();

        // the map of sharks that have starved and died
        List<Cell> starved = new ArrayList<>();

        for (Map.Entry<Cell, Integer> animalTracked : animalTurnTracker.entrySet()) {
            Cell animal = animalTracked.getKey();
            animalTurnTracker.put(animal, animalTurnTracker.get(animal) + 1);

            // check if shark has starved starved and kills it if so
            if (animal.getCurrState() == SHARK) {
                int turnsSinceLastEating = sharkHungerTracker.get(animal);
                Cell shark = killSharkIfStarved(animal, turnsSinceLastEating);
                if (shark != null) {
                    starved.add(shark);
                    continue;
                }
            }

            int turnsSurvived = animalTracked.getValue();
            Cell bred = breedAnimalIfAble(animal, turnsSurvived);
            if (bred != null) {
                bredAnimals.put(bred, 0);    // being bred does not count as having survived a turn
            }
        }

        for (Cell dead : starved) {
            animalTurnTracker.remove(dead);
        }

        animalTurnTracker.putAll(bredAnimals);
    }

    /**
     * Kills shark if it has gone too many turns without eating
     * @param shark the shark that is being checked for starvation
     * @param turnsSinceLastEating the number of turns the shark has gone without eating
     * @return the cell of the shark if it has starved and died, null otherwise
     */
    private Cell killSharkIfStarved(Cell shark, int turnsSinceLastEating) {
        if (turnsSinceLastEating >= NUM_TURNS_TO_STARVE) {
            shark.setNextState(EMPTY, COLOR_EMPTY);
            return shark;
        }
        return null;
    }

    // TODO Is this method too long?
    /**
     * Breeds animal into empty cardinal neighbor cell if it has survived enough turns after being born or last breeding.
     * If no such neighbor exists, don't breed.
     * @param animal the animal to potentially breed, represented by its current cell
     * @param turnsSurvived the number of turns survived by that animal since being born or last breeding (whichever was
     *                      most recent)
     * @return the cell that is bred into, null if no breeding occurs
     */
    private Cell breedAnimalIfAble(Cell animal, int turnsSurvived) {
        int curr = animal.getCurrState();

        boolean willBreed = false;

        if (curr == FISH && turnsSurvived >= NUM_TURNS_TO_BREED_FISH) {
            willBreed = true;
        }
        else if (curr == SHARK && turnsSurvived >= NUM_TURNS_TO_BREED_SHARK) {
            willBreed = true;
        }

        if (willBreed) {
            List<Cell> canBreedInto = getNeighborsOfType(animal, EMPTY, true);
            canBreedInto = removeNewEmptyCells(canBreedInto);
            canBreedInto = removeCellsWithAnimalsAlreadyThere(canBreedInto);

            if (!canBreedInto.isEmpty()) {
                Cell willBreedInto = chooseRandomCellFromList(canBreedInto);
                willBreedInto.setNextState(curr, colors[curr]);

                // as this empty cell has been bred into, it will no longer stay empty
                willStayEmpty.remove(willBreedInto);

                // as an animal has now bred here, update animalAlreadyHere accordingly
                animalAlreadyHere.add(willBreedInto);

                return willBreedInto;
            }
        }
        return null;
    }

    /**
     * Have each empty cell that has not had an animal move or breed into it stay empty
     */
    private void stayEmpty() {
        for (Cell empty : willStayEmpty) {
            empty.setNextState(EMPTY, COLOR_EMPTY);
        }
    }

    // TODO Is this way of ensuring lock-step synchronization when it comes to moving/breeding into empty cells ok?
    /**
     * Takes a list of empty neighbor cells and returns a copy of the list with only cells that were empty prior to the
     * current simulation step.
     * <p>
     * This ensures lock-step synchronization and that animals don't move/breed into cells made empty by another animal
     * moving in the same step. willStayEmpty is the list of cells that were empty prior to the step except for the ones
     * that have since been filled by an animal (doesn't affect this method).
     * @param emptyCells the list of empty neighbor cells
     * @return the list of empty neighbor cells that were originally empty
     */
    private List<Cell> removeNewEmptyCells(List<Cell> emptyCells) {
        List<Cell> noNewEmptyCells = new ArrayList<>();

        for (Cell emptyCell : emptyCells) {
            if (willStayEmpty.contains(emptyCell)) {
                noNewEmptyCells.add(emptyCell);
            }
        }
        return noNewEmptyCells;
    }

    // TODO Is this method name ok?
    /**
     * Takes a list of potential destination cells for an animal (after eating, moving, or breeding) and returns a copy
     * of the list with only cells that each have not been eaten, moved, or bred into by another animal.
     * <p>
     * This ensures no conflicts occur where animals "overwrite" each other after eating, moving, or breeding into the
     * same cell.
     */
    private List<Cell> removeCellsWithAnimalsAlreadyThere(List<Cell> potentialDests) {
        List<Cell> noAnimalsAlreadyThere = new ArrayList<>();

        for (Cell potentialDest : potentialDests) {
            if (!animalAlreadyHere.contains(potentialDest)) {
                noAnimalsAlreadyThere.add(potentialDest);
            }
        }
        return noAnimalsAlreadyThere;
    }
}
