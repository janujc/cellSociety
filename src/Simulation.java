import java.util.*;

// TODO Do I need to write the simulation rules in the comments?
// TODO Do I have too many comments?
/**
 * Superclass for all simulations
 * <p>
 * This class provides methods to create a simulation grid and functionality to update the grid.
 *
 * @author Jonathan Yu
 */
public abstract class Simulation {

    /**
     * The simulation grid made up of cells each with their own state (represented by an int)
     * <p>
     * NOTE: grid is in (x, y) coordinate form, so the outer array represents the columns and the inner array represents
     * the element of each row in a particular column.
     */
    protected final Cell[][] grid;

    /**
     * The length of one side of the grid
     * Grid is always a square, so its dimensions are gridSideSize x gridSideSize.
     */
    private final int gridSideSize;

    /**
     * Creates and populates the simulation grid
     * @param sideSize the length of one side of the grid
     * @param states the possible states of the cells in the simulation grid
     * @param populationFreqs the population frequencies of the states (not exact percentages)
     */
    protected Simulation(int sideSize, int[] states, double[] populationFreqs) {
        gridSideSize = sideSize;
        grid = new Cell[gridSideSize][gridSideSize];
        populateGrid(states, populationFreqs);
    }

    // TODO Is the implementation of frequencies and randomness ok? Or should it be absolute percentages?
    /**
     * Fills the grid with cells, with states based off of their population frequencies (not exact percentages)
     * @param states the possible states
     * @param populationFreqs the populations frequencies of each state the same order as the states parameter
     */
    private void populateGrid(int[] states, double[] populationFreqs) {
        Random rand = new Random();

        for (int x = 0; x < gridSideSize; x++) {
            for (int y = 0; y < gridSideSize; y++) {
                int randNum = rand.nextInt(100);
                double cumulativeFreqs = 0;
                for (int k = 0; k < states.length; k++) {
                    cumulativeFreqs += populationFreqs[k];
                    if (randNum < 100 * (cumulativeFreqs)) {
                        grid[x][y] = new Cell(states[k], x, y);
                        break;
                    }
                }
            }
        }
    }

    // TODO Is it okay to have a method just made up of two method calls? It makes sense logically but might be unnecessary.
    /**
     * Calculates the next state for each cell in the grid, then updates the grid. This represents a single step in the
     * simulation.
     */
    public void step() {
        calculateNextStates();
        updateStates();
    }

    /**
     * Calculates the next state of each cell in the grid (will be defined in subclasses to implement specific rules)
     */
    protected abstract void calculateNextStates();

    /**
     * Updates the state of each cell in the grid
     */
    private void updateStates() {
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                cell.updateState();
            }
        }
    }

    // TODO Is it better to leave getCornerNeighbors() separate or put it inside getAllNeighbors() since that's the only time it's needed?
    // TODO Is it better to have getAllNeighbors() and getCardinalNeighbors() as separate methods or should I use an extra parameter to differentiate between the two implementations in one method?
    /**
     * Gets the all neighbors (cardinal and corner) of a particular cell in the grid
     * @param center the cell whose neighbors are being retrieved
     * @return the list of cells that neighbor center
     */
    protected List<Cell> getAllNeighbors(Cell center) {
        List<Cell> neighbors = new ArrayList<>();

        // as the cardinal and corner neighbors are disjoint, no worry about double-counting
        neighbors.addAll(getCardinalNeighbors(center));
        neighbors.addAll(getCornerNeighbors(center));
        return neighbors;
    }

    /**
     * Gets the cardinal direction neighbors of a particular cell in the grid
     * @param center the cell whose neighbors are being retrieved
     * @return the list of cells that neighbor center in the cardinal directions
     */
    protected List<Cell> getCardinalNeighbors(Cell center) {

        /*
         * list of the coordinates of the cell's neighbors, represented by an array, where the 0th index is the
         * neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate
         */
        List<int[]> neighborCoords = new ArrayList<>();
        int centerX = center.getXCoord();
        int centerY = center.getYCoord();

        neighborCoords.add(new int[]{centerX, centerY - 1});    // North
        neighborCoords.add(new int[]{centerX + 1, centerY});    // East
        neighborCoords.add(new int[]{centerX, centerY + 1});    // South
        neighborCoords.add(new int[]{centerX - 1, centerY});    // West
        return validateNeighbors(neighborCoords);
    }

    // TODO Should this be protected in case some future simulation needs just the corners?
    /**
     * Gets the corner neighbors of a particular cell in the grid
     * @param center the cell whose neighbors are being retrieved
     * @return the list of cells that neighbor center at its corners
     */
    private List<Cell> getCornerNeighbors(Cell center) {

        /*
         * list of the coordinates of the cell's neighbors, represented by an array, where the 0th index is the
         * neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate
         */
        List<int[]> neighborCoords = new ArrayList<>();
        int centerX = center.getXCoord();
        int centerY = center.getYCoord();

        neighborCoords.add(new int[]{centerX + 1, centerY - 1});    //Northeast
        neighborCoords.add(new int[]{centerX + 1, centerY + 1});    //Southeast
        neighborCoords.add(new int[]{centerX - 1, centerY + 1});    //Southwest
        neighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
        return validateNeighbors(neighborCoords);
    }

    /**
     * Takes a list possible neighbors and checks that they are in the grid (valid)
     * @param neighborCoords the list of the coordinates of the cell's possible neighbors, represented by an array,
     *                       where the 0th index is the neighbor's x-coordinate and the 1st index is the neighbor's
     *                       y-coordinate
     * @return the list of cells that are valid neighbors
     */
    private List<Cell> validateNeighbors(List<int[]> neighborCoords) {
        List<Cell> neighbors = new ArrayList<>();

        for (int[] neighbor : neighborCoords) {
            int neighborX = neighbor[0];
            int neighborY = neighbor[1];
            if (!(neighborX < 0 || neighborX >= gridSideSize || neighborY < 0 || neighborY >= gridSideSize)) {
                neighbors.add(grid[neighborX][neighborY]);
            }
        }
        return neighbors;
    }

    // TODO Should this method be separate or built into the base getNeighbors method? The current implementation keeps the method parameters simple but adds another method and inefficiently gets neighbors of all type before checking the state.
    /**
     * Gets either all or just the cardinal neighbors of a cell that have a certain state
     * @param center the cell whose neighbors are being retrieved
     * @param type the desired state
     * @param onlyCardinal whether only the cardinal neighbors or all neighbors are retrieved
     * @return the list of cells that neighbor center and have the desired state
     */
    protected List<Cell> getNeighborsOfType(Cell center, int type, boolean onlyCardinal) {
        List<Cell> neighbors;
        List<Cell> neighborsOfType = new ArrayList<>();

        if (onlyCardinal) {
            neighbors =  getCardinalNeighbors(center);
        }
        else {
            neighbors = getAllNeighbors(center);
        }

        for (Cell neighbor : neighbors) {
            if (neighbor.getCurrState() == type) {
                neighborsOfType.add(neighbor);
            }
        }
        return neighborsOfType;
    }

    /**
     * Returns the grid for Visualizer to access
     * @return the simulation grid
     */
    public Cell[][] getGrid() {
        return grid;
    }
}
