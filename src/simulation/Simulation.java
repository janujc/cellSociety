package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.Random;

/**
 * Superclass for all simulations
 * <p>
 * This class provides methods to create a simulation grid and update it
 *
 * @author Jonathan Yu
 */
public abstract class Simulation {

    public static String RANDOM_TYPE = "random";
    public static String FREQUENCIES = "freqs";
    public static String NUM_OCCUPY  = "numToOccupy";
    public static String LIST_BASED  = "list";

    /**
     * The value for a cell's next state that has not been set (after initialization and each step). All simulations
     * must not use this value for other states.
     */
    static final int UNDETERMINED = -1;

    /**
     * The possible states of each cell in the simulation
     */
    protected final Integer[] states;

    /**
     * The simulation grid made up of cells each with their own state (represented by an int)
     */
    final Grid myGrid;

    // TODO
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

    // TODO
    private String metadata;

    /**
     * Initializes instance variables
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

    // TODO
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType,
                         Object populatingInfo) {
        this(grid, simStates, stateColors);
        populateGrid(populatingType, populatingInfo);
    }

    /**
     * Creates and populates the simulation grid based off a list of specific locations and states
     *
     * @param grid        the simulation grid
     * @param simStates   the possible states of the cells in the simulation grid
     * @param stateColors the cell colors of each state in the simulation
     * @param cells       the 2D array with the specified states, where the indices correspond to the grid
     */
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors, Integer[][] cells) {
        this(grid, simStates, stateColors);
        myGrid.populate(colors, cells);
    }

    // TODO If the above constructor works, remove the rest of the constructors below

    /**
     * Creates and populates the simulation grid randomly
     *
     * @param grid        the simulation grid
     * @param simStates   the possible states of the cells in the simulation grid
     * @param stateColors the cell colors of each state in the simulation
     * @param random      true if the grid should be populated with equal probabilities of each state, false otherwise
     */
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors, boolean random) {
        this(grid, simStates, stateColors);
        if (random) {
            myGrid.populate(states, colors);
        }
    }

    /**
     * Creates and populates the simulation grid randomly with specified numbers of each state
     *
     * @param grid        the simulation grid
     * @param simStates   the possible states of the cells in the simulation grid
     * @param stateColors the cell colors of each state in the simulation
     * @param numToOccupy the number of cells each state must occupy
     */
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors, Integer[] numToOccupy) {
        this(grid, simStates, stateColors);
        myGrid.populate(states, colors, numToOccupy);
    }

    /**
     * Creates and populates the simulation grid based on population frequencies.
     *
     * @param grid            the simulation grid
     * @param simStates       the possible states of the cells in the simulation grid
     * @param stateColors     the cell colors of each state in the simulation
     * @param populationFreqs the population frequencies of the states (probabilities, not proportions)
     */
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors, Double[] populationFreqs) {
        this(grid, simStates, stateColors);
        myGrid.populate(states, colors, populationFreqs);
    }

    // TODO
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

    // TODO
    public void rotateState(int x, int y) {
        Cell currCell = myGrid.getCellAt(x, y);
        int currState = currCell.getCurrState();
        int newState = (currState + 1) % states.length;

        currCell.setState(newState, colors[newState]);
    }

    // TODO
    public Grid getGrid() {
        return myGrid;
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

    // TODO
    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    // TODO
    public Color[] getColors() {
        return colors;
    }
}
