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
    private final Integer DEAD;
    private final Integer ALIVE;

    /**
     * Creates the simulation by calling super and using the constructor in simulation.Simulation
     * <p>
     * In order to create a new Simulation, we must first create a Grid object that has to be passed to the Simulation
     * constructor. If we want to create a GOL simulation with a triangular grid where the dead state is black and the
     * alive state is white, with predetermined frequencies, we would write:
     *
     * Simulation GOL = new GameOfLife(new Triangular(parameters), new Integer[]{0, 1}, new Color[]{Color.BLACK, Color.WHITE},
     *                                 "frequencies", new Double[]{0.5, 0.5}, NULL);
     *
     * The last parameter is NULL because GOL requires no metadata, while some other Simulation subclasses need more
     * information (such as Predator-Prey).
     * <p>
     * For the GOL simulation, there are only two possible states, dead and alive. When choosing a grid length,
     * it is recommended that the number be between 25 and 100 so that it is easier to follow each step.
     *
     * @param grid              the simulation grid
     * @param simStates         the possible states of the cells in the simulation grid
     * @param stateColors       the cell colors of each state in the simulation
     * @param populatingType    designates how the grid should be populated (with a list, randomly, with set numbers of
     *                          each state, based on frequencies)
     * @param populatingInfo    the data needed to populate the grid based on populatingType
     * @param metadata          any other information that might be needed for the simulation. In this case, null.
     */
    public GameOfLife(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType, Object populatingInfo,
                      String metadata) {
        super(grid, simStates, stateColors, populatingType, populatingInfo);
        DEAD = states[0];
        ALIVE = states[1];
    }

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
