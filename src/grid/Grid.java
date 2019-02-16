package grid;

import javafx.scene.paint.Color;
import utils.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Grid class holds all the cells that will be used by the Simulation and Visualization classes. To add a different
 * grid type, one should extend the Grid class and at least overwrite getNeighbors().
 * <p>
 * The Grid is initialized without any Cell objects. In order to initialize Cell objects throughout the grid,
 * one of the four populate() methods should be used.
 * <p>
 * To update the state of each Cell, first Cell.nextState and Cell.nextColor should be set. Then the simulation class
 * should use the updateStates() method and all Cell objects will have their nextState become their currState.
 *
 * NOTE: If one wants to access the individual cells, getCellAt() should be used, not getMyGrid().
 *
 * @author Januario Carreiro and Jonathan Yu
 */
public abstract class Grid {
    /**
     * Strings for each Grid subclass. If more subclasses are added, be sure to also add it below.
     */
    public final static String SQUARE_GRID = "square";
    public final static String TRIANGULAR_GRID = "triangular";
    public final static String HEXAGONAL_GRID = "hexagonal";

    /**
     * The simulation grid made up of cells each with their own state (represented by an int)
     * <p>
     * NOTE: grid is in (x, y) coordinate form, so the outer array represents the columns and the inner array represents
     * the element of each row in a particular column. Reminder that top left is (0, 0).
     */
    private final Cell[][] myGrid;
    private final boolean isToroidal;
    private final int myNumRows;
    private final int myNumCols;
    private boolean outlines = true;
    private double manualSize = -1;

    /**
     * Constructor for Grid. Initializes instance variables.
     *
     * Note: Not all grids have the same length and width because the size of each cell changes depending on
     * whether the grid has Square, Triangular or Hexagonal cells. If rows = columns for the Triangular and Hexagonal
     * grid types, the visualization would look off.
     *
     * @param size      the standard number of rows/columns
     * @param toroidal  whether the Grid is toroidal or not. Necessary for correct getNeighbor() implementation
     * @param factor    how much size is to multiplied by to get number of columns.
     */
    public Grid(int size, boolean toroidal, double factor) {
        myNumCols = (int) (size * factor);
        myNumRows = size;
        isToroidal = toroidal;
        myGrid = new Cell[myNumCols][myNumRows];
    }

