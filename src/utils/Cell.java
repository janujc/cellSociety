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
    private int myXCoord;
    private int myYCoord;
    private Color myCurrColor;
    private Color myNextColor;

    /**
     * "Default" constructor for Cell. Sets myCurrColor to Color.GHOSTWHITE.
     * @param state the initial state of the cell.
     * @param xCoord the column of the cell.
     * @param yCoord the row of the cell.
     */
    public Cell(int state, int xCoord, int yCoord) {
        myCurrState = state;
        myXCoord = xCoord;
        myYCoord = yCoord;
        myCurrColor = Color.GHOSTWHITE;
    }

    public Cell(int state, int xCoord, int yCoord, Color color) {
        myCurrState = state;
        myXCoord = xCoord;
        myYCoord = yCoord;
        myCurrColor = color;
    }

    public void updateState() {
        myCurrState = myNextState;
        myCurrColor = myNextColor;
    }

    /**
     * Sets next State and Color of Cell object.
     * @param state the state the object should be in the next step.
     * @param color the color the object should be in the next step.
     */
    public void setNextState(int state, Color color) {
        myNextState = state;
        setNextColor(color);
    }

    /**
     * Helper method for setNextState, sets the next Color of the Cell object.
     * @param color the color the object should be in the next step.
     */
    public void setNextColor(Color color) {
        myNextColor = color;
    }

    public int getXCoord() {
        return myXCoord;
    }

    public int getYCoord() {
        return myYCoord;
    }

    public int getCurrState() {
        return myCurrState;
    }

    public Color getCurrColor() {
        return myCurrColor;
    }
}
