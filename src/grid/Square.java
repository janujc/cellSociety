package grid;

import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;
import utils.Cell;

public class Square extends Grid {
    public Square(int size, boolean toroidal) {
        super(size, toroidal, 1.0);
    }

    @Override
    public List<Cell> getNeighbors(Cell center, boolean onlyCardinal) {
        int centerX = center.getCol();
        int centerY = center.getRow();

        neighborCoords.add(new int[]{centerX, centerY - 1});    // North
        neighborCoords.add(new int[]{centerX + 1, centerY});    // East
        neighborCoords.add(new int[]{centerX, centerY + 1});    // South
        neighborCoords.add(new int[]{centerX - 1, centerY});    // West

        if (! onlyCardinal) {
            neighborCoords.add(new int[]{centerX + 1, centerY - 1});    // Northeast
            neighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
            neighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest
            neighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
        }

        return validateNeighbors(neighborCoords);
    }
}
