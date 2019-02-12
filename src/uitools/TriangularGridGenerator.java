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
                                         double marginY, double marginX) {
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
                        i*2; // padding
                double xOffset = j*cellSize
                        + j*2; // padding
                Polygon polygon;

                if ((j%2 == 1 && i%2 == 0) || (j%2 != 1 && i%2 != 0)) {
                    polygon = new Polygon(pointsUpwards);
                    yOffset += cellSize/2;
                } else {
                    polygon = new Polygon(pointsDownwards);
                }

                polygon.setLayoutX(xOffset);
                polygon.setLayoutY(yOffset);
                polygon.setStroke(simulation.getGrid().getMyGrid()[i][j].getCurrColor());
                polygon.setStrokeWidth(cellSize);
                final int iF = i, jF = j;
                polygon.setOnMouseClicked((e) ->  {
                    simulation.rotateState(jF, iF);
                    polygon.setFill(simulation.getGrid().getMyGrid()[jF][iF].getCurrColor());
                });
                myG.getChildren().add(polygon);

                gridViews[i][j] = polygon;
            }
        }

        myG.setLayoutY(marginY);
        myG.setLayoutX(marginX);
        myContainer.getChildren().addAll(myG);

        return gridViews;
    }
}
