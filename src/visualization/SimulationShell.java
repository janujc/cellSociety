package visualization;

import grid.Hexagonal;
import grid.Square;
import grid.Triangular;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import simulation.Simulation;
import uitools.HexagonalGridGenerator;
import uitools.SquareGridGenerator;
import uitools.TriangularGridGenerator;
import utils.Cell;

import java.util.ArrayList;

public class SimulationShell {
    private Group myContainer;
    private Simulation simulation;
    private ArrayList<Cell[][]> history;
    private int historyPos = 0;
    private Shape[][] gridViews;
    private Stage myStage;
    private double currentCellSize = 0.0;

    // TODO: refactor render, getcelllox and initialise methods into new static class
    // TODO: interface that simulationshell and screen both implement

    public SimulationShell(Stage myStage, Simulation simulation, String title) {
        this.myStage = myStage;
        this.history = new ArrayList<>();

        myContainer = new Group();
        Scene scene = new Scene(myContainer, 400, 400, Color.GHOSTWHITE);

        this.simulation = simulation;
        history.clear();
        historyPos = 0;

        int numCells = simulation.getGrid().getMyGrid().length;

        // render initial state
        if (simulation.getGrid() instanceof Square) {
            currentCellSize = (400
                    - (simulation.getGrid().shouldShowOutlines() ? (numCells - 1) * 1 : 0)) // Account for padding
                    / (numCells * 1.0); // Number of cells
            gridViews = SquareGridGenerator.createGrid(
                    simulation.getGrid().getNumRows(), simulation.getGrid().getNumCols(),
                    currentCellSize, simulation, myContainer, 0, 0
            );
        } else if (simulation.getGrid() instanceof Triangular) {
            currentCellSize = (400
                    - - (simulation.getGrid().shouldShowOutlines() ? (numCells - 1) * 1 : 0))  // Account for padding
                    / (numCells * 1.0); // Number of cells
            gridViews = TriangularGridGenerator.createGrid(
                    simulation.getGrid().getNumRows(), simulation.getGrid().getNumCols(),
                    currentCellSize, simulation, myContainer, false
            );
            myStage.setWidth(myContainer.getLayoutBounds().getWidth());
            myStage.setHeight(myContainer.getLayoutBounds().getHeight() + currentCellSize);
        } else if (simulation.getGrid() instanceof Hexagonal) {
            currentCellSize = (400
                    - (simulation.getGrid().shouldShowOutlines() ? ((numCells - 1)/2d) * 2 : 0)) // Account for padding
                    / (numCells/2.0); // Number of cells
            gridViews = HexagonalGridGenerator.createGrid(
                    simulation.getGrid().getNumRows(), simulation.getGrid().getNumCols(),
                    currentCellSize, simulation, myContainer, false
            );
            myStage.setWidth(myContainer.getLayoutBounds().getWidth());
            myStage.setHeight(myContainer.getLayoutBounds().getHeight() + currentCellSize);
        }

        renderGrid(simulation.getGrid().getMyGrid());

        myStage.setScene(scene);
        myStage.setTitle(title);

        myStage.show();
    }

    private void renderGrid(Cell[][] grid) {
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
            Cell[][] oldGrid = makeDeepCopy(simulation.getGrid().getMyGrid());

            history.add(oldGrid); // Save current grid in history
            historyPos++;
            simulation.step(); // Compute next grid
            Cell[][] newGrid = simulation.getGrid().getMyGrid(); // Get next grid
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
