package grid;

import utils.Cell;
import java.util.ArrayList;
import java.util.List;

public class Triangular extends Grid {
    /**
     * This parameter needs to be private static in order to be used in the super declaration. If not static, it would
     * not be able to be referenced before the supertype constructor.
     */
    private static final double factor = 1.5;

    /**
     * Creates a Grid by calling the supertype constructor.
     * <p>
     * To create a Grid, we only have to decide on its size and whether or not it is toroidal. The main difference
     * between grid types is how cell neighbors are calculated. The visualization of the different cell/grid types is
     * completely dealt by the Visualization classes, i.e. nothing in this Grid subclass determines the shape of the
     * grid cells except for the fact that it would make sense for them to be triangular due to the name of the class.
     * <p>
     * To create a Triangular Grid that is not toroidal, we could write:
     *
     * Grid triangular = new Triangular(100, false);
     *
     * @param size      number of rows/columns of grid
     * @param toroidal  whether left/right of grid is connected (donut)
     */
    public Triangular(int size, boolean toroidal) {
        super(size, toroidal, factor);
    }

    /**
     * Method to calculate cells that are neighbors of the Cell passed as the first parameter. Overrides the abstract
     * method getNeighbors() in the Grid superclass.
     *
     * @param center        Cell whose neighbors are to be calculated
     * @param onlyCardinal  whether to calculate all neighbors or only 3 neighbors directly adjacent
     * @return              a list of Cells that are neighbor of center
     */
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
