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
     * The colors of the empty state (the other colors are only used by accessing the color array with a variable index)
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
        COLOR_EMPTY = colors[EMPTY];

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
        killStarvedAndBreedAnimals();
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
            fishEdible = removeCellsWithAnimalsAlreadyThere(fishEdible);

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
     * Move each animal that is supposed to move (sharks that don't eat, fish that aren't eaten) to a randomly-chosen
     * empty cardinal neighbor cell. If no such cells exist, leave it in the current cell.
     */
    private void moveAbleAnimals() {

        // animals move in random order
        Collections.shuffle(willMove);
        for (Cell mover : willMove) {
            List<Cell> canMoveTo = getCardinalNeighbors(mover);
            canMoveTo = removeCellsThatWillNotBeEmpty(canMoveTo);
            canMoveTo = removeCellsWithAnimalsAlreadyThere(canMoveTo);

            if (canMoveTo.isEmpty()) {
                int curr = mover.getCurrState();
                mover.setNextState(curr, colors[curr]);
            } else {
                Cell willMoveTo = chooseRandomCellFromList(canMoveTo);
                moveAnimal(mover, willMoveTo);
            }
        }
    }

    /**
     * Moves animal from one cell to another empty cell and updates animalTurnTracker and animalAlreadyHere accordingly
     *
     * @param source the original cell of the animal being moved
     * @param dest   the empty cell where the animal is being moved to
     */
    private void moveAnimal(Cell source, Cell dest) {
        int curr = source.getCurrState();

        dest.setNextState(curr, colors[curr]);
        willBeEmpty.remove(dest);

        source.setNextState(EMPTY, COLOR_EMPTY);
        willBeEmpty.add(source);

        animalTurnTracker.put(dest, animalTurnTracker.get(source));
        animalTurnTracker.remove(source);

        if (curr == SHARK) {
            sharkHungerTracker.put(dest, sharkHungerTracker.get(source));
            sharkHungerTracker.remove(source);
        }
    }

    /**
     * Kill sharks that have starved. Then, have all animals that can breed do so. Finally, update the turn tracker to
     * account for these occurrences.
     * <p>
     * Both of these operations are done here to save having to iterate through the map more than once.
     */
    private void killStarvedAndBreedAnimals() {

        // the new animals that are bred, used to add them to the turn tracker without affecting iteration
        Map<Cell, Integer> bredAnimals = new HashMap<>();

        // the sharks that have starved and died, used to remove them from the turn tracker without affecting iteration
        List<Cell> starved = new ArrayList<>();

        for (Cell animal : animalTurnTracker.keySet()) {

            // determine if shark is starved and kill if so (need to use next state as all moves have already occurred)
            if (animal.getNextState() == SHARK) {
                if (killSharkIfStarved(animal)) {
                    starved.add(animal);
                    continue;
                }
            }

            // determine if animal is able to breed and do it if so
            Cell bred = breedAnimalIfSurvivedLongEnough(animal);
            if (bred != null) {
                bredAnimals.put(bred, 0);
            }
        }

        // update turn tracker to account for deaths and breeding
        for (Cell dead : starved) {
            animalTurnTracker.remove(dead);
        }
        animalTurnTracker.putAll(bredAnimals);
    }

    /**
     * Update hunger tracker to account for a turn passing. Kill shark if it has gone too many turns without eating.
     *
     * @param shark the shark that is being checked for starvation
     * @return whether the shark has starved or not (true if yes, false otherwise)
     */
    private boolean killSharkIfStarved(Cell shark) {
        sharkHungerTracker.put(shark, sharkHungerTracker.get(shark) + 1);
        int turnsSinceLastEating = sharkHungerTracker.get(shark);

        if (turnsSinceLastEating >= NUM_TURNS_TO_STARVE) {
            shark.setNextState(EMPTY, COLOR_EMPTY);
            sharkHungerTracker.remove(shark);
            willBeEmpty.add(shark);
            return true;
        }
        return false;
    }

    /**
     * Update turn tracker to account for a turn passing. Attempt to breed animal if it has survived enough turns.
     *
     * @param animal the animal to potentially breed, represented by its current cell
     * @return the cell that is bred into, null if no breeding occurs
     */
    private Cell breedAnimalIfSurvivedLongEnough(Cell animal) {
        animalTurnTracker.put(animal, animalTurnTracker.get(animal) + 1);
        int curr = animal.getCurrState();
        int turnsSurvived = animalTurnTracker.get(animal);
        Cell bred = null;

        if (curr == FISH && turnsSurvived >= NUM_TURNS_TO_BREED_FISH) {
            bred = breedAbleAnimal(animal);
        } else if (curr == SHARK && turnsSurvived >= NUM_TURNS_TO_BREED_SHARK) {
            bred = breedAbleAnimal(animal);
        }
        return bred;
    }

    /**
     * Breed animal into a randomly-chosen empty cardinal neighbor cell. If none such cells exist, do nothing.
     * <p>
     * This is the breeding behavior as specified by the Wa-Tor World Assignment, not the wiki.
     *
     * @param animal the animal to potentially breed, represented by its current cell
     * @return the cell that is bred into, null if no breeding occurs
     */
    private Cell breedAbleAnimal(Cell animal) {
        List<Cell> canBreedInto = getCardinalNeighbors(animal);
        canBreedInto = removeCellsThatWillNotBeEmpty(canBreedInto);
        canBreedInto = removeCellsWithAnimalsAlreadyThere(canBreedInto);

        if (!canBreedInto.isEmpty()) {
            Cell willBreedInto = chooseRandomCellFromList(canBreedInto);
            int curr = animal.getCurrState();
            willBreedInto.setNextState(curr, colors[curr]);

            animalTurnTracker.put(animal, 0);

            if (curr == SHARK) {
                sharkHungerTracker.put(willBreedInto, 0);
            }

            willBeEmpty.remove(willBreedInto);

            return willBreedInto;
        }
        return null;
    }

    /**
     * Have each cell that does not contain an animal stay empty
     */
    private void stayEmpty() {
        for (Cell empty : willBeEmpty) {
            empty.setNextState(EMPTY, COLOR_EMPTY);
        }
    }

    /**
     * Helper method that takes a list of cells and removes all cells that are not currently set to be empty
     *
     * @param potentiallyNotEmpty the list of cells to be modified
     * @return the list of cells from potentiallyNotEmpty that will be empty
     */
    private List<Cell> removeCellsThatWillNotBeEmpty(List<Cell> potentiallyNotEmpty) {
        List<Cell> emptyCells = new ArrayList<>();

        for (Cell cell : potentiallyNotEmpty) {
            if (willBeEmpty.contains(cell)) {
                emptyCells.add(cell);
            }
        }
        return emptyCells;
    }

    /**
     * Helper method that takes a list of cells and removes all cells that are already occupied by animals
     * <p>
     * This ensures no conflicts occur where animals "overwrite" each other after eating, moving, or breeding into the
     * same cell
     *
     * @param potentiallyOccupied the list of cells to be modified
     * @return the list of cells from potentiallyOccupied that do not already contain animals
     */
    private List<Cell> removeCellsWithAnimalsAlreadyThere(List<Cell> potentiallyOccupied) {
        List<Cell> noAnimalsAlreadyThere = new ArrayList<>();

        for (Cell potentialDest : potentiallyOccupied) {

            // no animal there if next state is -1 (undetermined) or 0 (empty)
            if (potentialDest.getNextState() <= 0) {
                noAnimalsAlreadyThere.add(potentialDest);
            }
        }
        return noAnimalsAlreadyThere;
    }

    /**
     * Helper method that randomly chooses a cell from a given list
     *
     * @param cells the list of cells to choose from
     * @return the randomly chosen cell
     */
    private Cell chooseRandomCellFromList(List<Cell> cells) {
        return cells.get(rand.nextInt(cells.size()));
    }
}
