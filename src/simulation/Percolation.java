package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.Random;

/**
 * Class that represents the simulation.Percolation simulation.
 * <p>
 * States: blocked(0), open(1), percolated(1)
 *
 * @author Januario Carreiro
 */
public class Percolation extends Simulation {
    private final Integer BLOCKED;
    private final Integer OPEN;
    private final Integer PERCOLATED;

    /**
     * Creates the simulation by calling super and using the constructor in simulation.Simulation
     * <p>
     * In order to create a new Simulation, we must first create a Grid object that has to be passed to the Simulation
     * constructor. If we want to create a Percolation simulation with a square grid where the BLOCKED state is black,
     * the OPEN state is white, and the PERCOLATED state is blue, with predetermined frequencies, we would write:
     *
     * Simulation perc = new Percolation(new Square(parameters), new Integer[]{0, 1, 2},
     *                                 new Color[]{Color.BLACK, Color.WHITE, Color.BLUE}, "frequencies",
     *                                 new Double[]{0.4, 0.6, 0.0}, NULL);
     *
     * The last parameter is NULL because Percolation requires no metadata, while some other Simulation subclasses need more
     * information (such as Predator-Prey).
     * <p>
     * For the Percolation simulation, it is customary to generate a world with a 0.0 chance of creating a PERCOLATED
     * Cell and having the constructor randomly generate a single PERCOLATED Cell. When creating a grid, it is
     * recommended that the side length be between 25 and 100.
     *
     * @param grid              the simulation grid
     * @param simStates         the possible states of the cells in the simulation grid
     * @param stateColors       the cell colors of each state in the simulation
     * @param populatingType    designates how the grid should be populated (with a list, randomly, with set numbers of
     *                          each state, based on frequencies)
     * @param populatingInfo    the data needed to populate the grid based on populatingType
     * @param metadata          any other information that might be needed for the simulation. In this case, null.
     */
    public Percolation(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType, Object populatingInfo,
                       String metadata) {
        super(grid, simStates, stateColors, populatingType, populatingInfo);
        BLOCKED = states[0];
        OPEN = states[1];
        PERCOLATED = states[2];
        Cell temp = determineStartLocation();
        Cell startPerc = myGrid.getCellAt(temp.getCol(), temp.getRow());
        startPerc.setState(PERCOLATED, colors[PERCOLATED]);
    }

    private Cell determineStartLocation() {
        Random rand = new Random();
        int side = rand.nextInt(4);
        int coord1, coord2;
        if (side == 0) {
            coord1 = 0;
            coord2 = rand.nextInt(gridNumRows);
        } else if (side == 1) {
            coord1 = gridNumCols - 1;
            coord2 = rand.nextInt(gridNumRows);
        } else if (side == 2) {
            coord1 = rand.nextInt(gridNumCols);
            coord2 = 0;
        } else {
            coord1 = rand.nextInt(gridNumCols);
            coord2 = gridNumRows - 1;
        }
        return new Cell(PERCOLATED, coord1, coord2, colors[PERCOLATED]);
    }

    @Override
    protected void calculateNextStates() {
        for (int x = 0; x < gridNumCols; x++) {
            for (int y = 0; y < gridNumRows; y++) {
                Cell currCell = myGrid.getCellAt(x, y);
                Boolean hasPercolatedNeighbors = !(myGrid.getNeighborsOfType(currCell, PERCOLATED, false).isEmpty());
                calculateNextStateOfCurrCell(currCell, hasPercolatedNeighbors);
            }
        }
    }

    private void calculateNextStateOfCurrCell(Cell cell, Boolean hasPercolatedNeighbors) {
        if (cell.getCurrState() == BLOCKED) {
            cell.setNextState(BLOCKED, colors[BLOCKED]);
            return;
        }
        if (cell.getCurrState() == OPEN && hasPercolatedNeighbors) {
            cell.setNextState(PERCOLATED, colors[PERCOLATED]);
            return;
        }
        if (cell.getCurrState() == OPEN && !(hasPercolatedNeighbors)) {
            cell.setNextState(OPEN, colors[OPEN]);
            return;
        }
        if (cell.getCurrState() == PERCOLATED) {
            cell.setNextState(PERCOLATED, colors[PERCOLATED]);
        }
    }
}
