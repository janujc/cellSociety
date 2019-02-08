package grid;

import javafx.scene.paint.Color;
import utils.Cell;

import java.util.List;

// TODO:
public class Hexagonal extends Grid {
    public Hexagonal(int size, Color[] colors ) {
        super(size, colors);
    }

    @Override
    public void populateGrid() {

    }

    @Override
    public void populateGrid(Integer[] states, Double[] populationFreqs) {

    }

    @Override
    public void populateGrid(Integer[][] states) {

    }

    @Override
    public List<Cell> getNeighbors(Cell center, int arrangement) {
        return null;
    }
}
