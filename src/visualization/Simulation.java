package visualization;

import grid.Square;
import javafx.scene.shape.Shape;
import utils.Cell;

/**
 * Author: Anshu Dwibhashi
 * Abstract class containing functionality common to both SimulationScreen and SimulationShell
 */
public abstract class Simulation {
    protected Shape[][] gridViews;
    protected simulation.Simulation simulation;

    protected Cell[][] makeDeepCopy(Cell[][] a) {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = new Cell(a[i][j].getCurrState(), a[i][j].getCol(), a[i][j].getRow(), a[i][j].getCurrColor());
            }
        }
        return b;
    }

    protected void renderGrid(Cell[][] grid) {
        for (int i = 0; i < gridViews.length; i++) {
            for (int j = 0; j < gridViews[0].length; j++) {
                if (simulation.getGrid() instanceof Square) {
                    gridViews[i][j].setFill(grid[j][i].getCurrColor());
                } else {
                    gridViews[i][j].setStroke(grid[j][i].getCurrColor());
                }
            }
        }
    }
}
