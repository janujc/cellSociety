package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents the Rock-Paper-Scissors simulation
 *
 * @author Jonathan Yu
 */
public class RockPaperScissors extends Simulation {

    private static final int MIN_GRADIENT = 0;
    private final int WHITE;
    private final int RED;
    private final int GREEN;
    private final int BLUE;
    private final int MAX_GRADIENT;

    private final Map<Cell, Integer> gradientTracker;

    public RockPaperScissors(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType,
                             Object populatingInfo, String maxGradient) {
        super(grid, simStates, stateColors, populatingType, populatingInfo);

        WHITE = states[0];
        RED = states[1];
        GREEN = states[2];
        BLUE = states[3];

        MAX_GRADIENT = Integer.valueOf(maxGradient);

        gradientTracker = new HashMap<>();
        initializeGradientTracker();
    }

    private void initializeGradientTracker() {
        for (int x = 0; x < gridNumCols; x++) {
            for (int y = 0; y < gridNumRows; y++) {
                Cell currCell = myGrid.getCellAt(x, y);
                if (currCell.getCurrState() != WHITE) {
                    gradientTracker.put(currCell, MIN_GRADIENT);
                }
            }
        }
    }

    @Override
    protected void calculateNextStates() {
        for (Cell cell : getCellsInRandomOrder()) {
            int cellState = getMostRecentState(cell);

            List<Cell> neighbors = myGrid.getNeighbors(cell, false);
            Cell randNeighbor = chooseRandomCellFromList(neighbors);
            int neighborState = getMostRecentState(randNeighbor);

            if (neighborState != WHITE) {
                if (cellState == WHITE) {
                    replaceWhite(cell, randNeighbor);
                }
                else if (shouldEat(cellState, neighborState)) {
                    eat(cell, randNeighbor);
                }
            }
            else {
                cell.setNextState(cellState, colors[cellState]);
            }
        }
    }

    private void replaceWhite(Cell whiteCell, Cell colorCell) {
        int colorCellState = colorCell.getCurrState();
        int colorCellGradient = gradientTracker.get(colorCell);

        if (colorCellGradient < MAX_GRADIENT) {
            whiteCell.setNextState(colorCellState, colors[colorCellState]);
            gradientTracker.put(whiteCell, colorCellGradient + 1);
        }
        else {
            whiteCell.setNextState(WHITE, colors[WHITE]);
        }
    }

    private boolean shouldEat(int currCellState, int neighborState) {
        if (currCellState == RED && neighborState == GREEN) {
            return true;
        } else if (currCellState == GREEN && neighborState == BLUE) {
            return true;
        } else return currCellState == BLUE && neighborState == RED;
    }

    private void eat(Cell eaten, Cell eater) {
        int eaterState = getMostRecentState(eater);
        int eaterGradient = gradientTracker.get(eater);

        eaten.setNextState(eaterState, colors[eaterState]);

        if (eaterGradient != MIN_GRADIENT) {
            eaterGradient--;
        }

        gradientTracker.put(eater, eaterGradient);
        gradientTracker.put(eaten, eaterGradient);
    }

    @Override
    public void rotateState(int x, int y) {
        Cell currCell = myGrid.getCellAt(x, y);
        int currState = currCell.getCurrState();
        int newState = (currState + 1) % states.length;
        if (newState == WHITE) {
            newState++;
        }

        gradientTracker.put(currCell, MIN_GRADIENT);

        currCell.setState(newState, colors[newState]);
    }
}
