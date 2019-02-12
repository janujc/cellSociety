package uitools;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import simulation.Simulation;

public class SquareGridGenerator  {
    public static Rectangle[][] createGrid(int rows, int columns, double cellSize, Simulation simulation, Group myContainer) {
        var gridViews = new Rectangle[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Shape gridView = new Rectangle(getCellXLocation(j, cellSize), getCellYLocation(i, cellSize), cellSize, cellSize);
                gridView.setFill(simulation.getGrid().getMyGrid()[i][j].getCurrColor());
                final int iF = i, jF = j;
                gridView.setOnMouseClicked((e) ->  {
                    simulation.rotateState(jF, iF);
                    gridView.setFill(simulation.getGrid().getMyGrid()[jF][iF].getCurrColor());
                });
                gridViews[i][j] = (Rectangle) gridView;
                myContainer.getChildren().add(gridView);
            }
        }
        return gridViews;
    }



    private static double getCellXLocation(int column, double currentCellSize) {
        return 100.0 + // Margin
                column * 1.0 // Padding between cells
                + currentCellSize * column;
    }

    private static double getCellYLocation(int row, double currentCellSize) {
        return 87.0 + // Margin
                row * 1.0 // Padding between cells
                + currentCellSize * row;
    }

}
