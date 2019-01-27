import java.util.Random;

public class Fire extends Simulation {

    // the possible states of each cell
    final int EMPTY = 0;
    final int TREE = 1;
    final int BURNING = 2;

    // the initial probabilities of each state for use by populateGrid()
    final double FREQ_EMPTY = .3;
    final double FREQ_TREE = .6;
    final double FREQ_BURNING = 1 - FREQ_EMPTY - FREQ_TREE;

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
}
