import java.util.List;
import java.util.Map;
import java.util.Random;

public class Fire extends Simulation {

    // the possible states of each cell
    final int EMPTY = 0;
    final int TREE = 1;
    final int BURNING = 2;

    // the initial probabilities of each state for use by populateGrid()
    final double FREQ_EMPTY;
    final double FREQ_TREE;
    final double FREQ_BURNING;
    // probability that a tree next to a burning tree catches on fire
    final double PROB_CATCH;

    public Fire(int sideSize, double freqEmpty, double freqTree, double freqBurning, double probCatch) {
        super(sideSize);
        FREQ_EMPTY = freqEmpty;
        FREQ_TREE = freqTree;
        FREQ_BURNING = freqBurning;
        PROB_CATCH = probCatch;
    }

    protected void populateGrid() {
        Random rand = new Random();
        for (int i = 0; i < gridSideSize; i++) {
            for (int j = 0; j < gridSideSize; j++) {
                int randNum = rand.nextInt(100);
                if (randNum < FREQ_EMPTY * 100) {
                    grid[i][j] = new Cell(EMPTY);
                }
                else if (randNum < FREQ_EMPTY + FREQ_TREE) {
                    grid[i][j] = new Cell(TREE);
                }
                else {
                    grid[i][j] = new Cell(BURNING);
                }
            }
        }
    }

    public void step() {
        // create another grid to hold the updated states, which saves us from writing another nested for loop for
        // updating the states
        int[][] updatedGrid = new int[gridSideSize][gridSideSize];

        Random rand = new Random();
        for (int i = 0; i < gridSideSize; i++) {
            for (int j = 0; j < gridSideSize; j++) {
                Map<int[], Integer> neighbors = getNeighbors(i, j);
                // if a tree is next to a burning tree, it will catch fire with a probability of PROB_CATCH
                if (grid[i][j] == TREE && neighbors.containsValue(BURNING)) {
                    int randNum = rand.nextInt(100);
                    if (randNum < PROB_CATCH * 100) {
                        updatedGrid[i][j] = BURNING;
                    }
                    else {
                        updatedGrid[i][j] = grid[i][j];
                    }
                }
                // if a tree is burning, it will burn down (become empty cell)
                else if (grid[i][j] == BURNING) {
                    updatedGrid[i][j] = EMPTY;
                }
                // otherwise, the cell remains the same
                else {
                    updatedGrid[i][j] = grid[i][j];
                }
            }
        }

        grid = updatedGrid;
    }
}
