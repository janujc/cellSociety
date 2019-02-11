package grid;

import javafx.scene.paint.Color;
import utils.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Grid {

    /**
     * The simulation grid made up of cells each with their own state (represented by an int)
     * <p>
     * NOTE: grid is in (x, y) coordinate form, so the outer array represents the columns and the inner array represents
     * the element of each row in a particular column
     */
    private final Cell[][] myGrid;
    private final boolean isToroidal;
    private final int mySize;
    private final int myNumRows;
    private final int myNumCols;

    final boolean onlyCardinalNeighbors;
    List<int[]> neighborCoords = new ArrayList<>();

    public Grid(int size, boolean onlyCardinal, boolean toroidal) {
        mySize = size;
        myNumCols = mySize;
        myNumRows = mySize;
        onlyCardinalNeighbors = onlyCardinal;
        isToroidal = toroidal;
        myGrid = new Cell[myNumCols][myNumRows];
    }

    // populate based on list of states
    public void populate(Color[] colors, Integer[][] cells) {
        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int currState = cells[x][y];
                myGrid[x][y] = new Cell(currState, x, y, colors[currState]);
            }
        }
    }

    // populate randomly
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
    public void populate(Integer[] states, Color[] colors, Integer[] numToOccupy){
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

    // populate based on population frequencies
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

    public void updateStates() {
        for (Cell[] column : myGrid) {
            for (Cell cell : column) {
                cell.updateState();
            }
        }
    }

    public abstract List<Cell> getNeighbors(Cell center);

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

    public List<Cell> getNeighborsOfType(Cell center, int type) {
        List<Cell> neighbors;
        List<Cell> neighborsOfType = new ArrayList<>();

        neighbors = getNeighbors(center);

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
        if (y < 0) toroidalY = myNumRows + x;
        else if (y >= myNumRows) toroidalX = myNumRows - x;
        return new int[]{toroidalX, toroidalY};
    }

    public Cell[][] getMyGrid() {
        return myGrid;
    }
}
