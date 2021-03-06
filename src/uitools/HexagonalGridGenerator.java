package uitools;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import simulation.Simulation;

/**
 * Author: Anshu Dwibhashi
 * Class to generate a grid with hexagonal cells
 */
public class HexagonalGridGenerator {
    /**
     * Create a grid with hexagonal cells with given specs
     * @param rows Number of rows grid
     * @param columns Number of columns in grid
     * @param cellSize Size of each cell
     * @param simulation Simulation class that's generating this grid
     * @param myContainer Main view in the scene where this grid will be placed
     * @param adjustMargin Whether or not we adjust the margin to centre the grid
     * @return
     */
    public static Polygon[][] createGrid(int rows, int columns, double cellSize, Simulation simulation, Group myContainer,
                                         boolean adjustMargin) {
        var myGrid = new Polygon[rows][columns];
        double[] points = new double[12];
        for (int i = 0; i < 12; i += 2) {
            double angle = Math.PI * (0.5 + i / 6d);
            points[i] = Math.cos(angle);
            points[i + 1] = Math.sin(angle);
        }

        Group myG = new Group();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double yOffset = i*2*cellSize +
                        (simulation.getGrid().shouldShowOutlines() ? i*2 : 0); // padding
                double xOffset = 0;
                if (j%2 == 1) {
                    yOffset += cellSize +
                            (simulation.getGrid().shouldShowOutlines() ? 1 : 0); // padding
                    xOffset += ((j/2)+1)*cellSize/2 + (j/2)*cellSize/2 +
                            (simulation.getGrid().shouldShowOutlines() ? (j)*2 : 0 ); // padding
                } else {
                    xOffset += (j/2)*cellSize +
                            (simulation.getGrid().shouldShowOutlines() ? (j)*2 : 0); // padding
                }

                Polygon polygon = new Polygon(points);
                polygon.setLayoutX(xOffset);
                polygon.setLayoutY(yOffset);
                polygon.setStroke(simulation.getGrid().getMyGrid()[j][i].getCurrColor());
                polygon.setStrokeWidth(cellSize);
                final int iF = i, jF = j;
                polygon.setOnMouseClicked((e) ->  {
                    simulation.rotateState(jF, iF);
                    polygon.setStroke(simulation.getGrid().getMyGrid()[jF][iF].getCurrColor());
                });
                myG.getChildren().add(polygon);
                myGrid[i][j] = polygon;
            }
        }

        if (adjustMargin) {
            myG.setLayoutY(myContainer.getLayoutBounds().getHeight() / 2 - myG.getLayoutBounds().getHeight() / 2 - 15);
            myG.setLayoutX(myContainer.getLayoutBounds().getWidth() / 2 - myG.getLayoutBounds().getWidth() / 2);
        } else {
            myG.setLayoutY(cellSize/2);
            myG.setLayoutX(cellSize/2);
        }
        myContainer.getChildren().addAll(myG);

        return myGrid;
    }
}
