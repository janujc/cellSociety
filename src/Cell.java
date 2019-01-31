
/**
 * Class that represents each agent/empty cell in our simulation
 *
 * @author Januario Carreiro
 */
public class Cell {
    private int myCurrState;
    private int myNextState;
    private int myXCor;
    private int myYCor;

    public Cell(int state) {
        myCurrState = state;
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

    public int getXCor() {
        return myXCor;
    }

    public int getYCor() {
        return myYCor;
    }

}
