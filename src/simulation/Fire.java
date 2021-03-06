package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.List;

/**
 * Class that represents the Spreading of Fire simulation
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
     * @param grid           the simulation grid
     * @param simStates      the possible states of the cells in the simulation grid
     * @param stateColors    the cell colors of each state in the simulation
     * @param populatingType designates how the grid should be populated (with a list, randomly, with set numbers of
     *                       each state, based on frequencies)
     * @param populatingInfo the data needed to populate the grid based on populatingType
     * @param probCatch      the probability that a tree next to a burning tree catches on fire, read from the config
     *                       file (passed in a String, so need to parse)
     */
    public Fire(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType, Object populatingInfo,
                String probCatch) {
        super(grid, simStates, stateColors, populatingType, populatingInfo);
        EMPTY = states[0];
        TREE = states[1];
        BURNING = states[2];
        PROB_CATCH = Double.valueOf(probCatch);
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules
     */
    @Override
    protected void calculateNextStates() {
        for (int x = 0; x < gridNumCols; x++) {
            for (int y = 0; y < gridNumRows; y++) {
                Cell currCell = myGrid.getCellAt(x, y);
                int cellState = calculateNextStateOfCurrCell(currCell);
                currCell.setNextState(cellState, colors[cellState]);
            }
        }
    }

    /**
     * Calculates the next state for one cell in the grid
     * <p>
     * Simulation rules: Empty spaces stay empty. Burning trees burn down in one turn and become empty space. Trees that
     * neighbor burning trees catch fire with a probability of PROB_CATCH.
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
