package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Superclass for all simulations
 * <p>
 * This class provides methods to create a simulation grid and update it.
 *
 * @author Jonathan Yu
 */
public abstract class Simulation {

    /**
     * The value for a cell's next state that has not been set (after initialization and each step). All simulations
     * must not use this value for other states.
     */
    static final int UNDETERMINED = -1;

    /**
     * The simulation grid made up of cells each with their own state (represented by an int)
     */
    protected final Grid myGrid;
    protected final Cell[][] grid;

    protected String metadata;

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    // TODO Move this to the Grid class
    /**
     * The length of one side of the grid
     * <p>
     * Grid is always a square, so its dimensions are gridSideSize x gridSideSize
     */
    final int gridSideSize;

    /**
     * The array of colors for each possible state where the index is the corresponding state
     */
    final Color[] colors;

    public Color[] getColors() {
        return colors;
    }

    /**
     * Used for random number generation. Implemented as an instance variable to avoid initializing multiple times in a
     * short time period, resulting in similar seeds.
     */
    final Random rand;

    // TODO Move this to the Visualizer class
    private String currentFileName, displayName;

    // TODO Make constructors for the different grid types

    /**
     * Initializes instance variables
     *
     * @param sideSize    the length of one side of the grid
     * @param stateColors the cell colors of each state in the simulation
     */
    protected Simulation(int sideSize, Integer[] states, Color[] stateColors) {
        gridSideSize = sideSize;
        colors = stateColors;
        myGrid = new Grid(gridSideSize, states, colors);
        grid = myGrid.getMyGrid();
        rand = new Random();
    }

    /**
     * Creates and populates the simulation grid based off a list of specific locations and states
     *
     * @param sideSize    the length of one side of the grid
     * @param states      the possible states of the cells in the simulation grid
     * @param stateColors the cell colors of each state in the simulation
     * @param cells       the 2D array with the specified states, where the indices correspond to the grid
     */
    protected Simulation(int sideSize, Integer[] states, Color[] stateColors, Integer[][] cells) {
        this(sideSize, states, stateColors);
        myGrid.populateGrid(cells);
    }

    /**
     * Creates and populates the simulation grid randomly
     *
     * @param sideSize    the length of one side of the grid
     * @param states      the possible states of the cells in the simulation grid
     * @param stateColors the cell colors of each state in the simulation
     */
    /*
    protected Simulation(int sideSize, Integer[] states, Color[] stateColors) {
        this(sideSize, states, stateColors);
        myGrid = new Grid(states, colors);
    }*/

    /**
     * Creates and populates the simulation grid randomly with specified numbers of each state
     *
     * @param sideSize    the length of one side of the grid
     * @param states      the possible states of the cells in the simulation grid
     * @param numToOccupy the number of cells each state must occupy
     * @param stateColors the cell colors of each state in the simulation
     */
    protected Simulation(int sideSize, Integer[] states, Integer[] numToOccupy, Color[] stateColors) {
        this(sideSize, states, stateColors);
        myGrid.populateGrid(states);
    }

    /**
     * Creates and populates the simulation grid randomly based on population frequencies.
     *
     * @param sideSize        the length of one side of the grid
     * @param states          the possible states of the cells in the simulation grid
     * @param populationFreqs the population frequencies of the states (probabilities, not proportions)
     * @param stateColors     the cell colors of each state in the simulation
     */
    protected Simulation(int sideSize, Integer[] states, Double[] populationFreqs, Color[] stateColors) {
        this(sideSize, states, stateColors);
        myGrid.populateGrid(states, populationFreqs);
    }

    /**
     * Returns the filename of the currently running simulation configuration
     *
     * @return
     */
    public String getCurrentFileName() {
        return currentFileName;
    }

    /**
     * Stores the name of the configuration file currently being used
     *
     * @param fileName name of the configuration file
     */
    public void setCurrentFileName(String fileName) {
        this.currentFileName = fileName;
    }

    /**
     * Returns the simulation's display name
     *
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Stores the simulation's display name
     *
     * @param fileName name of the configuration file
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    // TODO Move this to the Grid class
    /**
     * Fills the grid with cells, with states based off of their population frequencies (probabilities, not proportions)
     *
     * @param states          the possible states
     * @param populationFreqs the populations frequencies of each state in the same order as the states parameter
     */
    private void populateGrid(Integer[] states, Double[] populationFreqs) {
        for (int x = 0; x < gridSideSize; x++) {
            for (int y = 0; y < gridSideSize; y++) {
                int initialState = applyPopulationFreqs(states, populationFreqs);
                grid[x][y] = new Cell(states[initialState], x, y, colors[initialState]);
            }
        }
    }

