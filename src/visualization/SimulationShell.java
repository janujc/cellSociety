package visualization;

import grid.Hexagonal;
import grid.Square;
import grid.Triangular;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import simulation.Simulation;
import uitools.HexagonalGridGenerator;
import uitools.SquareGridGenerator;
import uitools.TriangularGridGenerator;
import utils.Cell;

import java.util.ArrayList;

/**
 * Author: Anshu Dwibhashi
 * Class to display a shell of a simulation in a new window
 */
public class SimulationShell extends visualization.Simulation {
    private Group myContainer;
    private ArrayList<Cell[][]> history;
    private int historyPos = 0;
    private Stage myStage;
    private double currentCellSize = 0.0;

    /**
     * Create a simulation shell object to compare simulation with
     * @param myStage Stage being used to create this shell
     * @param simulation Simulation to run in this shell
     * @param title Title to display on the window
     */
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

    public void destroy() {
        myStage.close();
    }
}
