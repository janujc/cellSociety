package grid;

import utils.Cell;
import java.util.ArrayList;
import java.util.List;

public class Hexagonal extends Grid {
    /**
     * This parameter needs to be private static in order to be used in the super declaration. If not static, it would
     * not be able to be referenced before the supertype constructor.
     */
    private static final double factor = 4.0;

    /**
     * Creates a Grid by calling the supertype constructor.
     * <p>
     * To create a Grid, we only have to decide on its size and whether or not it is toroidal. The main difference
     * between grid types is how cell neighbors are calculated. The visualization of the different cell/grid types is
     * completely dealt by the Visualization classes, i.e. nothing in this Grid subclass determines the shape of the
     * grid cells except for the fact that it would make sense for them to be hexagonal due to the name of the class.
     * <p>
     * To create a Hexagonal Grid that is not toroidal, we could write:
     *
     * Grid hexagonal = new Hexagonal(100, false);
     *
     * @param size      number of rows/columns of grid
     * @param toroidal  whether left/right of grid is connected (donut)
     */
    public Hexagonal(int size, boolean toroidal) {
        super(size, toroidal, factor);
    }

    /**
     * Method to calculate cells that are neighbors of the Cell passed as the first parameter. Overrides the abstract
     * method getNeighbors() in the Grid superclass.
     *
     * @param center        Cell whose neighbors are to be calculated
     * @param onlyCardinal  does not do anything for Hexagonal Grid
     * @return              a list of Cells that are neighbor of center
     */
    @Override
    public List<Cell> getNeighbors(Cell center, boolean onlyCardinal) {
        List<int[]> neighborCoords = new ArrayList<>();

        int centerX = center.getCol();
        int centerY = center.getRow();

        neighborCoords.add(new int[]{centerX + 2, centerY});        // East
        neighborCoords.add(new int[]{centerX - 2, centerY});        // West

        if (centerX % 2 == 0) {
            neighborCoords.add(new int[]{centerX + 1, centerY - 1});    // Northeast
            neighborCoords.add(new int[]{centerX + 1, centerY});        // Southeast
            neighborCoords.add(new int[]{centerX - 1, centerY});        // Southwest
            neighborCoords.add(new int[]{centerX - 1, centerY - 1});    // Northwest
        }
        else {
            neighborCoords.add(new int[]{centerX + 1, centerY});        // Northeast
            neighborCoords.add(new int[]{centerX + 1, centerY + 1});    // Southeast
            neighborCoords.add(new int[]{centerX - 1, centerY + 1});    // Southwest
            neighborCoords.add(new int[]{centerX - 1, centerY});        // Northwest
        }

        return validateNeighbors(neighborCoords);
    }
}
