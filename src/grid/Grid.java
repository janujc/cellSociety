package grid;

import javafx.scene.paint.Color;
import utils.Cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public abstract class Grid {

    /**
     * The simulation grid made up of cells each with their own state (represented by an int)
     * <p>
     * NOTE: grid is in (x, y) coordinate form, so the outer array represents the columns and the inner array represents
     * the element of each row in a particular column
     */
    protected final Cell[][] myGrid;

    public final int mySize;
    public final int myNumRows;
    public final int myNumCols;
    public final Color[] myColors;
    protected List<int[]> neighborCoords = new ArrayList<>();

    public Grid(int size, Color[] colors, double mult) {
        mySize = size;
        myNumCols = (int) (size * mult);
        myNumRows = size;
        myColors = colors;
    }

    public void populateGrid(Integer[] states) {
        Random rand = new Random();

        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int randNum = rand.nextInt(states.length);
                myGrid[x][y] = new Cell(states[randNum], x, y, myColors[randNum]);
            }
        }
    }

    public void populateGrid(Integer[] states, Double[] populationFreqs) {
        Random rand = new Random();

        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                int randNum = rand.nextInt(100);
                double cumulativeFreqs = 0;
                for (int k = 0; k < states.length; k++) {
                    cumulativeFreqs += populationFreqs[k];
                    if (randNum < 100 * (cumulativeFreqs)) {
                        myGrid[x][y] = new Cell(states[k], x, y, myColors[k]);
                        break;
                    }
                }
            }
        }
    }

    public void populateGrid(Integer[][] states){
        for (int x = 0; x < myNumCols; x++) {
            for (int y = 0; y < myNumRows; y++) {
                myGrid[x][y] = new Cell(states[x][y], x, y, myColors[states[x][y]]);
            }
        }
    }

    public abstract List<Cell> getNeighbors(Cell center, Boolean bool);

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

    public List<Cell> getNeighborsOfType(Cell center, Boolean onlyCardinal, int type) {
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
        }
        return neighbors;
    }

    public Cell[][] getMyGrid() {
        return myGrid;
    }
}
