package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Superclass for all simulations, which provides methods to create a simulation grid and update it
 * <p>
 * To create a new simulation, one must extend this class and override the abstract method calculateNextStates(). The
 * overridden method is where the specific simulation rules are implemented. Additionally, one must implement subclass
 * constructors that call the appropriate superclass constructor before initializing the simulation-specific variables.
 * NOTE: All simulations must allow for -1 to be used as the value for the UNDETERMINED state and cannot use it for any
 * other state.
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
     * The string values to represent each method of populating the grid
     */
    public static String RANDOM_TYPE = "random";
    public static String FREQUENCIES = "freqs";
    public static String NUM_OCCUPY = "numToOccupy";
    public static String LIST_BASED = "list";

    /**
     * The possible states of each cell in the simulation
     */
    protected final Integer[] states;

    /**
     * The simulation grid made up of cells each with their own state (represented by an int)
     */
    final Grid myGrid;

    /**
     * The dimensions of the grid
     */
    final int gridNumRows;
    final int gridNumCols;

    /**
     * The colors for each possible state where the index corresponds to the states array
     */
    final Color[] colors;

    /**
     * Used for random number generation. Implemented as an instance variable to avoid initializing multiple times in a
     * short time period, resulting in similar seeds.
     */
    final Random rand;

    /**
     * The name of the simulation currently being run (ex: Fire, PredatorPrey, etc.)
     */
    private String displayName;

    /**
     * The name of the configuration file for the current simulation
     */
    private String currentFileName;

    /**
     * The simulation-specific data (ex: probCatch in Fire)
     */
    private String metadata;

    /**
     * Initializes instance variables needed for all simulations regardless of type and populating method (the grid,
     * list of states, list of state colors)
     *
     * @param grid        the simulation grid
     * @param simStates   the possible states of the cells in the simulation grid
     * @param stateColors the cell colors of each state in the simulation
     */
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors) {
        myGrid = grid;
        gridNumRows = myGrid.getNumRows();
        gridNumCols = myGrid.getNumCols();
        states = simStates;
        colors = stateColors;
        rand = new Random();
    }

    /**
     * Initializes instance variables and populates the grid using the desired method
     *
     * @param grid           the simulation grid
     * @param simStates      the possible states of the cells in the simulation grid
     * @param stateColors    the cell colors of each state in the simulation
     * @param populatingType the string representing the desired method of populating the grid
     * @param populatingInfo the data for populating the grid using the method indicated by populatingType
     */
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType,
                         Object populatingInfo) {
        this(grid, simStates, stateColors);
        populateGrid(populatingType, populatingInfo);
    }

    /**
     * Populates the grid using the desired method
     *
     * @param populatingType the string representing the desired method of populating the grid
     * @param populatingInfo the data for populating the grid using the method indicated by populatingType
     */
    private void populateGrid(String populatingType, Object populatingInfo) {
        final String POPULATION_LIST = "list";
        final String POPULATION_SET_NUMBERS = "numToOccupy";
        final String POPULATION_FREQS = "freqs";

        switch (populatingType) {
            case POPULATION_LIST:
                myGrid.populate(colors, (Integer[][]) populatingInfo);
                break;
            case POPULATION_SET_NUMBERS:
                myGrid.populate(states, colors, (Integer[]) populatingInfo);
                break;
            case POPULATION_FREQS:
                myGrid.populate(states, colors, (Double[]) populatingInfo);
                break;
            default:

                // populate randomly by default
                myGrid.populate(states, colors);
                break;
        }
    }

    /**
     * Calculates the next state for each cell in the grid, then updates the grid. This represents a single step in the
     * simulation.
     */
    public void step() {
        calculateNextStates();
        myGrid.updateStates();
    }

    /**
     * Calculates the next state of each cell in the grid (will be defined in subclasses to implement specific rules)
     */
    protected abstract void calculateNextStates();

    /**
     * Rotates or increments the state of the cell at a certain location in the grid
     * <p>
     * Called by the visualizer to allow users to dynamically change the state of a cell by clicking on it
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    public void rotateState(int x, int y) {
        Cell currCell = myGrid.getCellAt(x, y);
        int currState = currCell.getCurrState();
        int newState = (currState + 1) % states.length;

        currCell.setState(newState, colors[newState]);
    }

    /**
     * Helper method that gets all of the cells in the grid in random order
     * <p>
     * Used to create less predictable simulations as animals can affect each others' behavior within the same step
     *
     * @return the list of cells in random order
     */
    List<Cell> getCellsInRandomOrder() {
        List<Cell> allCells = new ArrayList<>();

        for (int x = 0; x < gridNumCols; x++) {
            for (int y = 0; y < gridNumRows; y++)
                allCells.add(myGrid.getCellAt(x, y));
        }
        Collections.shuffle(allCells, rand);
        return allCells;
    }

    /**
     * Helper method that gets the most recently set state of a cell. If the cell's next state has already been set,
     * returns that. Otherwise, returns the current state.
     *
     * @param cell the cell whose state is desired
     * @return the most recently set state of the cell
     */
    int getMostRecentState(Cell cell) {
        int nextState = cell.getNextState();

        return (nextState == UNDETERMINED) ? cell.getCurrState() : nextState;
    }

    /**
     * Helper method that randomly chooses a cell from a given list
     *
     * @param chooseFrom the list of cells to choose from
     * @return the randomly chosen cell
     */
    Cell chooseRandomCellFromList(List<Cell> chooseFrom) {
        return chooseFrom.get(rand.nextInt(chooseFrom.size()));
    }

    /**
     * Gets the Grid object associated with the simulation for the visualizer to access
     *
     * @return the grid
     */
    public Grid getGrid() {
        return myGrid;
    }

    /**
     * Gets the state colors of the simulation for the visualizer to access
     *
     * @return the array of colors
     */
    public Color[] getColors() {
        return colors;
    }

    /**
     * Gets the name of the simulation currently being run
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Stores the name of the simulation currently being run
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the name of the configuration file for the current simulation
     *
     * @return the file name
     */
    public String getCurrentFileName() {
        return currentFileName;
    }

    /**
     * Stores the name of the configuration file for the current simulation
     *
     * @param fileName name of the configuration file
     */
    public void setCurrentFileName(String fileName) {
        this.currentFileName = fileName;
    }

    /**
     * Gets the simulation-specific data/values for the visualizer to access
     *
     * @return the simulation data
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * Stores the simulation-specific data, which the visualizer will need to access
     *
     * @param metadata the simulation data
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
