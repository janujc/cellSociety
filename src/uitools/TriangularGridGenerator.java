package uitools;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TriangularGridGenerator {
    public static Group createGrid(int rows, int columns, double cellSize) {
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
