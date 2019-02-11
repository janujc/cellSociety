package visualization;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import simulation.Simulation;
import utils.Cell;

import java.util.ArrayList;

public class SimulationShell {
    private Group myContainer;
    private Simulation simulation;
    private ArrayList<Cell[][]> history;
    private int historyPos = 0;
    private Rectangle[][] gridViews;
    private Stage myStage;
    private double currentCellSize = 0.0;

    // TODO: refactor render, getcelllox and initialise methods into new static class
    // TODO: interface that simulationshell and screen both implement

    public SimulationShell(Stage myStage, Simulation simulation, String title) {
        this.myStage = myStage;
        this.history = new ArrayList<>();

        myContainer = new Group();
        Scene scene = new Scene(myContainer, 400, 400, Color.GHOSTWHITE);

        gridViews = new Rectangle[simulation.getGrid().length][simulation.getGrid()[0].length];
        this.simulation = simulation;
        history.clear();
        historyPos = 0;

        int numCells = simulation.getGrid().length;
        currentCellSize = (400
                - (numCells - 1) * 1) // Account for padding
                / (numCells * 1.0); // Number of cells

        // render initial state
        initialiseGridViews(simulation.getGrid());
        renderGrid(simulation.getGrid());

        myStage.setScene(scene);
        myStage.setTitle(title);
        myStage.show();
    }

    private void renderGrid(Cell[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                gridViews[i][j].setFill(grid[j][i].getCurrColor());
            }
        }
    }

    private void initialiseGridViews(Cell[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Shape gridView = new Rectangle(getCellXLocation(j), getCellYLocation(i), currentCellSize, currentCellSize);
                gridView.setFill(grid[i][j].getCurrColor());
                final int iF = i, jF = j;
                gridView.setOnMouseClicked((e) ->  {
                    simulation.rotateState(jF, iF);
                    gridView.setFill(simulation.getGrid()[jF][iF].getCurrColor());
                });
                gridViews[i][j] = (Rectangle) gridView;
                myContainer.getChildren().add(gridViews[i][j]);
            }
        }
    }

    private double getCellXLocation(int column) {
        return // 100.0 + // Margin
                column * 1.0 // Padding between cells
                + currentCellSize * column;
    }

    private double getCellYLocation(int row) {
        return // 87.0 + // Margin
                row * 1.0 // Padding between cells
                + currentCellSize * row;
    }


    /**
     * Method to reset the grid to how it was one cycle ago
     */
    public void stepBack() {
        if (historyPos > 0) {
            renderGrid(history.get(historyPos - 1));
            historyPos--;
        }
    }

    /**
     * Method to set the grid to how it'll be one cycle from now
     */
    public void stepForward() {
        if (historyPos < history.size() - 1) {
            renderGrid(history.get(historyPos + 1));
            historyPos++;
        } else {
            Cell[][] oldGrid = makeDeepCopy(simulation.getGrid());

            history.add(oldGrid); // Save current grid in history
            historyPos++;
            simulation.step(); // Compute next grid
            Cell[][] newGrid = simulation.getGrid(); // Get next grid
            renderGrid(newGrid);
        }
    }

    private Cell[][] makeDeepCopy(Cell[][] a) {
        Cell[][] b = new Cell[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = new Cell(a[i][j].getCurrState(), a[i][j].getCol(), a[i][j].getRow(), a[i][j].getCurrColor());
            }
        }
        return b;
    }

    public void destroy() {
        myStage.close();
    }
}
