import java.util.Map;
import java.util.Random;

/**
 * Class that represents the Spreading of Fire simulation
 * <p>
 * States: 0 (empty), 1 (tree), 2 (burning tree)
 */
public class Fire extends Simulation {

    // probability that a tree next to a burning tree catches on fire, which is passed to the constructor
    private final double PROB_CATCH;

    public Fire(int sideSize, double[] initialPopulationFreqs, double probCatch) {
        super(sideSize, new int[]{0, 1, 2}, initialPopulationFreqs); // hard-coded here b/c states are pre-determined
        PROB_CATCH = probCatch;
    }

    // TODO Should I put the body of the for loop in another method?
    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules and the passed-in
     * PROB_CATCH value, then updates the grid
     */
    public void step() {
        // the possible states of each cell
        final int EMPTY = 0;
        final int TREE = 1;
        final int BURNING = 2;

        // create another grid to hold the updated states, which saves us from writing another nested for loop for
        // updating the states
        int[][] updatedGrid = new int[gridSideSize][gridSideSize];

        Random rand = new Random();
        for (int x = 0; x < gridSideSize; x++) {
            for (int y = 0; y < gridSideSize; y++) {
                Map<int[], Integer> neighbors = getCardinalNeighbors(x, y);
                // if a tree is next to a burning tree, it will catch fire with a probability of PROB_CATCH
                if (grid[x][y] == TREE && neighbors.containsValue(BURNING)) {
                    int randNum = rand.nextInt(100);
                    if (randNum < PROB_CATCH * 100) {
                        updatedGrid[x][y] = BURNING;
                    }
                    else {
                        updatedGrid[x][y] = grid[x][y];
                    }
                }
                // if a tree is burning, it will burn down (become empty cell)
                else if (grid[x][y] == BURNING) {
                    updatedGrid[x][y] = EMPTY;
                }
                // otherwise, the cell remains the same
                else {
                    updatedGrid[x][y] = grid[x][y];
                }
            }
        }

        grid = updatedGrid;
    }
}
