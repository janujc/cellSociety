package simulation;

import javafx.scene.paint.Color;
import utils.Cell;
import java.util.List;

/**
 * Class that represents the GameOfLife simulation
 * <p>
 * States: dead (0), alive (1)
 *
 * @author Januario Carreiro
 *
 */
public class GameOfLife extends Simulation {

    /**
     * The possible states of each cell in the GameOfLife simulation
     */
    private final int DEAD = 0;
    private final int ALIVE = 1;

    public GameOfLife(int sideSize, int[] states, double[] populationFreqs, Color[] colors) {
        super(sideSize, new int[]{0, 1}, populationFreqs, colors);
    }

    @Override
    protected void calculateNextStates() {
        /*
         * TODO: Change a cell's state according to a few rules:
         * Any LIVE cell with fewer than 2 neighbors DIES
         * Any LIVE cell with two or three neighbors LIVES
         * Any LIVE cell with more than 3 neighbors DIES
         * Any DEAD cell with more than 3 LIVE neighbors, becomes ALIVE.
         */

        for (Cell[] xCells : grid) {
            // TODO Is naming a variable after its type ok?
            for (Cell cell : xCells) {

                // Fire only looks at cardinal neighbors, so pass in true
                List<Cell> neighbors = getNeighborsOfType(cell, ALIVE, true);
                boolean hasBurningNeighbor = !neighbors.isEmpty();
                // Gonna comment out lines that prevent compilation
                // TODO: Fix
                //calculateNextStateOfOneCell(cell, hasBurningNeighbor);
            }
        }
    }
}
