package simulation;

import javafx.scene.paint.Color;
import utils.Cell;

import java.util.Random;

/**
 * Class that represents the simulation.Percolation simulation.
 * <p>
 * States: blocked(0), open(1), percolated(1)
 *
 * @author Januario Carreiro
 *
 */
public class Percolation extends Simulation {

    /**
     * The possible states of each cell in the Percolation simulation
     */
    private int BLOCKED = 0;
    private int OPEN = 1;
    private int PERCOLATED = 2;

    /**
     * Instance variables to detect when to end Percolation
     */
    private Integer oppositeCol;
    private Integer oppositeRow;
    private boolean simComplete;

    /**
     * Creates the simulation by calling super and using the constructor in simulation.Simulation
     * <p>
     * In order to create a 50x50 simulation where chance of generating BLOCKED Cell is 0.4, chance of generating
     * OPEN Cell is 0.6, Chance of generating PERCOLATED Cell is 0.0, and the Cell colors are orange, green, and white,
     * respectively, we would use:
     * Simulation Perc = new Percolation(50, new Integer[] {0, 1, 2}, new Double[] {0.4, 0.6, 0.0}, new Color[]
     *                  {Color.ORANGE, Color.GREEN, Color.WHITE}, null);
     * <p>
     * For the Percolation simulation, it is customary to generate a world with a 0.0 chance of creating a PERCOLATED
     * Cell and having the constructor randomly generate a single PERCOLATED Cell. When creating a grid, it is
     * recommended that the side length be between 25 and 100.
     *
     * @param sideSize length of one side of grid
     * @param states an array of the possible states of each cell
     * @param populationFreqs an array of the frequencies corresponding to the states
     * @param colors an array of the colors corresponding to the states
     * @param metadata any other information that might be needed for the simulation. In this case, null.
     */
    public Percolation(int sideSize, Integer[] states, Double[] populationFreqs, Color[] colors, String metadata) {
        super(sideSize, states, populationFreqs, colors);
        Cell startPerc = determineStartLocation();
        grid[startPerc.getCol()][startPerc.getRow()] = startPerc;
        simComplete = false;
    }

    /**
     * Determines the initial location for the percolated cell. The initial location can be anywhere on the perimeter
     * of the grid.
     * @return Cell object set to Percolated state
     */
    private Cell determineStartLocation() {
        Random rand = new Random();
        int gridSideSize = getGridSideSize();
        int side = rand.nextInt(4);
        int coord1, coord2;
        if (side == 0) {
            coord1 = 0;
            coord2 = rand.nextInt(gridSideSize);
            oppositeCol = gridSideSize - 1;
        }
        else if (side == 1) {
            coord1 = gridSideSize - 1;
            coord2 = rand.nextInt(gridSideSize);
            oppositeCol = 0;
        }
        else if (side == 2) {
            coord1 = rand.nextInt(gridSideSize);
            coord2 = 0;
            oppositeRow = gridSideSize - 1;
        }
        else {
            coord1 = rand.nextInt(gridSideSize);
            coord2 = gridSideSize - 1;
            oppositeRow = 0;
        }
        return new Cell(PERCOLATED, coord1, coord2, colors[PERCOLATED]);
    }

    /**
     * The initial grid will contain mostly blocked and open cells, and one percolated cell. Calculates the next state
     * for each cell in the grid based on this simulation's rules.
     */
    @Override
    protected void calculateNextStates() {
        if (!simComplete) {
            isSimComplete();
        }
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                if (simComplete) {
                    cell.setNextState(cell.getCurrState(), colors[cell.getCurrState()]);
                }
                else {
                    Boolean hasPercolatedNeighbors = !(getNeighborsOfType(cell, PERCOLATED, false).isEmpty());
                    calculateNextStateOfOneCell(cell, hasPercolatedNeighbors);
                }
            }
        }
    }

    /**
     * Calculates the next state of an individual cell. Only OPEN cells can become PERCOLATED. BLOCKED and PERCOLATED
     * Cell objects do not change states.
     *
     * @param cell current Cell object
     * @param hasPercolatedNeighbors Boolean corresponding to number of PERCOLATED neighbors
     */
    private void calculateNextStateOfOneCell(Cell cell, Boolean hasPercolatedNeighbors) {
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

    private void isSimComplete() {
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                if(oppositeRow != null) {
                    simComplete = (cell.getCurrState() == PERCOLATED && cell.getRow() == oppositeRow);
                    if(simComplete) return;
                }
                if(oppositeCol != null) {
                    simComplete = (cell.getCurrState() == PERCOLATED && cell.getCol() == oppositeCol);
                    if(simComplete) return;
                }
            }
        }
    }
}
