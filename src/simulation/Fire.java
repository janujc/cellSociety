package simulation;

import javafx.scene.paint.Color;
import utils.Cell;

import java.util.List;
import java.util.Random;

/**
 * Class that represents the Spreading of Fire simulation
 * <p>
 * States: empty (0), tree (1), burning tree (2)
 *
 * @author Jonathan Yu
 */
public class Fire extends Simulation {

    /**
     * The possible states of each cell in the Fire simulation
     */
    private final int EMPTY = 0;
    private final int TREE = 1;
    private final int BURNING = 2;

    /**
     * The colors of each possible state in the Fire Simulation
     * <p>
     * Used to minimize array accesses
     */
    private final Color COLOR_EMPTY;
    private final Color COLOR_TREE;
    private final Color COLOR_BURNING;

    /**
     * The probability that a tree next to a burning tree catches on fire, which is read from the config file
     */
    private final double PROB_CATCH;

    /**
     * Used for applying PROB_CATCH. Implemented as an instance variable to avoid initializing multiple times in a short
     * time period, resulting in similar seeds.
     */
    private final Random rand;

    /**
     * Describes whether the sim has reached its end state (no burning trees left). True if it has, false otherwise.
     */
    private boolean simComplete;

    /**
     * Describes whether the sim will reach its end states after the current step. True if it will, false otherwise.
     */
    private boolean simWillEndAfterStep;

    /**
     * Creates the simulation and calls the super constructor to create the grid
     *
     * @param sideSize        the length of one side of the grid
     * @param states          the possible states of the cells in the simulation grid
     * @param populationFreqs the population frequencies of the states (probabilities, not proportions)
     * @param stateColors     the cell colors of each state in the simulation
     * @param probCatch       the probability that a tree next to a burning tree catches on fire, read from the config
     *                        file (passed in a String, so need to parse)
     */
    public Fire(int sideSize, Integer[] states, Double[] populationFreqs, Color[] stateColors, String probCatch) {
        super(sideSize, states, populationFreqs, stateColors);
        COLOR_EMPTY = colors[EMPTY];
        COLOR_TREE = colors[TREE];
        COLOR_BURNING = colors[BURNING];
        PROB_CATCH = Double.valueOf(probCatch);
        rand = new Random();
        simComplete = false;
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules and the PROB_CATCH value.
     * Afterwards, mark simulation as complete if it has been determined that the simulation will end after the current
     * step.
     */
    @Override
    protected void calculateNextStates() {

        // assume this is true at first and only determine if it's false while calculating (simplest implementation)
        simWillEndAfterStep = true;
        for (Cell[] column : grid) {
            for (Cell cell : column) {

                // if sim has reaches its end state, no need to calculate new states
                if (simComplete) {
                    int cellState = cell.getCurrState();
                    cell.setNextState(cellState, colors[cellState]);
                    continue;
                }

                // Fire only looks at cardinal neighbors, so pass in true
                List<Cell> neighbors = getNeighborsOfType(cell, BURNING, true);

                // determine this here to make calculateNextStateOfCurrCell() simpler
                boolean hasBurningNeighbor = !neighbors.isEmpty();
                calculateNextStateOfCurrCell(cell, hasBurningNeighbor);
            }
        }

        if (simWillEndAfterStep) {
            simComplete = true;
        }
    }

    /**
     * Calculates the next state for one cell in the grid
     * <p>
     * Also, if the simulation is set to end after the current step but a tree is calculated to catch fire, make it so
     * the simulation will not end
     *
     * @param currCell           the cell whose next state is being calculated
     * @param hasBurningNeighbor whether the cell has a burning tree as a neighbor or not
     */
    private void calculateNextStateOfCurrCell(Cell currCell, boolean hasBurningNeighbor) {
        int cellState = currCell.getCurrState();

        // if a tree neighbors a burning tree, it will catch fire with a probability of PROB_CATCH
        if (cellState == TREE && hasBurningNeighbor) {
            int randNum = rand.nextInt(100);
            if (randNum < PROB_CATCH * 100) {
                currCell.setNextState(BURNING, COLOR_BURNING);

                // only need one tree to catch fire for the simulation to not end after the current step
                if (simWillEndAfterStep) {
                    simWillEndAfterStep = false;
                }
            } else {
                currCell.setNextState(TREE, COLOR_TREE);
            }
        }

        // if a tree is burning, it will burn down (become empty cell)
        else if (cellState == BURNING) {
            currCell.setNextState(EMPTY, COLOR_EMPTY);
        }

        // otherwise, the cell remains the same (tree with no burning neighbors, empty cell)
        else {
            currCell.setNextState(cellState, colors[cellState]);
        }
    }
}
