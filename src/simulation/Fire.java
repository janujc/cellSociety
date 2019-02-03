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

    // TODO EMPTY and TREE can be local, but it's more readable to initialize here. Is that ok?
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
     * The probability that a tree next to a burning tree catches on fire, which is read from the XML file
     */
    private final double PROB_CATCH;

    /**
     * Creates the simulation and calls the super constructor to create the grid
     * @param sideSize the length of one side of the grid
     * @param populationFreqs the population frequencies of the states (not exact percentages)
     * @param probCatch the probability that a tree next to a burning tree catches on fire, read from the XML file
     */
    public Fire(int sideSize, Integer[] states, Double[] populationFreqs, Color[] stateColors, String probCatch) {
        super(sideSize, states, populationFreqs, stateColors, Double.valueOf(probCatch));
        COLOR_EMPTY = colors[EMPTY];
        COLOR_TREE = colors[TREE];
        COLOR_BURNING = colors[BURNING];
        PROB_CATCH = Double.valueOf(probCatch);
    }

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules and the PROB_CATCH value
     */
    @Override
    protected void calculateNextStates() {
        for (Cell[] xCells : grid) {
            // TODO Is naming a variable after its type ok?
            for (Cell cell : xCells) {

                // Fire only looks at cardinal neighbors, so pass in true
                List<Cell> neighbors = getNeighborsOfType(cell, BURNING, true);
                boolean hasBurningNeighbor = !neighbors.isEmpty();
                calculateNextStateOfOneCell(cell, hasBurningNeighbor);
            }
        }
    }

    /**
     * Calculates the next state for one cell in the grid
     * @param cell the cell whose next state is being calculated
     * @param hasBurningNeighbor whether the cell has a burning tree as a neighbor or not
     */
    protected void calculateNextStateOfOneCell(Cell cell, boolean hasBurningNeighbor) {
        Random rand = new Random();

        // if a tree neighbors a burning tree, it will catch fire with a probability of PROB_CATCH
        if (cell.getCurrState() == TREE && hasBurningNeighbor) {
            int randNum = rand.nextInt(100);
            if (randNum < PROB_CATCH * 100) {
                cell.setNextState(BURNING, COLOR_BURNING);
            }
            else {
                cell.setNextState(TREE, COLOR_TREE);
            }
        }

        // if a tree is burning, it will burn down (become empty cell)
        else if (cell.getCurrState() == BURNING) {
            cell.setNextState(EMPTY, COLOR_EMPTY);
        }

        // otherwise, the cell remains the same (tree with no burning neighbors, empty cell)
        else {
            int curr = cell.getCurrState();
            cell.setNextState(curr, colors[curr]);
        }
    }
}
