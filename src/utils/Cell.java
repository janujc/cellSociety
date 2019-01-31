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
    private Color myColor;

    public Cell(int state, int xCoord, int yCoord) {
        myCurrState = state;
        myXCoord = xCoord;
        myYCoord = yCoord;
        myColor = Color.GHOSTWHITE;
    }

    public Cell(int state, int xCoord, int yCoord, Color color) {
        myCurrState = state;
        myXCoord = xCoord;
        myYCoord = yCoord;
        myColor = color;
    }

    public void updateState() {
        myCurrState = myNextState;
    }

    public void setNextState(int state) {
        myNextState = state;
    }

    public void setColor(Color color) {
        myColor = color;
    }

    public int getCurrState() {
        return myCurrState;
    }

    public int getXCoord() {
        return myXCoord;
    }

    public int getYCoord() {
        return myYCoord;
    }

    public Color getColor() {
        return myColor;
    }
}
