package simulation;

import javafx.scene.paint.Color;
import utils.Cell;

import java.util.*;

// TODO Should I use Cell objects (current) or Fish/Shark objects?
// TODO Is it okay that eating, moving, and breeding don't truly follow lock-step synchronization when dealing with conflicts?
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
     * The possible states of each cell in the Fire simulation
     */
    private final int EMPTY = 0;
    private final int FISH = 1;
    private final int SHARK = 2;

    /**
     * If a fish or shark survives for this number of turns, it will breed.
     */
    private final int NUM_TURNS_TO_BREED;

    /**
     * Tracks the number of turns each fish and shark has survived since being born or breeding.
     * <p>
     * Key is the cell that currently holds the fish (prior to step). Values are the number of turns survived.
     */
    private final Map<Cell, Integer> animalTurnTracker;

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
     * The list of empty cells that will stay empty on a given step, represented by their current cells.
     * <p>
     * This avoids unnecessarily setting the next state of empty cells as empty if they are ultimately going to have an
     * animal move or breed into them.
     */
    private List<Cell> willStayEmpty;

    // TODO Is this way of avoiding conflicts ok?
    /**
     * The list of cells that have each already had an animal eat, move, or breed into it.
     * <p>
     * This is used to prevent conflicts where animals "overwrite" each other after eating, moving, or breeding into the
     * same cell.
     */
    private List<Cell> animalAlreadyHere;

    /**
     * Creates the simulation and calls the super constructor to create the grid
     * @param sideSize the length of one side of the grid
     * @param populationFreqs the population frequencies of the states (not exact percentages)
     * @param numTurnsToBreed number of turns survived needed to breed
     */
    public PredatorPrey(int sideSize, double[] populationFreqs, int numTurnsToBreed) {
        super(sideSize, new int[]{0, 1, 2}, populationFreqs);    // hard-coded b/c states are pre-determined
        NUM_TURNS_TO_BREED = numTurnsToBreed;
        animalTurnTracker = new HashMap<>();
        initializeAnimalTurnTracker();
    }

    /**
     * Finds all of the fish and sharks in the initial grid and adds them to the turn tracker
     */
    private void initializeAnimalTurnTracker() {
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                if (cell.getCurrState() != EMPTY) {
                    animalTurnTracker.put(cell, 0);    // grid population doesn't count as a turn
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
        breedAbleAnimalsAndUpdateTracker();
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
                mover.setNextState(mover.getCurrState(), mover.getColor());
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

        // place animal in destination
        dest.setNextState(source.getCurrState(), source.getColor());

        // make animal's original location empty
        source.setNextState(EMPTY, Color.GHOSTWHITE);

        animalTurnTracker.put(dest, animalTurnTracker.get(source));
        animalTurnTracker.remove(source);

        animalAlreadyHere.add(dest);
    }

    /**
     * Have all animals that can breed (survived enough turns, have empty cardinal neighbor cell to breed into) breed.
     * Also, update the animalTurnTracker.
     * <p>
     * The tracker is updated here to save having to iterate through the map another time.
     */
    private void breedAbleAnimalsAndUpdateTracker() {
        for (Map.Entry<Cell, Integer> animalTracked : animalTurnTracker.entrySet()) {
            Cell animal = animalTracked.getKey();
            int turnsSurvived = animalTracked.getValue();

            Cell bred = breedAnimalIfAble(animal, turnsSurvived);
            // TODO Are these if statements ok?
            if (bred == null) {
                continue;
            }

            if (!animalTurnTracker.containsKey(bred)) {
                animalTurnTracker.put(bred, -1);    // being bred does not count as having survived a turn
            }
            animalTurnTracker.put(animal, animalTurnTracker.get(animal) + 1);
        }
    }

    /**
     * Breed animal into empty cardinal neighbor cell if it has survived enough turns after being born or last breeding.
     * If no such neighbor exists, don't breed.
     * @param animal the animal to potentially breed, represented by its current cell
     * @param turnsSurvived the number of turns survived by that animal since being born or last breeding (whichever was
     *                      most recent)
     * @return the cell that is bred into, null if no breeding occurs
     */
    private Cell breedAnimalIfAble(Cell animal, int turnsSurvived) {
        if (turnsSurvived >= NUM_TURNS_TO_BREED) {
            List<Cell> canBreedInto = getNeighborsOfType(animal, EMPTY, true);
            canBreedInto = removeNewEmptyCells(canBreedInto);
            canBreedInto = removeCellsWithAnimalsAlreadyThere(canBreedInto);

            if (!canBreedInto.isEmpty()) {
                Cell willBreedInto = chooseRandomCellFromList(canBreedInto);
                willBreedInto.setNextState(animal.getCurrState(), animal.getColor());

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
            empty.setNextState(EMPTY, Color.GHOSTWHITE);
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
