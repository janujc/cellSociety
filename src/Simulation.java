import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for all simulations
 * <p>
 * This class creates a simulation grid and provides functionality to update the grid.
 */
public abstract class Simulation {

    // grid is in (x, y) coordinate form, so the outer array represents the columns and the inner array represents the
    // element of each row in a particular column
    private Cell[][] grid;
    private int gridSideSize;

    public Simulation(int sideSize) {
        gridSideSize = sideSize;
        grid = new Cell[gridSideSize][gridSideSize];
        populateGrid();
    }

    /**
     * Fills the grid with Cell objects
     */
    protected abstract void populateGrid();

    /**
     * Calculates the next state for each Cell object in the grid, then updates the grid
     */
    public abstract void step();

    /**
     * Gets the neighbors of a particular cell in the grid
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @return list of ordered pairs, representing the neighbors, in the order {N, NE, E, SE, S, SW, W, NW} where
     * neighbor cells that are not in the grid (invalid) are null
     */
    protected List<int[]> getNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();

        // N
        neighbors.add(new int[]{x, y - 1});
        // NE
        neighbors.add(new int[]{x + 1, y - 1});
        // E
        neighbors.add(new int[]{x + 1, y});
        // SE
        neighbors.add(new int[]{x + 1, y + 1});
        // S
        neighbors.add(new int[]{x, y + 1});
        // SW
        neighbors.add(new int[]{x - 1, y + 1});
        // W
        neighbors.add(new int[]{x - 1, y});
        //NW
        neighbors.add(new int[]{x -1, y - 1});

        // mark invalid neighbors (not in grid) null
        for (int i = 0; i < neighbors.size(); i ++) {
            int[] neighbor = neighbors.get(i);
            if (!isCellValid(neighbor[0], neighbor[1])) {
                neighbors.set(i, null);
            }
        }

        return neighbors;
    }

    /**
     * Determines if a particular cell is in the grid or not
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @return true if the cell is in the grid (valid), false otherwise (invalid)
     */
    protected boolean isCellValid(int x, int y) {
        if (x < 0 || x >= gridSideSize || y < 0 || y >= gridSideSize) {
            return false;
        }

        return true;
    }

    /**
     * Returns the grid for other classes to access
     * @return the simulation grid
     */
    public Cell[][] getGrid() {
        return grid;
    }

}
