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
    private int BLOCKED = 0;
    private int OPEN = 1;
    private int PERCOLATED = 2;


    public Percolation(int sideSize, Integer[] states, Double[] populationFreqs, Color[] colors, Object metadata) {
        super(sideSize, states, populationFreqs, colors, null);
        Cell startPerc = determineStartLocation();
        grid[startPerc.getXCoord()][startPerc.getYCoord()] = startPerc;
    }

    /**
     * The initial grid will contain mostly blocked and open cells, and one percolated cell.
     *
     * The simulation will end when a cell on the opposite side of the initial cell is percolated.
     *
     * If a cell is open and one of its neighbors is percolated, it will too become percolated.
     */
    @Override
    protected void calculateNextStates() {
        // TODO: Change a cell's state if one of its neighbors is percolated and it is open
    }

    private Cell determineStartLocation() {
        Random rand = new Random();
        int side = rand.nextInt(4);
        int coord1, coord2;
        if (side == 0) {
            coord1 = 0;
            coord2 = rand.nextInt(gridSideSize);
        }
        else if (side == 1) {
            coord1 = gridSideSize - 1;
            coord2 = rand.nextInt(gridSideSize);
        }
        else if (side == 2) {
            coord1 = rand.nextInt(gridSideSize);
            coord2 = 0;
        }
        else {
            coord1 = rand.nextInt(gridSideSize);
            coord2 = gridSideSize - 1;
        }
        return new Cell(PERCOLATED, coord1, coord2, colors[PERCOLATED]);
    }
}
