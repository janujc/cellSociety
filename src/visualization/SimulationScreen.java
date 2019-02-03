package visualization;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import simulation.PredatorPrey;
import simulation.Simulation;
import uitools.*;
import utils.Cell;
import utils.ConfigParser;
import utils.Snapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import static uitools.TextGenerator.makeText;
import static visualization.Controller.*;

public class SimulationScreen {
    private Group myContainer;
    private Controller context;
    private Simulation simulation;
    private int rate = 1; // Frequency in Hertz
    private Text rateText;
    private Control speedUpControl, speedDownControl, nextStateControl, prevStateControl, playPauseToggle;
    private boolean isPaused = true; // Paused by default
    private double continueIn = 1000.0/rate; // How many milliseconds we'll continue the animation in
    private double pausedFor = 0; // How many milliseconds we've been paused for before animating
    private Rectangle[][] gridViews;
    private double currentCellSize;

    private ArrayList<Cell[][]> history;
    private int historyPos = 0;
    private String configFolder;

    private String className;

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
        currentCellSize = (400 - (numCells-1)*1)/(numCells*1.0);

        // render initial state
        initialiseGridViews(simulation.getGrid());
        renderGrid(simulation.getGrid());
    }

    public SimulationScreen(Scene scene, Controller context, Simulation simulation, String label, String configFolder, String className) {
        this.context = context;
        this.history = new ArrayList<>();
        this.configFolder = configFolder;
        this.className = className;

        var container = new Group();
        Text titleText = makeText(label, sofiaPro, Color.SLATEGREY,
                scene.getWidth()/2,
                scene.getHeight()/10);

        myContainer = container;

        Text pressEscape = makeText("Press Escape To Exit", bebasKai, Color.SLATEGREY,
                scene.getWidth()/2, scene.getHeight()-15-2.5);

        Text loadConfig = makeText("Load new config file", bebasKaiMedium, Color.SLATEGREY,
                scene.getWidth()/2, scene.getHeight()-30-5);
        loadConfig.setCursor(Cursor.HAND);
        loadConfig.setOnMouseClicked(mouseEvent -> loadNewConfigFile());

        speedUpControl = new SpeedUpControl(this);
        speedUpControl.getView().setLayoutX(25);
        speedUpControl.getView().setLayoutY(scene.getHeight()-70);
        speedUpControl.getView().setTooltip(new Tooltip("Speed up"));

        rateText = makeText("1", sofiaPro, Color.SLATEGREY,
                50+30,
                scene.getHeight()-25);

        speedDownControl = new SpeedDownControl(this);
        speedDownControl.getView().setLayoutX(rateText.getX()+rateText.getLayoutBounds().getWidth());
        speedDownControl.getView().setLayoutY(scene.getHeight()-70);
        speedDownControl.getView().setTooltip(new Tooltip("Speed down"));

        nextStateControl = new NextStateControl(this);
        nextStateControl.getView().setLayoutX(scene.getWidth() - 30 - 25);
        nextStateControl.getView().setLayoutY(scene.getHeight()-70);
        nextStateControl.getView().setTooltip(new Tooltip("Step forwards"));

        prevStateControl = new PrevStateControl(this);
        prevStateControl.getView().setLayoutX(scene.getWidth() - 3*30 - 25 -15*2);
        prevStateControl.getView().setLayoutY(scene.getHeight()-70);
        prevStateControl.getView().setTooltip(new Tooltip("Step backwards"));

        playPauseToggle = new PlayPauseToggleControl(this);
        playPauseToggle.getView().setLayoutX(scene.getWidth() - 2*30 - 25 -15);
        playPauseToggle.getView().setLayoutY(scene.getHeight()-70);
        playPauseToggle.getView().setTooltip(new Tooltip("Toggle play or pause"));

        Rectangle header = new Rectangle(0, 0, scene.getWidth(), 75);
        header.setEffect(new DropShadow(10, Color.DARKGREY));
        header.setFill(Color.WHITE);

        Rectangle footer = new Rectangle(0, scene.getHeight()-100, scene.getWidth(), 100);
        footer.setEffect(new DropShadow(10, Color.DARKGREY));
        footer.setFill(Color.WHITE);

        container.getChildren().addAll(header, footer, titleText, loadConfig, pressEscape, speedUpControl.getView(),
                rateText, speedDownControl.getView(), nextStateControl.getView(), prevStateControl.getView(),
                playPauseToggle.getView());
        processSimulation(simulation);
    }

    private double getCellXLocation(int column) {
        return 100.0 + column*1.0 + currentCellSize * column;
    }

    private double getCellYLocation(int row) {
        return 87.0 + row*1.0 + currentCellSize * row;
    }

    public void loadNewConfigFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(configFolder));
        File chosenFile = fileChooser.showOpenDialog(context.getStage());
        if (chosenFile != null) {
            Simulation newSim = ConfigParser.parseConfigFile(chosenFile.getAbsolutePath(), className);
            processSimulation(newSim);
        }
    }

    public void step(double elapsedTime) {
        if (!isPaused) {
            if (pausedFor >= continueIn) {
                // We've paused enough
                pausedFor = 0;
                continueIn = 1000.0/rate;

                stepForward();
            } else {
                pausedFor += elapsedTime * 1000; // We continue to pause the animation
            }
        }
    }

    private void renderGrid(Cell[][] grid) {
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                gridViews[i][j].setFill(grid[i][j].getCurrColor());
            }
        }
    }

    private void initialiseGridViews(Cell[][] grid) {
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                gridViews[i][j] = new Rectangle(getCellXLocation(j), getCellYLocation(i), currentCellSize, currentCellSize);
                gridViews[i][j].setFill(grid[i][j].getCurrColor());
                myContainer.getChildren().add(gridViews[i][j]);
            }
        }
    }

    public Group getContainer() {
        return myContainer;
    }

    public void setRate(int rate) {
        if (rate > 0 && rate <= 10) { // rate can't go below 1 (that's just pause), and can't see clearly above 10
            this.rate = rate;
            this.rateText.setText(String.valueOf(rate));
            speedDownControl.getView().setLayoutX(rateText.getX() + rateText.getLayoutBounds().getWidth()); // recalc pos
        }
    }

    public int getRate() {
        return rate;
    }

    public void stepBack() {
        if (historyPos > 0) {
            renderGrid(history.get(historyPos - 1));
            historyPos--;
        }
    }

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
                b[i][j] = new Cell(a[i][j].getCurrState(), a[i][j].getXCoord(), a[i][j].getYCoord(), a[i][j].getCurrColor());
            }
        }
        return b;
    }

    public boolean getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
}
