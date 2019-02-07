package uitools;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Polygon;

public class HexagonalGridGenerator {
    public static GridPane createHoneyComb(int rows, int columns, double size) {
        // Source: https://stackoverflow.com/questions/40530899/honeycomb-layout-in-javafx-with-flowpane
        double[] points = new double[12];
        for (int i = 0; i < 12; i += 2) {
            double angle = Math.PI * (0.5 + i / 6d);
            points[i] = Math.cos(angle);
            points[i + 1] = Math.sin(angle);
        }

        Polygon polygon = new Polygon(points);

        GridPane result = new GridPane();
        RowConstraints rc1 = new RowConstraints(size / 4);
        rc1.setFillHeight(true);
        RowConstraints rc2 = new RowConstraints(size / 2);
        rc2.setFillHeight(true);

        double width = Math.sqrt(0.75) * size;
        ColumnConstraints cc = new ColumnConstraints(width/2);
        cc.setFillWidth(true);

        for (int i = 0; i < columns; i++) {
            result.getColumnConstraints().addAll(cc, cc);
        }

        for (int r = 0; r < rows; r++) {
            result.getRowConstraints().addAll(rc1, rc2);
            int offset = r % 2;
            int count = columns - offset;
            for (int c = 0; c < count; c++) {
                Button b = new Button();
                b.setPrefSize(width, size);
                b.setShape(polygon);
                b.setDisable(true);
                b.setStyle("-fx-background-color: #00ff00");
                b.setOpacity(1);
                result.add(b, 2 * c + offset, 2 * r, 2, 3);
            }
        }
        result.setHgap(size/20); //horizontal gap in pixels => that's what you are asking for
        result.setVgap(size/20); //vertical gap in pixels
        result.getRowConstraints().add(rc1);
        return result;
    }
}
