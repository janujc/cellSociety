package grid;

import utils.Cell;

import java.util.ArrayList;
import java.util.List;

public class Triangular extends Grid {
    public Triangular(int size, boolean toroidal) {
        super(size, toroidal, 1.5);
    }

    @Override
    public List<Cell> getNeighbors(Cell center, boolean onlyCardinal) {
        List<int[]> neighborCoords = new ArrayList<>();

        int centerX = center.getCol();
        int centerY = center.getRow();

        if (((centerX + centerY) % 2) == 0) neighborCoords = getNeighborsForSouth(centerX, centerY, onlyCardinal);
        else neighborCoords = getNeighborsForNorth(centerX, centerY, onlyCardinal);

        return validateNeighbors(neighborCoords);
    }

    private List<int[]> getNeighborsForNorth(int centerX, int centerY, boolean onlyCardinal) {
        List<int[]> northNeighborCoords = new ArrayList<>();

        northNeighborCoords.add(new int[]{centerX + 1, centerY});        // East
        northNeighborCoords.add(new int[]{centerX, centerY + 1});        // South
        northNeighborCoords.add(new int[]{centerX - 1, centerY});        // West

        if (! onlyCardinal) {
            northNeighborCoords.add(new int[]{centerX, centerY - 1});        // North
            northNeighborCoords.add(new int[]{centerX + 1, centerY - 1});    // Northeast
            northNeighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
            northNeighborCoords.add(new int[]{centerX + 2, centerY});        // Far East
            northNeighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
            northNeighborCoords.add(new int[]{centerX + 2, centerY + 1});    // Far Southeast
            northNeighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest
            northNeighborCoords.add(new int[]{centerX - 2, centerY + 1});    // Far Southwest
            northNeighborCoords.add(new int[]{centerX - 2, centerY});        // Far West
        }

        return northNeighborCoords;
    }

    private List<int[]> getNeighborsForSouth(int centerX, int centerY, boolean onlyCardinal) {
        List<int[]> southNeighborCoords = new ArrayList<>();

        southNeighborCoords.add(new int[]{centerX, centerY - 1});        // North
        southNeighborCoords.add(new int[]{centerX + 1, centerY});        // East
        southNeighborCoords.add(new int[]{centerX - 1, centerY});        // West

        if (! onlyCardinal) {
            southNeighborCoords.add(new int[]{centerX + 1, centerY - 1});    // Northeast
            southNeighborCoords.add(new int[]{centerX + 2, centerY - 1});    // Far Northeast
            southNeighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
            southNeighborCoords.add(new int[]{centerX - 2, centerY - 1});    // Far Northwest
            southNeighborCoords.add(new int[]{centerX + 2, centerY});        // Far East
            southNeighborCoords.add(new int[]{centerX, centerY + 1});        // South
            southNeighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
            southNeighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest
            southNeighborCoords.add(new int[]{centerX - 2, centerY});        // Far West
        }

        return southNeighborCoords;
    }
}
