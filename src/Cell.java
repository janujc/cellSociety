
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

    public Cell(int state, int xCoord, int yCoord) {
        myCurrState = state;
        myXCoord = xCoord;
        myYCoord = yCoord;
    }

    public void updateState() {
        myCurrState = myNextState;
    }

    public void setNextState(int state) {
        myNextState = state;
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
}
