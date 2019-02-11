package simulation;

import grid.Grid;
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
    private final int EMPTY;
    private final int TREE;
    private final int BURNING;

    /**
     * The probability that a tree next to a burning tree catches on fire, which is read from the config file
     */
    private final double PROB_CATCH;

    /**
     * Creates the simulation and calls the super constructor to create the grid
     *
     * @param grid            the simulation grid
     * @param simStates       the possible states of the cells in the simulation grid
     * @param stateColors     the cell colors of each state in the simulation
     * @param populationFreqs the population frequencies of the states (probabilities, not proportions)
     * @param probCatch       the probability that a tree next to a burning tree catches on fire, read from the config
     *                        file (passed in a String, so need to parse)
     */
    public Fire(Grid grid, Integer[] simStates, Color[] stateColors, Double[] populationFreqs, String probCatch) {
        super(grid, simStates, stateColors, populationFreqs);
        EMPTY = states[0];
        TREE = states[1];
        BURNING = states[2];
        PROB_CATCH = Double.valueOf(probCatch);
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules and the PROB_CATCH value.
     * Afterwards, mark simulation as complete if it has been determined that the simulation will end after the current
     * step.
     */
    @Override
    protected void calculateNextStates() {
        for (Cell[] column : myCells) {
            for (Cell cell : column) {
                int cellState = calculateNextStateOfCurrCell(cell);
                cell.setNextState(cellState, colors[cellState]);
            }
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
        List<Cell> burningNeighbors = myGrid.getNeighborsOfType(tree, BURNING, true);

        if (!burningNeighbors.isEmpty()) {
            int randNum = rand.nextInt(100);
            if (randNum < PROB_CATCH * 100) {
                return BURNING;
            }
        }
        return TREE;
    }
}