    // TODO Move this to the Grid class
    /**
     * Determines the initial state of a cell based off of the population frequencies
     *
     * @param states          the possible states
     * @param populationFreqs the population frequencies of each state in the same order as the states parameter
     * @return the initial state of the cell
     */
    private int applyPopulationFreqs(Integer[] states, Double[] populationFreqs) {
        int randNum = rand.nextInt(100);
        double cumulativeFreqs = 0;

        for (int i = 0; i < states.length; i++) {
            cumulativeFreqs += populationFreqs[i];
            if (randNum < 100 * cumulativeFreqs) {
                return states[i];
            }
        }

        return -1;    // will never get here as the population frequencies must sum to 1.0
    }

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

    // TODO Move this to the Grid class
    /**
     * Updates the state of each cell in the grid
     */
    private void updateStates() {
        for (Cell[] column : grid) {
            for (Cell cell : column) {
                cell.updateState();
            }
        }
    }

    // TODO Move all the neighbors stuff to the Grid class

    /**
     * Gets all neighbors (cardinal and corner) of a particular cell in the grid
     * <p>
     * Access type is protected in case future simulations not currently implemented need all neighbors
     *
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
     *
     * @param center the cell whose neighbors are being retrieved
     * @return the list of cells that neighbor center in the cardinal directions
     */
    List<Cell> getCardinalNeighbors(Cell center) {

        /*
         * list of the coordinates of the cell's neighbors, represented by an array, where the 0th index is the
         * neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate
         */
        List<int[]> neighborCoords = new ArrayList<>();
        int centerX = center.getCol();
        int centerY = center.getRow();

        neighborCoords.add(new int[]{centerX, centerY - 1});    // North
        neighborCoords.add(new int[]{centerX + 1, centerY});    // East
        neighborCoords.add(new int[]{centerX, centerY + 1});    // South
        neighborCoords.add(new int[]{centerX - 1, centerY});    // West
        return validateNeighbors(neighborCoords);
    }

    /**
     * Gets the corner neighbors of a particular cell in the grid
     * <p>
     * Access type is protected in case future simulations not currently implemented need just the corner neighbors
     *
     * @param center the cell whose neighbors are being retrieved
     * @return the list of cells that neighbor center at its corners
     */
    protected List<Cell> getCornerNeighbors(Cell center) {

        /*
         * list of the coordinates of the cell's neighbors, represented by an array, where the 0th index is the
         * neighbor's x-coordinate and the 1st index is the neighbor's y-coordinate
         */
        List<int[]> neighborCoords = new ArrayList<>();
        int centerX = center.getCol();
        int centerY = center.getRow();

        neighborCoords.add(new int[]{centerX + 1, centerY - 1});    // Northeast
        neighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
        neighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest
        neighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
        return validateNeighbors(neighborCoords);
    }

    /**
     * Takes a list possible neighbors and checks that they are in the grid (valid)
     *
     * @param neighborCoords the list of the coordinates of the cell's possible neighbors, represented by an array,
     *                       where the 0th index is the neighbor's x-coordinate and the 1st index is the neighbor's
     *                       y-coordinate
     * @return the list of cells, with coordinates in neighborCoords, that are valid neighbors
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

    /**
     * Gets either all or just the cardinal neighbors of a cell that have a certain state
     *
     * @param center       the cell whose neighbors are being retrieved
     * @param type         the desired state
     * @param onlyCardinal whether only the cardinal neighbors or all neighbors are retrieved
     * @return the list of cells that neighbor center and have the desired state
     */
    List<Cell> getNeighborsOfType(Cell center, int type, boolean onlyCardinal) {
        List<Cell> neighbors;
        List<Cell> neighborsOfType = new ArrayList<>();

        if (onlyCardinal) {
            neighbors = getCardinalNeighbors(center);
        } else {
            neighbors = getAllNeighbors(center);
        }

        for (Cell neighbor : neighbors) {
            if (neighbor.getCurrState() == type) {
                neighborsOfType.add(neighbor);
            }
        }
        return neighborsOfType;
    }

    // TODO Move this to the Grid class
    /**
     * Returns the grid for Visualizer to access
     *
     * @return the simulation grid
     */
    public Cell[][] getGrid() {
        return grid;
    }

    // TODO Move this to the Grid class
    public void rotateState(int i, int j) {
        int numStates = colors.length;
        int currentState = grid[i][j].getCurrState();
        grid[i][j].setState((currentState + 1) % numStates, colors[(currentState + 1) % numStates]);
    }
}
