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
    public static Group createHoneyComb(int rows, int columns, double size) {
        // Source: https://stackoverflow.com/questions/40530899/honeycomb-layout-in-javafx-with-flowpane
        double[] points = new double[12];
        for (int i = 0; i < 12; i += 2) {
            double angle = Math.PI * (0.5 + i / 6d);
            points[i] = Math.cos(angle);
            points[i + 1] = Math.sin(angle);
        }

        Group myG = new Group();
        Polygon polygon = new Polygon(points);
        polygon.setLayoutX(25);
        polygon.setLayoutY(25);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(10);

        Polygon polygon2 = new Polygon(points);
        polygon2.setLayoutX(35);
        polygon2.setLayoutY(25);
        polygon2.setStroke(Color.BLACK);
        polygon2.setStrokeWidth(10);

        Polygon polygon3 = new Polygon(points);
        polygon3.setLayoutX(25+10/2d);
        polygon3.setLayoutY(35);
        polygon3.setStroke(Color.BLACK);
        polygon3.setStrokeWidth(10);

        myG.getChildren().addAll(polygon, polygon2, polygon3);
        return myG;
/*
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
        */
    }
}
