package visualization;

import controls.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simulation.Simulation;
import utils.Cell;
import utils.ConfigParser;

import java.io.File;
import java.util.ArrayList;

import static uitools.TextGenerator.makeText;
import static uitools.TextGenerator.makeTextRelative;
import static visualization.Controller.*;

/**
 * Author: Anshu Dwibhashi
 * Class defining the screen that displays a simulation
 */
public class SimulationScreen {
    private Group myContainer;
    private Stage myStage;
    private Simulation simulation;
    private int rate = 1; // Frequency in Hertz
    private Text rateText;
    private Control speedUpControl, speedDownControl, nextStateControl, prevStateControl, playPauseToggle, menuControl;
    private boolean isPaused = true; // Paused by default
    private double continueIn = 1000.0 / rate; // How many milliseconds we'll continue the animation in
    private double pausedFor = 0; // How many milliseconds we've been paused for before animating
    private Rectangle[][] gridViews;
    private double currentCellSize;

    private ArrayList<Cell[][]> history;
    private int historyPos = 0;
    private String configFolder;

    private String className;

    SimulationScreen(Scene scene, Stage myStage, Simulation simulation, String label, String configFolder, String className) {
        this.myStage = myStage;
        this.history = new ArrayList<>();
        this.configFolder = configFolder;
        this.className = className;

        var container = new Group();
        Text titleText = makeText(label, sofiaPro, Color.SLATEGREY,
                scene.getWidth() / 2,
                scene.getHeight() / 10);

        myContainer = container;

        Text pressEscape = makeText("Press Escape To Exit", bebasKai, Color.SLATEGREY,
                scene.getWidth() / 2, scene.getHeight() - 15 - 2.5);

        Text loadConfig = makeText("Load new config file", bebasKaiMedium, Color.SLATEGREY,
                scene.getWidth() / 2, scene.getHeight() - 30 - 5);
        loadConfig.setCursor(Cursor.HAND);
        loadConfig.setOnMouseClicked(mouseEvent -> loadNewConfigFile());

        speedUpControl = new SpeedUpControl(this);
        speedUpControl.getView().setLayoutX(25);
        speedUpControl.getView().setLayoutY(scene.getHeight() - 70);
        speedUpControl.getView().setTooltip(new Tooltip("Speed up"));

        rateText = makeText("1", sofiaPro, Color.SLATEGREY,
                50 + 30,
                scene.getHeight() - 25);

        speedDownControl = new SpeedDownControl(this);
        speedDownControl.getView().setLayoutX(rateText.getX() + rateText.getLayoutBounds().getWidth());
        speedDownControl.getView().setLayoutY(scene.getHeight() - 70);
        speedDownControl.getView().setTooltip(new Tooltip("Speed down"));

        nextStateControl = new NextStateControl(this);
        nextStateControl.getView().setLayoutX(scene.getWidth() - 30 - 25);
        nextStateControl.getView().setLayoutY(scene.getHeight() - 70);
        nextStateControl.getView().setTooltip(new Tooltip("Step forwards"));

        prevStateControl = new PrevStateControl(this);
        prevStateControl.getView().setLayoutX(scene.getWidth() - 3 * 30 - 25 - 15 * 2);
        prevStateControl.getView().setLayoutY(scene.getHeight() - 70);
        prevStateControl.getView().setTooltip(new Tooltip("Step backwards"));

        playPauseToggle = new PlayPauseToggleControl(this);
        playPauseToggle.getView().setLayoutX(scene.getWidth() - 2 * 30 - 25 - 15);
        playPauseToggle.getView().setLayoutY(scene.getHeight() - 70);
        playPauseToggle.getView().setTooltip(new Tooltip("Toggle play or pause"));

        Rectangle header = new Rectangle(0, 0, scene.getWidth(), 75);
        header.setEffect(new DropShadow(10, Color.DARKGREY));
        header.setFill(Color.WHITE);

        menuControl = new MenuControl(this);
        menuControl.getView().setLayoutX(scene.getWidth() - 30 - 25);
        menuControl.getView().setLayoutY(15);
        menuControl.getView().setCursor(Cursor.HAND);
        menuControl.getView().setTooltip(new Tooltip("Open Menu"));

        Rectangle footer = new Rectangle(0, scene.getHeight() - 100, scene.getWidth(), 100);
        footer.setEffect(new DropShadow(10, Color.DARKGREY));
        footer.setFill(Color.WHITE);

        container.getChildren().addAll(header, footer, titleText, loadConfig, pressEscape, speedUpControl.getView(),
                rateText, speedDownControl.getView(), nextStateControl.getView(), prevStateControl.getView(),
                playPauseToggle.getView(), menuControl.getView());
        processSimulation(simulation);
    }

