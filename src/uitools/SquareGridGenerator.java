package uitools;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import simulation.Simulation;

/**
 * Author: Anshu Dwibhashi
 * Class to generate a grid with square cells
 */
public class SquareGridGenerator  {
    /**
     * Create a grid with square cells with given specs
     * @param rows Number of rows grid
     * @param columns Number of columns in grid
     * @param cellSize Size of each cell
     * @param simulation Simulation class that's generating this grid
     * @param myContainer Main view in the scene where this grid will be placed
     * @param adjustMargin Whether or not we adjust the margin to centre the grid
     * @return
     */
    public static Rectangle[][] createGrid(int rows, int columns, double cellSize, Simulation simulation, Group myContainer,
                                           double marginY, double marginX) {
        var gridViews = new Rectangle[rows][columns];
        Group myG = new Group();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Shape gridView = new Rectangle(getCellXLocation(j, cellSize, simulation), getCellYLocation(i, cellSize, simulation), cellSize, cellSize);
                gridView.setFill(simulation.getGrid().getMyGrid()[i][j].getCurrColor());
                final int iF = i, jF = j;
                gridView.setOnMouseClicked((e) ->  {
                    simulation.rotateState(jF, iF);
                    gridView.setFill(simulation.getGrid().getMyGrid()[jF][iF].getCurrColor());
                });
                gridViews[i][j] = (Rectangle) gridView;
                myG.getChildren().add(gridView);
            }
        }
        myG.setLayoutY(marginY);
        myG.setLayoutX(marginX);
        myContainer.getChildren().add(myG);
        return gridViews;
    }



    private static double getCellXLocation(int column, double currentCellSize, Simulation simulation) {
        return (simulation.getGrid().shouldShowOutlines() ? column * 1.0 : 0) // Padding between cells
                + currentCellSize * column;
    }

    private static double getCellYLocation(int row, double currentCellSize, Simulation simulation) {
        return (simulation.getGrid().shouldShowOutlines() ? row * 1.0 : 0) // Padding between cells
                + currentCellSize * row;
    }

}
