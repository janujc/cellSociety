package grid;

import utils.Cell;

import java.util.Random;

public abstract class Grid {
    // TODO: grid is 2-d array
    // TODO: populateGrid() to create a grid ->
    // TODO:

    private Cell[][] myGrid;
    private final int mySize;

    public Grid(int size) {
        mySize = size;
    }

    public populateGrid() {} // each state equally likely

    public populateGrid(Integer[] states, Double[] populationFreqs) {
        Random rand = new Random();

        for (int x = 0; x < gridSideSize; x++) {
            for (int y = 0; y < gridSideSize; y++) {
                int randNum = rand.nextInt(100);
                double cumulativeFreqs = 0;
                for (int k = 0; k < states.length; k++) {
                    cumulativeFreqs += populationFreqs[k];
                    if (randNum < 100 * (cumulativeFreqs)) {
                        myGrid[x][y] = new Cell(states[k], x, y, colors[k]);
                        break;
                    }
                }
            }
        }
    }

    public populteGrid(given 2-d array) {}

    public getNeighbors(Cell center) {}
}
