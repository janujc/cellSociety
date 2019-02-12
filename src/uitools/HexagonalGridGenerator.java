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

public class HexagonalGridGenerator {
    public static Polygon[][] createGrid(int rows, int columns, double cellSize, Simulation simulation, Group myContainer,
                                         double marginY, double marginX) {
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
                        i*2; // padding
                double xOffset = 0;
                if (j%2 == 1) {
                    yOffset += cellSize +
                            1; // padding
                    xOffset += ((j/2)+1)*cellSize/2 + (j/2)*cellSize/2 +
                            (j)*2; // padding
                } else {
                    xOffset += (j/2)*cellSize +
                            (j)*2; // padding
                }

                Polygon polygon = new Polygon(points);
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
            }
        }

        myG.setLayoutY(marginY);
        myG.setLayoutX(marginX);
        myContainer.getChildren().addAll(myG);

        return myGrid;
    }
}
