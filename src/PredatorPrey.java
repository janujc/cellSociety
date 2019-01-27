import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class that represents the Predator-Prey simulation
 * <p>
 * States: 0 (empty), 1 (fish), 2 (shark)
 */
public class PredatorPrey extends Simulation {

    // if a fish survives for this number of turns, the fish will breed
    private final int NUM_TURNS_TO_BREED;

    // the possible states of each cell (hard-coded b/c states are pre-determined)
    private final int EMPTY = 0;
    private final int FISH = 1;
    private final int SHARK = 2;

    // tracks the number of turns each fish has survived
    // keys are the current coordinates of the fish, values are the number of turns
    private Map<int[], Integer> fishTurnTracker;

    public PredatorPrey(int sideSize, double[] initialPopulationFreqs, int numTurnsToBreed) {
        super(sideSize, new int[]{0, 1, 2}, initialPopulationFreqs); // hard-coded b/c states are pre-determined
        NUM_TURNS_TO_BREED = numTurnsToBreed;
        initializeFishTurnTracker();
    }

    /**
     * Finds all of the fish in the initial grid and adds them to the fish turn tracker
     */
    private void initializeFishTurnTracker() {
        for (int i = 0; i < gridSideSize; i++) {
            for (int j = 0; j < gridSideSize; j++) {
                if (grid[i][j] == FISH) {
                    fishTurnTracker.put(new int[]{i, j}, 0); // grid population doesn't count as a turn
                }
            }
        }
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules and then updates the grid
     */
    public void step() {
        // create another grid to hold the updated states, which saves us from writing another nested for loop for
        // updating the states
        int[][] updatedGrid = new int[gridSideSize][gridSideSize];

        Random rand = new Random();
        for (int i = 0; i < gridSideSize; i++) {
            for(int j = 0; j < gridSideSize; j++) {
                Map<int[], Integer> neighbors = getCardinalNeighbors(i, j);
                if (grid[i][j] == FISH) {

                }
            }
        }
    }

    /**
     * Determines which of the current cell's neighbors are empty and returns their coordinates
     * @param neighbors map of the cell's neighbors where the key is its coordinates and the value is its state
     * @return
     */
    private List<int[]> getEmptyNeighbors(Map<int[], Integer> neighbors) {
        List<int[]> emptyNeighbors = new ArrayList<>();

        for (Map.Entry<int[], Integer> neighbor : neighbors.entrySet()) {
            if (neighbor.getValue() == EMPTY) {
                emptyNeighbors.add(neighbor.getKey());
            }
        }

        return emptyNeighbors;
    }

}
