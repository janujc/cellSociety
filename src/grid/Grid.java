package grid;

import javafx.scene.paint.Color;
import utils.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Grid {

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
     *
     * @param size
     * @param toroidal
     * @param factor
     */
    public Grid(int size, boolean toroidal, double factor) {
        myNumCols = (int) (size * factor);
        myNumRows = size;
        isToroidal = toroidal;
        myGrid = new Cell[myNumCols][myNumRows];
    }

    // populate based on list of states

    /**
     *
     * @param colors
     * @param cells
     */
    public void populate(Color[] colors, Integer[][] cells) {
        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int currState = cells[x][y];
                myGrid[x][y] = new Cell(currState, x, y, colors[currState]);
            }
        }
    }

    // Whether or not the grid shows outlines

    /**
     * Method used by visualization.SimulationScreen to determine whether this grid is outlined or not
     *
     * @return boolean outlines
     */
    public boolean shouldShowOutlines() {
        return outlines;
    }

    /**
     * Sets whether grid should show outlines
     *
     * @param s boolean to set variable outlines
     */
    public void setShouldShowOutlines(boolean s) {
        outlines = s;
    }

    // If cell size shouldn't be calculated

    /**
     *
     * @return
     */
    public double getManualCellSize() {
        return manualSize;
    }

    /**
     *
     * @param manualSize
     */
    public void setManualCellSize(double manualSize) {
        this.manualSize = manualSize;
    }

    // populate randomly

    /**
     *
     * @param states
     * @param colors
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

    // populate randomly with a set number of each state

    /**
     *
     * @param states
     * @param colors
     * @param numToOccupy
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
     * Method to populate the grid to specified population frequencies
     *
     * @param states
     * @param colors
     * @param populationFreqs
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
     *
     */
    public void updateStates() {
        for (Cell[] column : myGrid) {
            for (Cell cell : column) {
                cell.updateState();
            }
        }
    }

    /**
     *
     * @param center
     * @param onlyCardinal
     * @return
     */
    public abstract List<Cell> getNeighbors(Cell center, boolean onlyCardinal);

//    public static void neighborRules(String code, String fileName) {
//        File file = new File(fileName);
//
//        Scanner kb = new Scanner(System.in);
//        Scanner scanner;
//        try {
//            scanner = new Scanner(file);
//            while (scanner.hasNext()) {
//                final String stringFromFile = scanner.next();
//                if (stringFromFile.contains(code)) {
//                    // TODO: add each instruction to neighborCoords
//                    String[] str = scanner.nextLine().split(", \\{|\\}");
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Cannot find file " + fileName);
//        } catch (RuntimeException e) {
//            System.out.println("Cannot find " + code + " in " + fileName);
//        }
//    }


    /**
     *
     * @param center
     * @param type
     * @param onlyCardinal
     * @return
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
        else if (x >= myNumCols) toroidalX = myNumCols - x;
        if (y < 0) toroidalY = myNumRows + y;
        else if (y >= myNumRows) toroidalY = myNumRows - y;
        return new int[]{toroidalX, toroidalY};
    }

    /**
     *
     *
     * @return
     */
    public int getNumRows() {
        return myNumRows;
    }

    /**
     *
     * @return
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

    public Cell[][] getMyGrid() {
        return myGrid;
    }
}
