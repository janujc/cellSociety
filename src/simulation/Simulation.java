package simulation;

import grid.Grid;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Superclass for all simulations
 * <p>
 * This class provides methods to create a simulation grid and update it
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

    /**
     * The possible states of each cell in the simulation
     */
    protected final Integer[] states;

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
     * Initializes instance variables
     *
     * @param grid        the simulation grid
     * @param simStates   the possible states of the cells in the simulation grid
     * @param stateColors the cell colors of each state in the simulation
     */
    protected Simulation(Grid grid, Integer[] simStates, Color[] stateColors) {
        myGrid = grid;
        states = simStates;
        colors = stateColors;
        rand = new Random();
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
        myGrid.populate(states, colors, cells);
    }

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
}
