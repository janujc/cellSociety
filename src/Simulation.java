import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Superclass for all simulations
 * <p>
 * This class creates a simulation grid and provides functionality to update the grid.
 */
public abstract class Simulation {

    /*
     * Simulation grid made up of states, represented by ints
     * NOTE: grid is in (x, y) coordinate form, so the outer array represents the columns and the inner array represents
     * the element of each row in a particular column
     */
    protected int[][] grid;
    protected int gridSideSize;

    public Simulation(int sideSize) {
        gridSideSize = sideSize;
        grid = new Cell[gridSideSize][gridSideSize];
        populateGrid();
    }

    /**
     * Fills the grid with states
     */
    protected abstract void populateGrid();

    /**
     * Calculates the next state for each cell in the grid, then updates the grid
     */
    public abstract void step();

    /**
     * Gets the all neighbors (cardinal and corner) of a particular cell in the grid
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @return map of neighbors where the key is the neighbor's coordinates (int array where 0th index is the neighbor's
     * x-coordinate and the 1st index is the neighbor's y-coordinate) and the value is the state of the neighbor
     */
    protected Map<int[], Integer> getAllNeighbors(int x, int y) {
        Map<int[], Integer> neighbors = new HashMap<>();

        // as the coordinates of the cardinal and corner neighbors are distinct, no worry about overwriting entries
        neighbors.putAll(getCardinalNeighbors(x, y));
        neighbors.putAll(getCornerNeighbors(x, y));

        return neighbors;
    }

    /**
     * Gets the cardinal direction neighbors of a particular cell in the grid
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @return map of neighbors where the key is the neighbor's coordinates (int array where 0th index is the neighbor's
     * x-coordinate and the 1st index is the neighbor's y-coordinate) and the value is the state of the neighbor
     */
    protected Map<int[], Integer> getCardinalNeighbors(int x, int y) {
        // list of the coordinates of the cell's neighbors, represented by an array, where the 0th index is the
        // neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate
        List<int[]> neighborCoords = new ArrayList<>();

        // N
        neighborCoords.add(new int[]{x, y - 1});
        // E
        neighborCoords.add(new int[]{x + 1, y});
        // S
        neighborCoords.add(new int[]{x, y + 1});
        // W
        neighborCoords.add(new int[]{x - 1, y});
        return validateNeighbors(neighborCoords);
    }

    /**
     * Gets the corner neighbors of a particular cell in the grid
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @return map of neighbors where the key is the neighbor's coordinates (int array where 0th index is the neighbor's
     * x-coordinate and the 1st index is the neighbor's y-coordinate) and the value is the state of the neighbor
     */
    protected Map<int[], Integer>  getCornerNeighbors(int x, int y) {
        // list of the coordinates of the cell's neighbors, represented by an array, where the 0th index is the
        // neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate
        List<int[]> neighborCoords = new ArrayList<>();

        // NE
        neighborCoords.add(new int[]{x + 1, y - 1});
        // SE
        neighborCoords.add(new int[]{x + 1, y + 1});
        // SW
        neighborCoords.add(new int[]{x - 1, y + 1});
        //NW
        neighborCoords.add(new int[]{x -1, y - 1});

        return validateNeighbors(neighborCoords);
    }

    /**
     * Takes a list possible neighbors and checks that they are in the grid (valid)
     * @param neighborCoords list of the coordinates of the cell's possible neighbors, represented by an array, where the
     *                       0th index is the neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate
     * @return map of valid neighbors where the key is the neighbor's coordinates (int array where 0th index is the
     * neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate) and the value is the state of the neighbor
     */
    private Map<int[], Integer> validateNeighbors(List<int[]> neighborCoords) {
        Map<int[], Integer> neighbors = new HashMap<>();

        for (int i = 0; i < neighborCoords.size(); i ++) {
            int[] neighbor = neighborCoords.get(i);
            int neighborX = neighbor[0];
            int neighborY = neighbor[1];
            if (!(neighborX < 0 || neighborX > gridSideSize || neighborY < 0 || neighborY > gridSideSize)) {
                neighbors.put(neighbor, grid[neighborX][neighborY]);
            }
        }

        return neighbors;
    }

    /**
     * Returns the grid for other classes to access
     * @return the simulation grid
     */
    public int[][] getGrid() {
        return grid;
    }

}
