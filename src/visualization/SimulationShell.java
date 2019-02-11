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
    private int rate = 1; // Frequency in Hertz
    private boolean isPaused = true; // Paused by default
    private double continueIn; // How many milliseconds we'll continue the animation in
    private double pausedFor = 0; // How many milliseconds we've been paused for before animating

    // TODO: refactor render, getcelllox and initialise methods into new static class
    // TODO: interface that simulationshell and screen both implement

    public SimulationShell(Stage myStage, Simulation simulation, String title, int rate) {
        this.myStage = myStage;
        this.history = new ArrayList<>();
        this.rate = rate;
        this.continueIn = 1000.0 / rate;

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
     * Method that changes state of the current simulation each frame
     *
     * @param elapsedTime
     */
    public void step(double elapsedTime) {
        if (!isPaused) {
            if (pausedFor >= continueIn) {
                // We've paused enough
                pausedFor = 0;
                continueIn = 1000.0 / rate;

                stepForward();
            } else {
                pausedFor += elapsedTime * 1000; // We continue to pause the animation
            }
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


    /**
     * Get whether the simulation is paused right now
     *
     * @return
     */
    public boolean getIsPaused() {
        return isPaused;
    }

    /**
     * Set whether the simulation is paused right now
     *
     * @param isPaused Whether the simulation is paused right now
     */
    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }


    /**
     * Method to obtain the current rate of simulation
     *
     * @return Rate of simulation in Hertz
     */
    public int getRate() {
        return rate;
    }

    /**
     * Set the rate at which the simulation is taking place
     *
     * @param rate Rate of simulation in Hertz
     */
    public void setRate(int rate) {
        if (rate > 0 && rate <= 10) { // rate can't go below 1 (that's just pause), and can't see clearly above 10
            this.rate = rate;
        }
    }

    public void destroy() {
        myStage.close();
    }
}
