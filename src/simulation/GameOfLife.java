package simulation;

import javafx.scene.paint.Color;
import utils.Cell;
import java.util.HashMap;
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
    private final Integer ALIVE = 0;
    private final Integer DEAD = 1;
    private final HashMap<Integer, Color> stateMap = new HashMap<>() {{
        put(DEAD, Color.GHOSTWHITE);
        put(ALIVE, Color.BLACK);
    }};

    public GameOfLife(int sideSize, int[] states, double[] populationFreqs, Color[] colors, Object metadata) {
        super(sideSize, states, populationFreqs, colors, null);
    }

    @Override
    protected void calculateNextStates() {
        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                List<Cell> neighbors = getNeighborsOfType(cell, ALIVE, false);
                int numOfAliveNeighbors = neighbors.size();
                calculateNextStateOfOneCell(cell, numOfAliveNeighbors);
            }
        }
    }

    protected void calculateNextStateOfOneCell(Cell cell, int numOfAliveNeighbors) {
        if (cell.getCurrState() == ALIVE && numOfAliveNeighbors < 2) {
            cell.setNextState(DEAD, colors[DEAD]);
        }
        else if (cell.getCurrState() == ALIVE && (numOfAliveNeighbors == 2 || numOfAliveNeighbors == 3)) {
            cell.setNextState(ALIVE, colors[ALIVE]);
        }
        else if (cell.getCurrState() == ALIVE && numOfAliveNeighbors > 3) {
            cell.setNextState(DEAD, colors[DEAD]);
        }
        else if (cell.getCurrState() == DEAD && numOfAliveNeighbors > 3) {
            cell.setNextState(ALIVE, colors[ALIVE]);
        }
    }
}
