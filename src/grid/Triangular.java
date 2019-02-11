package grid;

import utils.Cell;

import java.util.List;

public class Triangular extends Grid {
    public Triangular(int size, boolean toroidal) {
        super(size, toroidal);
    }

    @Override
    public List<Cell> getNeighbors(Cell center, boolean onlyCardinal) {
        int centerX = center.getCol();
        int centerY = center.getRow();

        if (((centerX + centerY) % 2) == 0) getNeighborsForSouth(centerX, centerY, onlyCardinal);
        else getNeighborsForNorth(centerX, centerY, onlyCardinal);

        return validateNeighbors(neighborCoords);
    }

    private void getNeighborsForNorth(int centerX, int centerY, boolean onlyCardinal) {
        neighborCoords.add(new int[]{centerX + 1, centerY});        // East
        neighborCoords.add(new int[]{centerX, centerY + 1});        // South
        neighborCoords.add(new int[]{centerX - 1, centerY});        // West

        if (! onlyCardinal) {
            neighborCoords.add(new int[]{centerX, centerY - 1});        // North
            neighborCoords.add(new int[]{centerX + 1, centerY - 1});    // Northeast
            neighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
            neighborCoords.add(new int[]{centerX + 2, centerY});        // Far East
            neighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
            neighborCoords.add(new int[]{centerX + 2, centerY + 1});    // Far Southeast
            neighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest
            neighborCoords.add(new int[]{centerX - 2, centerY + 1});    // Far Southwest
            neighborCoords.add(new int[]{centerX - 2, centerY});        // Far West
        }
    }

    private void getNeighborsForSouth(int centerX, int centerY, boolean onlyCardinal) {
        neighborCoords.add(new int[]{centerX, centerY - 1});        // North
        neighborCoords.add(new int[]{centerX + 1, centerY});        // East
        neighborCoords.add(new int[]{centerX - 1, centerY});        // West

        if (! onlyCardinal) {
            neighborCoords.add(new int[]{centerX + 1, centerY - 1});    // Northeast
            neighborCoords.add(new int[]{centerX + 2, centerY - 1});    // Far Northeast
            neighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
            neighborCoords.add(new int[]{centerX - 2, centerY - 1});    // Far Northwest
            neighborCoords.add(new int[]{centerX + 2, centerY});        // Far East
            neighborCoords.add(new int[]{centerX, centerY + 1});        // South
            neighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
            neighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest
            neighborCoords.add(new int[]{centerX - 2, centerY});        // Far West
        }
    }
}
