package grid;

import utils.Cell;

import java.util.ArrayList;
import java.util.List;

public class Hexagonal extends Grid {
    public Hexagonal(int size, boolean toroidal) {
        super(size, toroidal, 4.0);
    }

    @Override
    public List<Cell> getNeighbors(Cell center, boolean onlyCardinal) {
        List<int[]> neighborCoords = new ArrayList<>();

        int centerX = center.getCol();
        int centerY = center.getRow();

        neighborCoords.add(new int[]{centerX, centerY - 1});        // North
        neighborCoords.add(new int[]{centerX + 1, centerY});        // Northeast
        neighborCoords.add(new int[]{centerX - 1, centerY});        // Northwest
        neighborCoords.add(new int[]{centerX, centerY + 1});        // South
        neighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
        neighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest

        return validateNeighbors(neighborCoords);
    }
}
