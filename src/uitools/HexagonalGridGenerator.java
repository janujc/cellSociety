package uitools;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HexagonalGridGenerator {
    public static Group createGrid(int rows, int columns, double cellSize) {
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
                polygon.setStroke(Color.BLACK);
                polygon.setStrokeWidth(cellSize);
                myG.getChildren().add(polygon);
            }
        }

        myG.setLayoutY(25);
        myG.setLayoutX(25);

        return myG;
    }
}
