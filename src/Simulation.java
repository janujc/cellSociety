import java.util.*;

/**
 * Superclass for all simulations
 * <p>
 * This class creates a simulation grid and declares functionality to update the grid.
 */
public abstract class Simulation {

    /*
     * Simulation grid made up of states, represented by ints
     * NOTE: grid is in (x, y) coordinate form, so the outer array represents the columns and the inner array represents
     * the element of each row in a particular column
     */
    protected int[][] grid;
    protected int gridSideSize;

    public Simulation(int sideSize, int[] states, double[] initialFreqs) {
        gridSideSize = sideSize;
        grid = new int[gridSideSize][gridSideSize];
        populateGrid(states, initialFreqs);
    }

    /**
     * Fills the grid with states, based off of the passed-in initial population frequencies (not population percentages)
     * @param states array of possible states
     * @param initialFreqs array of the initial frequencies of each state, in the same order as states
     */
    protected void populateGrid(int[] states, double[] initialFreqs) {
        Random rand = new Random();
        for (int i = 0; i < gridSideSize; i++) {
            for (int j = 0; j < gridSideSize; j++) {
                int randNum = rand.nextInt(100);
                for (int k = 0; k < states.length; k++) {
                    double sumPrevFreqs = sumPrevFreqs(Arrays.copyOfRange(initialFreqs, 0, k));
                    // uses Random apply the frequencies
                    if (randNum < 100 * (sumPrevFreqs + initialFreqs[k])) {
                        grid[i][j] = states[k];
                    }
                }
            }
        }
    }

    /**
     * Helper function for populateGrid() that sums the "previous" frequencies in the array
     * @param prevFreqs double array of the "previous" frequencies
     * @return sum of the previous frequencies
     */
    private double sumPrevFreqs (double[] prevFreqs) {
        double sum = 0;
        for (double d : prevFreqs) {
            sum += d;
        }
        return sum;
    }

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

        for (int[] neighbor : neighborCoords) {
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
