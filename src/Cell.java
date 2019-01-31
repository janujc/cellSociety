
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

    public Cell(int state, int xCor, int yCor) {
        myCurrState = state;
        myXCor = xCor;
        myYCor = yCor;
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

<<<<<<< Updated upstream
    public int getX() {
        return myXCor;
    }

    public int getY() {
        return myYCor;
=======
    public int getXCoord() {
        return myXCoord;
    }

    public int getYCoord() {
        return myYCoord;
>>>>>>> Stashed changes
    }
}
