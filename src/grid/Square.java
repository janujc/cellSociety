package grid;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import utils.Cell;

// TODO: ADD TXT WITH RULES FOR GETTING NEIGHBORS FOR SQUARE CELL
public class Square extends Grid {

    public Square(int size, Color[] colors) {
        super(size, colors);
    }

    public void populateGrid() {} // each state equally likely

    public void populateGrid(Integer[] states, Double[] populationFreqs) {
        Random rand = new Random();

        for (int x = 0; x < mySize; x++) {
            for (int y = 0; y < mySize; y++) {
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

    public void populateGrid(Integer[][] states) { // Given 2-d array

    }

    public List<Cell> getNeighbors(Cell center, int arrangement) {
        List<int[]> neighborCoords = new ArrayList<>();
        int centerX = center.getCol();
        int centerY = center.getRow();

        // Loop through all the neighbors and add them to neighborCoords

        return validateNeighbors(neighborCoords);
    }
}
