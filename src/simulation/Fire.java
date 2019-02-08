package simulation;

import javafx.scene.paint.Color;
import utils.Cell;

import java.util.List;

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
    private static final int EMPTY = 0;
    private static final int TREE = 1;
    private static final int BURNING = 2;

    /**
     * The probability that a tree next to a burning tree catches on fire, which is read from the config file
     */
    private final double PROB_CATCH;

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
        PROB_CATCH = Double.valueOf(probCatch);
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
                int cellState;

                // if sim has reaches its end state, no need to calculate new states
                if (simComplete) {
                    cellState = cell.getCurrState();
                } else {
                    cellState = calculateNextStateOfCurrCell(cell);
                }
                cell.setNextState(cellState, colors[cellState]);
            }
        }

        if (simWillEndAfterStep) {
            simComplete = true;
        }
    }

    /**
     * Calculates the next state for one cell in the grid
     *
     * @param currCell the cell whose next state is being calculated
     * @return the next state of the cell
     */
    private int calculateNextStateOfCurrCell(Cell currCell) {
        int cellState = currCell.getCurrState();

        if (cellState == TREE) {
            return calculateTreeNextState(currCell);
        }

        // burning trees burn down, and empty cells stay empty
        else {
            return EMPTY;
        }
    }

    /**
     * Determines the next state of a cell containing a tree. If the tree neighbors a burning tree, it will catch fire
     * with a probability of PROB_CATCH. Otherwise, the tree is unaffected.
     * <p>
     * Also, if the simulation is set to end after the current step but a tree is calculated to catch fire, make it so
     * the simulation will not end.
     *
     * @param tree the tree whose next state is being calculated
     * @return the next state of the cell containing the tree
     */
    private int calculateTreeNextState(Cell tree) {
        List<Cell> burningNeighbors = getNeighborsOfType(tree, BURNING, true);

        if (!burningNeighbors.isEmpty()) {
            int randNum = rand.nextInt(100);
            if (randNum < PROB_CATCH * 100) {

                // only need one tree to catch fire for the simulation to not end after the current step
                if (simWillEndAfterStep) {
                    simWillEndAfterStep = false;
                }
                return BURNING;
            }
        }
        return TREE;
    }
}
