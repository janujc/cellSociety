package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

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
    private final Integer DEAD;
    private final Integer ALIVE;

    /**
     * Creates the simulation by calling super and using the constructor in simulation.Simulation
     * <p>
     * In order to create a 50x50 simulation where dead cells are green and make up half of the cells, and alive cells
     * are pink and make up the other half of the cells, we would use:
     * Simulation GOL = new GameOfLife(50, new Integer[] {0, 1}, new Double[] {0.5, 0.5}, new Color[] {Color.GREEN,
     *                  Color.PINK}, null);
     * <p>
     * For the Game of Life simulation, there are only two possible states, dead and alive. When choosing a grid length,
     * it is recommended that the number be between 25 and 100 so that it is easier to follow each step.
     *
     * @param grid           the simulation grid
     * @param simStates      the possible states of the cells in the simulation grid
     * @param stateColors    the cell colors of each state in the simulation
     * @param populatingType designates how the grid should be populated (with a list, randomly, with set numbers of each state, based on frequencies)
     * @param populatingInfo the data needed to populate the grid based on populatingType
     * @param metadata any other information that might be needed for the simulation. In this case, null.
     */
    public GameOfLife(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType, Object populatingInfo,
                      String metadata) {
        super(grid, simStates, stateColors, populatingType, populatingInfo);
        DEAD = states[0];
        ALIVE = states[1];
    }

    /**
     * Calculates the next state for each cell in the grid based on the number of alive neighbors of each cell.
     */
    @Override
    protected void calculateNextStates() {
        for (int x = 0; x < gridNumCols; x++) {
            for (int y = 0; y < gridNumRows; y++) {
                Cell currCell = myGrid.getCellAt(x, y);
                int numOfAliveNeighbors = myGrid.getNeighborsOfType(currCell, ALIVE, false).size();
                calculateNextStateOfCurrCell(currCell, numOfAliveNeighbors);
            }
        }
    }

    /**
     * Calculates the next state of one cell in the grid based on how many alive neighbors it has. There are 5 rules:
     *      1. If Cell is ALIVE and has FEWER than 2 ALIVE neighbors, its next state is DEAD
     *      2. If Cell is ALIVE and has 2 or 3 ALIVE neighbors, its next state is ALIVE
     *      3. If Cell is ALIVE and has MORE than 3 ALIVE neighbors, its next state is DEAD
     *      4. If Cell is DEAD and has EXACTLY 3 ALIVE neighbors, its next state is ALIVE
     *      5. If Cell is DEAD and DOES NOT HAVE 3 AlIVE neighbors, its next state is DEAD
     *
     * @param cell current Cell object
     * @param numOfAliveNeighbors number of neighbors whose current state is ALIVE
     */
    private void calculateNextStateOfCurrCell(Cell cell, int numOfAliveNeighbors) {
        if (cell.getCurrState() == ALIVE && numOfAliveNeighbors < 2) {
            cell.setNextState(DEAD, colors[DEAD]);
        } else if (cell.getCurrState() == ALIVE && (numOfAliveNeighbors == 2 || numOfAliveNeighbors == 3)) {
            cell.setNextState(ALIVE, colors[ALIVE]);
        } else if (cell.getCurrState() == ALIVE && numOfAliveNeighbors > 3) {
            cell.setNextState(DEAD, colors[DEAD]);
        } else if (cell.getCurrState() == DEAD && numOfAliveNeighbors == 3) {
            cell.setNextState(ALIVE, colors[ALIVE]);
        } else {
            cell.setNextState(cell.getCurrState(), cell.getCurrColor());
        }
    }
}
