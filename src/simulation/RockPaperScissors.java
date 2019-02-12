
package simulation;

import grid.Grid;
import javafx.scene.paint.Color;
import utils.Cell;

import java.util.*;

public class RockPaperScissors extends Simulation {

    private final int WHITE;
    private final int RED;
    private final int GREEN;
    private final int BLUE;

    private static final int MIN_GRADIENT = 0;
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
                gradientTracker.put(currCell, MAX_GRADIENT);
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

            if (shouldEat(cellState, neighborState)) {
                eat(cell, randNeighbor);
            }
            else {
                cell.setNextState(cellState, colors[cellState]);
            }
        }
    }

    private boolean shouldEat(int currCellState, int neighborState) {
        if (currCellState == WHITE && neighborState != WHITE) {
            return true;
        }
        else if (currCellState == RED && neighborState == GREEN) {
            return true;
        }
        else if (currCellState == GREEN && neighborState == BLUE) {
            return true;
        }
        else if (currCellState == BLUE && neighborState == RED){
            return true;
        }
        else {
            return false;
        }
    }

    private void eat(Cell currCell, Cell neighbor) {
        int neighborGradient = gradientTracker.get(neighbor);
        if (neighborGradient == MIN_GRADIENT) {
            return;
        }
        int currCellGradient = gradientTracker.get(currCell);
        int currCellState = getMostRecentState(currCell);
        int neighborState = getMostRecentState(neighbor);

        if (currCellState == WHITE || currCellGradient == MIN_GRADIENT) {
            currCell.setNextState(neighborState, colors[neighborState]);
            currCellGradient = neighborGradient - 1;
        }
        else {
            currCell.setNextState(currCellState, colors[currCellState]);
            currCellGradient--;
        }

        if (neighborGradient != MAX_GRADIENT) {
            neighborGradient++;
        }

        gradientTracker.put(currCell, currCellGradient);
        gradientTracker.put(neighbor, neighborGradient);
    }

    @Override
    public void rotateState(int x, int y) {
        Cell currCell = myGrid.getCellAt(x, y);
        int currState = currCell.getCurrState();
        int newState = (currState + 1) % states.length;

        gradientTracker.put(currCell, MAX_GRADIENT);

        currCell.setState(newState, colors[newState]);
    }
}
