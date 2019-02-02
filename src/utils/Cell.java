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

    public void setNextState(int state, Color color) {
        myNextState = state;
        setNextColor(color);
    }

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
