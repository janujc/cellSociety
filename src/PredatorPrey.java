import java.util.*;

/**
 * Class that represents the Predator-Prey simulation
 * <p>
 * States: 0 (empty), 1 (fish), 2 (shark)
 */
public class PredatorPrey extends Simulation {

    // if a fish or shark survives for this number of turns, the it will breed
    private final int NUM_TURNS_TO_BREED;

    // the possible states of each cell (hard-coded b/c states are pre-determined)
    private final int EMPTY = 0;
    private final int FISH = 1;
    private final int SHARK = 2;

    // tracks the number of turns each fish and shark has gone since being born or breeding
    // keys are the current coordinates of the fish, values are the number of turns
    private Map<int[], Integer> animalTurnTracker;

    public PredatorPrey(int sideSize, double[] initialPopulationFreqs, int numTurnsToBreed) {
        super(sideSize, new int[]{0, 1, 2}, initialPopulationFreqs); // hard-coded b/c states are pre-determined
        NUM_TURNS_TO_BREED = numTurnsToBreed;
        initializeAnimalTurnTracker();
    }

    /**
     * Finds all of the fish and sharks in the initial grid and adds them to the turn tracker
     */
    private void initializeAnimalTurnTracker() {
        animalTurnTracker = new HashMap<>();
        for (int i = 0; i < gridSideSize; i++) {
            for (int j = 0; j < gridSideSize; j++) {
                if (grid[i][j] != EMPTY) {
                    animalTurnTracker.put(new int[]{i, j}, 0); // grid population doesn't count as a turn
                }
            }
        }
    }

    // TODO Refactor into multiple methods
    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules and then updates the grid
     */
    public void step() {
        // create another grid to hold the updated states for each step, will be re-initialized upon the start of each step
        int[][] updatedGrid = new int[gridSideSize][gridSideSize];

        // keys are sharks that will eat, values are fish to be eaten (all represented by their current coordinates)
        Map<int[], int[]> toEat = new HashMap<>();

        // list of animals that will move (all represented by their current coordinates)
        List<int[]> toMove = new ArrayList<>();

        Random rand = new Random();

        for (int x = 0; x < gridSideSize; x++) {
            for(int y = 0; y < gridSideSize; y++) {
                if (grid[x][y] == EMPTY) {
                    continue;
                }

                // need to get the array object straight from the tracker for map operations to work
                int[] currAnimal = getKeyFromTracker(new int[]{x, y});

                if (grid[x][y] == FISH) {
                    toMove.add(currAnimal);
                }
                else if (grid[x][y] == SHARK) {
                    // fish that the shark can eat
                    // predator-prey simulation only looks at adjacent neighbors, not corners
                    List<int[]> fishEdible = getNeighborsOfType(x, y, FISH, true);
                    if (!fishEdible.isEmpty()) {
                        // if there are fish the shark can eat, randomly choose one
                        int[] randomFish = fishEdible.get(rand.nextInt(fishEdible.size()));
                        // need to get the array object straight from the tracker for map operations to work
                        toEat.put(currAnimal, getKeyFromTracker(randomFish));
                    }
                    else {
                        toMove.add(currAnimal);
                    }
                }
            }
        }

        // have sharks eat their fish and remove the fish from the animalTurnTracker
        for (Map.Entry<int[], int[]> eat : toEat.entrySet()) {
            // need to get the array object straight from the tracker for map operations to work
            int[] eater = eat.getKey();
            int[] eaten = eat.getValue();
            toMove.remove(eaten);
            animalTurnTracker.remove(eaten);
            moveAnimal(eater, eaten, updatedGrid);
        }

        // TODO Determine if the small duplicated part of these two loops needs to be refactored

        // TODO Look at other ways to handle move conflicts
        // move all animals that need to be moved
        for (int[] mover : toMove) {
            List<int[]> canMoveTo = getNeighborsOfType(mover[0], mover[1], EMPTY, true);
            // if no empty adjacent cells, don't move
            if (canMoveTo.isEmpty()) {
                int moverX = mover[0];
                int moverY = mover[1];
                updatedGrid[moverX][moverY] = grid[moverX][moverY];
            }
            else {
                // randomly choose an empty adjacent cell to move to
                int[] willMoveTo = canMoveTo.get(rand.nextInt(canMoveTo.size()));
                moveAnimal(mover, willMoveTo, updatedGrid);
            }
        }

        // TODO Look at other ways to handle breed conflicts
        // have all animals that can breed breed
        for (Map.Entry<int[], Integer> animalTracked : animalTurnTracker.entrySet()) {
            // only breed if animal has survived enough turns after being born or breeding
            int[] animal = animalTracked.getKey();

            // update the turn tracker by incrementing all animal's turns survived
            animalTurnTracker.put(animal, animalTurnTracker.get(animal) + 1);

            // because the tracker update happens first, need to add 1 to NUM_TURNS_TO_BREED
            if (animalTracked.getValue() >= NUM_TURNS_TO_BREED + 1) {
                int animalX = animal[0];
                int animalY = animal[1];
                List<int[]> canBreedInto = getNeighborsOfType(animalX, animalY, EMPTY, true);
                // if there is at least one adjacent empty cell, randomly choose one to breed into
                if (!canBreedInto.isEmpty()) {
                    int[] willBreedInto = canBreedInto.get(rand.nextInt(canBreedInto.size()));
                    updatedGrid[willBreedInto[0]][willBreedInto[1]] = updatedGrid[animalX][animalY];
                    animalTurnTracker.put(willBreedInto, 0);
                }
            }
        }
    }

    /**
     * Moves animal from one cell to another and updates animalTurnTracker
     * @param source coordinates of animal's original location
     * @param dest coordinates of animal's destination
     */
    private void moveAnimal (int[] source, int[] dest, int[][] toUpdate) {
        int sourceX = source[0];
        int sourceY = source[1];
        int destX = dest[0];
        int destY = dest[1];
        // place animal in destination
        toUpdate[destX][destY] = toUpdate[sourceX][sourceY];
        // make animal's original location empty
        toUpdate[sourceX][sourceY] = EMPTY;
        // update animalTurnTracker
        animalTurnTracker.put(dest, animalTurnTracker.get(source));
        animalTurnTracker.remove(source);
    }

    // TODO Remove after adding Cell class and refactoring
    /**
     * Gets the animalTurnTracker key equivalent to a certain animal
     * @param animal coordinates of the animal in question
     * @return key from animalTurnTracker equivalent to the parameter animal
     */
    private int[] getKeyFromTracker(int[] animal) {
        for (int[] tracked : animalTurnTracker.keySet()) {
            // check array equality
            if (tracked[0] == animal[0] && tracked[1] == animal[1]) {
                return tracked;
            }
        }
        return null; // will never get here, implementation guarantees animal is in tracker
    }

}
