package grid;

import javafx.scene.paint.Color;
import utils.Cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Grid {

    protected Cell[][] myGrid;
    public final int mySize;
    public final Color[] myColors;
    private File file;
    private List<int[]> neighborCoords = new ArrayList<>();

    public Grid(int size, Color[] colors) {
        mySize = size;
        myColors = colors;
    }

    public abstract void populateGrid(Integer[] states); // each state equally likely

    public abstract void populateGrid(Integer[] states, Double[] populationFreqs);

    public abstract void populateGrid(Integer[][] states); // Given 2-d array

    public abstract List<Cell> getNeighbors(Cell center, int arrangement);

    public static void neighborRules(String code, String fileName) {
        File file = new File(fileName);

        Scanner kb = new Scanner(System.in);
        Scanner scanner;
        try {
            scanner = new Scanner(file).useDelimiter(",");
            while (scanner.hasNext()) {
                final String stringFromFile = scanner.nextLine();
                if (stringFromFile.contains(code)) {
                    // TODO: add each instruction to neighborCoords
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file " + fileName);
        } catch (RuntimeException e) {
            System.out.println("Cannot find " + code + " in " + fileName);
        }
    }

    public List<Cell> getNeighborsOfType(Cell center, int arrangement, int type) {
        List<Cell> neighbors;
        List<Cell> neighborsOfType = new ArrayList<>();
        neighbors = getNeighbors(center, arrangement);

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
            if (!(neighborX < 0 || neighborX >= mySize || neighborY < 0 || neighborY >= mySize)) {
                neighbors.add(myGrid[neighborX][neighborY]);
            }
        }
        return neighbors;
    }

    public Cell[][] getMyGrid() {
        return myGrid;
    }
}
