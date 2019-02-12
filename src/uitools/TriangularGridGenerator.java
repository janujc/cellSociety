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

public class TriangularGridGenerator {
    public static Polygon[][] createGrid(int rows, int columns, double cellSize, Simulation simulation, Group myContainer,
                                         boolean adjustMargin) {
        var gridViews = new Polygon[rows][columns];
        double[] pointsDownwards = new double[6];
        double[] pointsUpwards = new double[6];
        for (int i = 0; i < 6; i += 2) {
            double angle = Math.PI * (0.5 + i / 3d);
            pointsDownwards[i] = Math.cos(angle);
            pointsUpwards[i] = Math.cos(angle);
            pointsDownwards[i + 1] = Math.sin(angle);
            pointsUpwards[i + 1] = -Math.sin(angle);
        }

        Group myG = new Group();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double yOffset = i*cellSize + i*cellSize/2 +
                        (simulation.getGrid().shouldShowOutlines() ? i*2 : 0); // padding
                double xOffset = j*cellSize
                        + (simulation.getGrid().shouldShowOutlines() ? j*2 : 0); // padding
                Polygon polygon;

                if ((j%2 == 1 && i%2 == 0) || (j%2 != 1 && i%2 != 0)) {
                    polygon = new Polygon(pointsUpwards);
                    yOffset += cellSize/2;
                } else {
                    polygon = new Polygon(pointsDownwards);
                }

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

                gridViews[i][j] = polygon;
            }
        }

        if (adjustMargin) {
            myG.setLayoutY(myContainer.getLayoutBounds().getHeight() / 2 - myG.getLayoutBounds().getHeight() / 2 - 5);
            myG.setLayoutX(myContainer.getLayoutBounds().getWidth() / 2 - myG.getLayoutBounds().getWidth() / 2 + 15);
        } else {
            myG.setLayoutY(cellSize/2);
            myG.setLayoutX(cellSize);
        }
        myContainer.getChildren().addAll(myG);

        return gridViews;
    }
}