    private Group menuGroup = null;
    public void showMenu() {
        menuGroup = new Group();
        Rectangle dialogBox = new Rectangle(0, 0, 350, 240);
        dialogBox.setEffect(new DropShadow(25, 0, 0, Color.web("#333333")));
        dialogBox.setArcWidth(20);
        dialogBox.setArcHeight(20);
        dialogBox.setFill(Color.WHITE);

        double originY = myStage.getScene().getHeight()/2 - dialogBox.getLayoutBounds().getHeight()/2 - 15;
        double originX = myStage.getScene().getWidth()/2 - dialogBox.getLayoutBounds().getWidth()/2;

        Control closeControl = new CloseControl(this);
        closeControl.getView().setLayoutX(dialogBox.getWidth() - 50);
        closeControl.getView().setLayoutY(10);
        closeControl.getView().setCursor(Cursor.HAND);
        closeControl.getView().setTooltip(new Tooltip("Close Menu"));

        Text graphText = makeTextRelative("Population Graph", bebasKaiMedium, Color.SLATEGREY,
        dialogBox.getWidth()/2, 85);
        graphText.setCursor(Cursor.HAND);
        graphText.setOnMouseClicked((event)->{
            System.out.println("Clicked graph button");
        });

        Text settingsText = makeTextRelative("Parameter settings", bebasKaiMedium, Color.SLATEGREY,
                dialogBox.getWidth()/2, 85 + 40);
        settingsText.setCursor(Cursor.HAND);
        settingsText.setOnMouseClicked((event)->{
            System.out.println("Clicked settings button");
        });

        Text saveText = makeTextRelative("Save current state", bebasKaiMedium, Color.SLATEGREY,
                dialogBox.getWidth()/2, 85 + 80);
        saveText.setCursor(Cursor.HAND);
        saveText.setOnMouseClicked((event)->{
            System.out.println("Clicked save button");
        });

        Text compareText = makeTextRelative("Compare with another simulation", bebasKaiMedium, Color.SLATEGREY,
                dialogBox.getWidth()/2, 85 + 120);

        menuGroup.getChildren().addAll(dialogBox, closeControl.getView(), graphText, settingsText, saveText,
                compareText);
        menuGroup.setLayoutY(originY);
        menuGroup.setLayoutX(originX);

        myContainer.getChildren().add(menuGroup);
    }

    public void closeMenu() {
        myContainer.getChildren().remove(menuGroup);
        menuGroup = null;
    }

    private void processSimulation(Simulation simulation) {
        // First clear any existing stuff.
        if (gridViews != null) {
            for (Rectangle[] row : gridViews) {
                if (row != null) {
                    for (Rectangle r : row) {
                        ((Group) r.getParent()).getChildren().remove(r);
                    }
                }
            }
        }
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
        //myContainer.getChildren().addAll(TriangularGridGenerator.createGrid(20, 30));
        //myContainer.getChildren().addAll(HexagonalGridGenerator.createGrid(20, 80));
    }

    private double getCellXLocation(int column) {
        return 100.0 + // Margin
                column * 1.0 // Padding between cells
                + currentCellSize * column;
    }

    private double getCellYLocation(int row) {
        return 87.0 + // Margin
                 row * 1.0 // Padding between cells
                + currentCellSize * row;
    }

    /**
     * Method to load new config file for this simulation
     */
    public void loadNewConfigFile() {
        if (!isPaused) {
            ((PlayPauseToggleControl) playPauseToggle).onClick();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(configFolder));
        File chosenFile = fileChooser.showOpenDialog(myStage);
        if (chosenFile != null) {
            try {
                Simulation newSim = ConfigParser.parseConfigFile(chosenFile.getAbsolutePath(), className);
                processSimulation(newSim);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    /**
     * Fetch group containing all children of this screen.
     *
     * @return Group object containing all elements of this screen.
     */
    public Group getContainer() {
        return myContainer;
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
            this.rateText.setText(String.valueOf(rate));
            speedDownControl.getView().setLayoutX(rateText.getX() + rateText.getLayoutBounds().getWidth()); // recalc pos
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
}
