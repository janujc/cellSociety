package utils;

import javafx.scene.paint.Color;

/**
 * Class that represents each agent/empty cell in our simulation
 *
 * @author Januario Carreiro
 */
public class Cell {
    private int myCurrState;
    private int myNextState;
    private int myCol;
    private int myRow;
    private Color myCurrColor;
    private Color myNextColor;

    /**
     * Default constructor for Cell. Sets myCurrColor to Color.GHOSTWHITE.
     * <p>
     * To create a Cell object whose current state is 0, we would use:
     *
     * Cell cell = new Cell(0, x, y);
     *
     * The parameters col and row should correspond to the col and row they are in grid.
     *
     * @param state the initial state of the cell.
     * @param col the column of the cell.
     * @param row the row of the cell.
     */
    public Cell(int state, int col, int row) {
        myCurrState = state;
        myNextState = -1;   // undetermined at this point
        myCol = col;
        myRow = row;
        myCurrColor = Color.GHOSTWHITE;
    }

    public Cell(int state, int xCoord, int yCoord, Color color) {
        myCurrState = state;
        myNextState = -1;
        myCol = xCoord;
        myRow = yCoord;
        myCurrColor = color;
    }

    /**
     * Sets myCurrentState to myNextState and also updates the color. Sets next state to undetermined (-1).
     */
    public void updateState() {
        myCurrState = myNextState;
        myCurrColor = myNextColor;

        myNextState = -1;
    }

    /**
     * Sets next State and Color of Cell object.
     * @param state the state the object should be in the next step.
     * @param color the color the object should be in the next step.
     */
    public void setNextState(int state, Color color) {
        myNextState = state;
        myNextColor = color;
    }

    /**
     * Getter method for column in which Cell object is located
     * @return column
     */
    public int getCol() {
        return myCol;
    }

    /**
     * Getter method for row in which Cell object is located
     * @return row
     */
    public int getRow() {
        return myRow;
    }

    /**
     * Getter method for current state of Cell
     * @return current state
     */
    public int getCurrState() {
        return myCurrState;
    }

    /**
     * Getter method for next state of cell
     * @return next state
     */
    public int getNextState() {
        return myNextState;
    }

    /**
     * Getter method for current color of Cell
     * @return current color
     */
    public Color getCurrColor() {
        return myCurrColor;
    }
}
