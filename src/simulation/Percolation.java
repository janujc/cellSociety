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

    /**
     * The possible states of each cell in the Percolation simulation
     */
    private int BLOCKED;
    private int OPEN;
    private int PERCOLATED;

    /**
     * Creates the simulation by calling super and using the constructor in simulation.Simulation
     * <p>
     * In order to create a 50x50 simulation where chance of generating BLOCKED Cell is 0.4, chance of generating
     * OPEN Cell is 0.6, Chance of generating PERCOLATED Cell is 0.0, and the Cell colors are orange, green, and white,
     * respectively, we would use:
     * Simulation Perc = new Percolation(50, new Integer[] {0, 1, 2}, new Double[] {0.4, 0.6, 0.0}, new Color[]
     * {Color.ORANGE, Color.GREEN, Color.WHITE}, null);
     * <p>
     * For the Percolation simulation, it is customary to generate a world with a 0.0 chance of creating a PERCOLATED
     * Cell and having the constructor randomly generate a single PERCOLATED Cell. When creating a grid, it is
     * recommended that the side length be between 25 and 100.
     *
     * @param metadata        any other information that might be needed for the simulation. In this case, null.
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

    /**
     * Determines the initial location for the percolated cell. The initial location can be anywhere on the perimeter
     * of the grid.
     *
     * @return Cell object set to Percolated state
     */
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

    /**
     * The initial grid will contain mostly blocked and open cells, and one percolated cell. Calculates the next state
     * for each cell in the grid based on this simulation's rules.
     */
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

    /**
     * Calculates the next state of an individual cell. Only OPEN cells can become PERCOLATED. BLOCKED and PERCOLATED
     * Cell objects do not change states.
     *
     * @param cell                   current Cell object
     * @param hasPercolatedNeighbors Boolean corresponding to number of PERCOLATED neighbors
     */
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