    /**
     * Method to populate the grid when passed a 2-D ARRAY OF STATES by config file.
     *
     * @param colors    array of colors for each state
     * @param cells     2-D array of which state the Cell at each location should be
     */
    public void populate(Color[] colors, Integer[][] cells) {
        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int currState = cells[x][y];
                myGrid[x][y] = new Cell(currState, x, y, colors[currState]);
            }
        }
    }

    /**
     * Method to populate the grid completely randomly.
     *
     * @param states    array of states
     * @param colors    array of colors corresponding to states
     */
    public void populate(Integer[] states, Color[] colors) {
        Random rand = new Random();

        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int randNum = rand.nextInt(states.length);
                myGrid[x][y] = new Cell(states[randNum], x, y, colors[randNum]);
            }
        }
    }

    /**
     * Method to populate semi-randomly when a SET NUMBER OF EACH STATE is specified.
     *
     * @param states        array of states
     * @param colors        array of colors corresponding to states
     * @param numToOccupy   number of Cell object at each state
     */
    public void populate(Integer[] states, Color[] colors, Integer[] numToOccupy) {
        Random rand = new Random();
        int[] numAlreadyOccupied = new int[numToOccupy.length];

        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int randNum = rand.nextInt(states.length);
                while (numAlreadyOccupied[randNum] == numToOccupy[randNum]) {
                    randNum = rand.nextInt(states.length);
                }
                myGrid[x][y] = new Cell(states[randNum], x, y, colors[randNum]);
                numAlreadyOccupied[randNum]++;
            }
        }
    }

    /**
     * Method to populate the grid to SPECIFIED POPULATION FREQUENCIES.
     *
     * @param states            array of states
     * @param colors            array of colors corresponding to states
     * @param populationFreqs   relative frequencies (read: probability) of each state. Should sum to 1.
     */
    public void populate(Integer[] states, Color[] colors, Double[] populationFreqs) {
        Random rand = new Random();

        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int randNum = rand.nextInt(100);
                double cumulativeFreqs = 0;
                for (int i = 0; i < states.length; i++) {
                    cumulativeFreqs += populationFreqs[i];
                    if (randNum < 100 * (cumulativeFreqs)) {
                        myGrid[x][y] = new Cell(states[i], x, y, colors[i]);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Method to update the state of each Cell. Should be called by Simulation after setting next state of each Cell
     * object.
     */
    public void updateStates() {
        for (Cell[] column : myGrid) {
            for (Cell cell : column) {
                cell.updateState();
            }
        }
    }

    /**
     * Method to get neighbors of each Cell object. Each Grid subclass should override this method.
     *
     * @param center        Cell whose neighbors are being calculated
     * @param onlyCardinal  whether to calculate all neighbors or only "cardinal" neighbors
     * @return              a list of Cells that are neighbors of center
     */
    public abstract List<Cell> getNeighbors(Cell center, boolean onlyCardinal);

    /**
     * Method to only get neighbors of each Cell object that are currently a certain state.
     *
     * @param center        Cell whose neighbors are being calculated
     * @param type          state that we are looking for
     * @param onlyCardinal  whether to calculate all neighbors or only "cardinal" neighbors
     * @return              a list of Cells that are neighbors of center and are of state "type"
     */
    public List<Cell> getNeighborsOfType(Cell center, int type, boolean onlyCardinal) {
        List<Cell> neighbors;
        List<Cell> neighborsOfType = new ArrayList<>();

        neighbors = getNeighbors(center, onlyCardinal);

        for (Cell neighbor : neighbors) {
            if (neighbor.getCurrState() == type) {
                neighborsOfType.add(neighbor);
            }
        }
        return neighborsOfType;
    }

    protected List<Cell> validateNeighbors(List<int[]> neighborCoords) {
        List<Cell> neighbors = new ArrayList<>();

        for (int[] neighbor : neighborCoords) {
            int neighborX = neighbor[0];
            int neighborY = neighbor[1];
            if (!(neighborX < 0 || neighborX >= myNumCols || neighborY < 0 || neighborY >= myNumRows)) {
                neighbors.add(myGrid[neighborX][neighborY]);
            }
            else if (isToroidal) {
                int[] toroidalNeighborCoords = getToroidalNeighbor(neighborX, neighborY);
                neighbors.add(myGrid[toroidalNeighborCoords[0]][toroidalNeighborCoords[1]]);
            }
        }
        return neighbors;
    }

    private int[] getToroidalNeighbor(int x, int y) {
        int toroidalX = x;
        int toroidalY = y;
        if (x < 0) toroidalX = myNumCols + x;
        else if (x >= myNumCols) toroidalX = Math.abs(myNumCols - x);
        if (y < 0) toroidalY = myNumRows + y;
        else if (y >= myNumRows) toroidalY = myNumRows - y;
        return new int[]{toroidalX, toroidalY};
    }

    /**
     * Gets number of rows in Grid.
     *
     * @return integer number of rows
     */
    public int getNumRows() {
        return myNumRows;
    }

    /**
     * Gets number of columns in Grid. Determined by Grid type.
     *
     * @return integer number of columns
     */
    public int getNumCols() {
        return myNumCols;
    }

    /**
     * Other classes should not have access to entire grid at once, only specific Cell objects. getCellAt() returns the
     * Cell object at the specified grid location.
     *
     * @param x column of cell
     * @param y row of cell
     * @return cell object at specified x,y
     */
    public Cell getCellAt(int x, int y) {
        return myGrid[x][y];
    }

    /**
     * Deprecated method used for iterating through Grid. Now iteration through Grid should be done using getCellAt().
     *
     * @return a 2-D array of Cell objects.
     */
    @Deprecated
    public Cell[][] getMyGrid() {
        return myGrid;
    }

    /**
     * Sets whether grid should show outlines
     *
     * @param s boolean to set variable outlines
     */
    public void setShouldShowOutlines(boolean s) {
        outlines = s;
    }

    /**
     * Method used by visualization.SimulationScreen to determine whether this grid is outlined or not
     *
     * @return boolean outlines
     */
    public boolean shouldShowOutlines() {
        return outlines;
    }

    /**
     * Method to be used if cell size shouldn't be calculated automatically.
     *
     * @param manualSize
     */
    public void setManualCellSize(double manualSize) {
        this.manualSize = manualSize;
    }

    /**
     * Getter for manualSize. manualSize hardcoded by setManualCellSize.
     *
     * @return double size of Cell
     */
    public double getManualCellSize() {
        return manualSize;
    }
}
