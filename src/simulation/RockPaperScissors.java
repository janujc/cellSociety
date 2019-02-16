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

    /**
     * The minimum gradient of each colored cell. If a cell's gradient is the minimum, it can spread its color to white
     * cells that are a distance of MAX_GRADIENT away.
     */
    private static final int MIN_GRADIENT = 0;

    /**
     * The maximum gradient of each colored cell. Corresponds to the max distance the cell can spread its color.
     */
    private final int MAX_GRADIENT;

    /**
     * The possible states of each cell
     */
    private final int WHITE;
    private final int RED;
    private final int GREEN;
    private final int BLUE;

    /**
     * Tracks the current gradients of each colored cell
     * <p>
     * Keys are the colored cells, and the values are the corresponding gradients (ints)
     */
    private final Map<Cell, Integer> gradientTracker;

    /**
     * Creates the simulation and calls the super constructor to create the grid
     *
     * @param grid           the simulation grid
     * @param simStates      the possible states of the cells in the simulation grid
     * @param stateColors    the cell colors of each state in the simulation
     * @param populatingType designates how the grid should be populated (with a list, randomly, with set numbers of
     *                       each state, based on frequencies)
     * @param maxGradient    the maximum gradient of a colored cell
     */
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

    /**
     * Adds all of the colored cells to the gradient tracker. Initially, all colored cells have the minimum gradient.
     */
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

    /**
     * Calculates the next state for each cell in the grid based off this simulation's rules
     * <p>
     * Simulation rules: Each cell randomly chooses a neighboring cell (8 possible). If a colored cell chooses a white
     * cell, it replaces its color to that white cell if its gradient is not maxed out. If a colored cell chooses
     * another colored cell, it may "eat" that cell depending on its color.
     */
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
                    continue;
                } else if (shouldEat(cellState, neighborState)) {
                    eat(cell, randNeighbor);
                    continue;
                }
            }
            cell.setNextState(cellState, colors[cellState]);
        }
    }

    /**
     * Replaces a white cell with a colored cell if the colored cell's gradient is not at max. The new colored cell has
     * an incremented gradient to represent that it is "weaker" than the original colored cell. Updates the gradient
     * tracker accordingly.
     *
     * @param whiteCell the cell that is white
     * @param colorCell the colored cell that is to replace the white cell
     */
    private void replaceWhite(Cell whiteCell, Cell colorCell) {
        int colorCellState = getMostRecentState(colorCell);
        int colorCellGradient = gradientTracker.get(colorCell);

        if (colorCellGradient < MAX_GRADIENT) {
            whiteCell.setNextState(colorCellState, colors[colorCellState]);
            gradientTracker.put(whiteCell, colorCellGradient + 1);
        } else {
            whiteCell.setNextState(WHITE, colors[WHITE]);
        }
    }

    /**
     * Determines if a colored cell should eat another colored cell
     * <p>
     * Red eats blue, green eats red, and blue eats green
     *
     * @param currCellState the state/color of the cell potentially doing the eating
     * @param neighborState the state/color of the cell to potentially be eaten
     * @return true if the cell should eat, false otherwise
     */
    private boolean shouldEat(int currCellState, int neighborState) {
        if (currCellState == RED && neighborState == BLUE) {
            return true;
        } else if (currCellState == GREEN && neighborState == RED) {
            return true;
        } else return currCellState == BLUE && neighborState == GREEN;
    }

    /**
     * Makes a colored cell eat another colored cell. Updates the gradient tracker accordingly.
     *
     * @param eaten the cell being eaten
     * @param eater the cell doing the eating
     */
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

    /**
     * Rotates the state of the cell at a certain location. If the cell's new color is not white, adds the cell to the
     * gradient tracker.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    @Override
    public void rotateState(int x, int y) {
        Cell currCell = myGrid.getCellAt(x, y);
        int currState = currCell.getCurrState();
        int newState = (currState + 1) % states.length;
        if (newState != WHITE) {
            gradientTracker.put(currCell, MIN_GRADIENT);
        }

        currCell.setState(newState, colors[newState]);
    }
}
