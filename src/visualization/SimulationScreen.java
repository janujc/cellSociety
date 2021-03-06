package visualization;

import controls.*;
import grid.Grid;
import grid.Hexagonal;
import grid.Square;
import grid.Triangular;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simulation.Simulation;
import uitools.HexagonalGridGenerator;
import uitools.SquareGridGenerator;
import uitools.TriangularGridGenerator;
import utils.Cell;
import utils.ConfigParser;
import utils.Dialogs;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.SocketImpl;
import java.util.*;

import static uitools.TextGenerator.makeText;
import static uitools.TextGenerator.makeTextRelative;
import static visualization.Controller.*;


/**
 * Author: Anshu Dwibhashi
 * Class defining the screen that displays a simulation
 */
public class SimulationScreen extends visualization.Simulation {
    private Group myContainer;
    private Stage myStage;
    private int rate = 1; // Frequency in Hertz
    private Text rateText;
    private Control speedUpControl, speedDownControl, nextStateControl, prevStateControl, playPauseToggle, menuControl;
    private boolean isPaused = true; // Paused by default
    private double continueIn = 1000.0 / rate; // How many milliseconds we'll continue the animation in
    private double pausedFor = 0; // How many milliseconds we've been paused for before animating
    private double currentCellSize;
    private Text titleText;
    private Map<Color, XYChart.Series<Number, Number>> populationStats; // Color -> {iteration: freq}

    private ArrayList<Cell[][]> history;
    private int historyPos = 0;
    private String configFolder;

    private String className;

    private ArrayList<SimulationShell> possessedShells;

    /**
     * Constructor to create simulation screen
     * @param scene Scene for which to create this screen
     * @param myStage Stage on which this scene will be placed
     * @param simulation Simulation class that will be rendered
     * @param label The title to display on this screen
     * @param configFolder The folder where default.xml will be found for this simulation
     * @param className Class used to instantiate this particular simulation
     */
    SimulationScreen(Scene scene, Stage myStage, Simulation simulation, String label, String configFolder, String className) {
        this.myStage = myStage;
        this.history = new ArrayList<>();
        this.possessedShells = new ArrayList<>();
        this.configFolder = configFolder;
        this.className = className;
        this.populationStats = new HashMap<>();

        var container = new Group();
        titleText = makeText(label, sofiaPro, Color.SLATEGREY,
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

    /**
     * Display the options menu
      */
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
            handleGraphClick();
        });

        Text settingsText = makeTextRelative("Parameter settings", bebasKaiMedium, Color.SLATEGREY,
                dialogBox.getWidth()/2, 85 + 40);
        settingsText.setCursor(Cursor.HAND);
        settingsText.setOnMouseClicked((event)->{
            handleSettingsClick();
        });

        Text saveText = makeTextRelative("Save current state", bebasKaiMedium, Color.SLATEGREY,
                dialogBox.getWidth()/2, 85 + 80);
        saveText.setCursor(Cursor.HAND);
        saveText.setOnMouseClicked((event)->{
            handleSaveClick();
        });

        Text compareText = makeTextRelative("Compare with another simulation", bebasKaiMedium, Color.SLATEGREY,
                dialogBox.getWidth()/2, 85 + 120);
        compareText.setCursor(Cursor.HAND);
        compareText.setOnMouseClicked((event)->{
            handleCompareClick();
        });

        menuGroup.getChildren().addAll(dialogBox, closeControl.getView(), graphText, settingsText, saveText,
                compareText);
        menuGroup.setLayoutY(originY);
        menuGroup.setLayoutX(originX);

        myContainer.getChildren().add(menuGroup);
    }

    private void handleCompareClick() {
        if (!isPaused) {
            ((PlayPauseToggleControl) playPauseToggle).onClick();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(configFolder));
        File chosenFile = fileChooser.showOpenDialog(myStage);
        if (chosenFile != null) {
            try {
                Simulation compSim = ConfigParser.parseConfigFile(chosenFile.getAbsolutePath());
                var simShell = new SimulationShell(new Stage(), compSim, compSim.getDisplayName());
                populationStats.clear();

                // Reset current simulation
                while (historyPos > 0) {
                    stepBack();
                }

                possessedShells.add(simShell);
            } catch (Exception e) {
                Dialogs.showAlert("Erroneous configuration file chosen.");
            }
        }
    }

    /**
     * Close all simulations being compared to.
     */
    public void destroyPossessions() {
        for (SimulationShell ss : possessedShells) {
            ss.destroy();
        }
    }

    private void handleSettingsClick () {
        // Remove everything but the box and the close button
        while (menuGroup.getChildren().size() > 2)
            menuGroup.getChildren().remove(2);

        TextField gridSizeTf = new TextField();
        gridSizeTf.setPromptText("Enter grid size (e.g. 100)");
        gridSizeTf.setLayoutX(30);
        gridSizeTf.setLayoutY(60);
        gridSizeTf.setPrefWidth(menuGroup.getChildren().get(0).getLayoutBounds().getWidth() - 60);

        TextField popFreqsTf = new TextField();
        popFreqsTf.setPromptText("Enter population frequencies (e.g. 0.3, 0.7)");
        popFreqsTf.setLayoutX(30);
        popFreqsTf.setLayoutY(60 + 30);
        popFreqsTf.setPrefWidth(menuGroup.getChildren().get(0).getLayoutBounds().getWidth() - 60);

        Text random = makeTextRelative("If you don't want to specify\nfrequencies, enter \"RANDOM\" above to\nrandomly " +
                "assign states to cells.", sofiaProSmall, Color.SLATEGREY, 180, 160);

        Rectangle saveButton = new Rectangle(100, 30, Color.SLATEGREY);
        saveButton.setX(menuGroup.getChildren().get(0).getLayoutBounds().getWidth()/2 - saveButton.getLayoutBounds().getWidth()/2);
        saveButton.setArcHeight(20);
        saveButton.setArcWidth(20);
        saveButton.setCursor(Cursor.HAND);
        saveButton.setOnMouseClicked(e -> changeParams(popFreqsTf.getText(), gridSizeTf.getText()));
        saveButton.setY(menuGroup.getChildren().get(0).getLayoutBounds().getHeight() - saveButton.getLayoutBounds().getHeight()/2 - 45);

        Text saveText = new Text("Save");
        saveText.setFont(bebasKai);
        saveText.setFill(Color.GHOSTWHITE);
        saveText.setCursor(Cursor.HAND);
        saveText.toFront();
        saveText.setOnMouseClicked(e -> changeParams(popFreqsTf.getText(), gridSizeTf.getText()));
        saveText.setX(menuGroup.getChildren().get(0).getLayoutBounds().getWidth()/2 - saveText.getLayoutBounds().getWidth()/2);
        saveText.setY(menuGroup.getChildren().get(0).getLayoutBounds().getHeight() - saveText.getLayoutBounds().getHeight()/2 - 32.5);

        menuGroup.getChildren().addAll(gridSizeTf, popFreqsTf, random, saveButton, saveText);

    }

    private void changeParams(String popFreqsText, String gridSizeText) {
        try {
            int gridSize;
            if (gridSizeText == null || gridSizeText.isEmpty()) {
                gridSize = simulation.getGrid().getMyGrid().length;
            } else {
                gridSize = Integer.valueOf(gridSizeText);
            }
            boolean random = (popFreqsText == null || popFreqsText.isEmpty() || popFreqsText.equals("RANDOM"));

            Simulation newSim;
            Integer[] states = new Integer[simulation.getColors().length];
            Arrays.setAll(states, i -> i);
            boolean toroidal = false;
            if (random) {
                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getConstructor(Grid.class, Integer[].class, Color[].class, String.class, Object.class, String.class);
                Grid grid = new Square(gridSize, toroidal);
                newSim = (Simulation) constructor.newInstance(grid, states, simulation.getColors(), Simulation.RANDOM_TYPE, null, simulation.getMetadata());
            } else {
                Class<?> clazz = Class.forName(className);
                Grid grid = new Square(gridSize, toroidal);
                Constructor<?> constructor = clazz.getConstructor(Grid.class, Integer[].class, Color[].class, String.class, Object.class, String.class);
                ArrayList<Double> popFreqsList = new ArrayList<>();
                double popFreqsSum = 0;
                for (String item : popFreqsText.split(",")) {
                    popFreqsList.add(Double.parseDouble(item));
                    popFreqsSum += Double.parseDouble(item);
                }
                if (popFreqsSum < 1.0) {
                    Dialogs.showAlert("Frequencies must add to 1");
                    closeMenu(); return;
                }
                if (popFreqsList.size() != simulation.getColors().length) {
                    Dialogs.showAlert("Not enough frequencies");
                    closeMenu(); return;
                }
                newSim = (Simulation) constructor.newInstance(grid, states, simulation.getColors(), Simulation.FREQUENCIES, popFreqsList.toArray(new Double[0]), simulation.getMetadata());
            }


            newSim.setDisplayName(simulation.getDisplayName());
            newSim.setCurrentFileName(simulation.getCurrentFileName());
            newSim.setMetadata(simulation.getMetadata());

            processSimulation(newSim);
            titleText.setText(newSim.getDisplayName());
            titleText.setX((myStage.getScene().getWidth() / 2) - titleText.getLayoutBounds().getWidth() / 2);
            titleText.setY((myStage.getScene().getHeight() / 10) - titleText.getLayoutBounds().getHeight() / 2);
            populationStats.clear();
            closeMenu();
        } catch (Exception e) {
            Dialogs.showAlert("Malformed data provided");
        }
    }

    private void handleSaveClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(configFolder));
        File chosenFile = fileChooser.showSaveDialog(myStage);
        if (chosenFile != null) {
            // Now save current state in this file
            if(!chosenFile.getAbsolutePath().endsWith(".xml")) {
                chosenFile = new File(chosenFile.getAbsolutePath()+".xml");
            }
            ConfigParser.addGridToFile(chosenFile, simulation);
        }
        Dialogs.showAlert("File saved!");
        closeMenu();
    }

    private LineChart<Number,Number> lineChart = null;
    private void handleGraphClick() {
        Stage stage = new Stage();
        stage.setTitle("Populations over time");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Population");

        //creating the chart
        lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        Scene scene  = new Scene(lineChart,800,600);
        for (Color color : populationStats.keySet()) {
            addToLine(color);
        }

        lineChart.setLegendVisible(false);

        stage.setScene(scene);
        stage.show();
        closeMenu();
    }

    /**
     * Close the options menu
     */
    public void closeMenu() {
        myContainer.getChildren().remove(menuGroup);
        menuGroup = null;
    }

    private void processSimulation(Simulation simulation) {
        // First clear any existing stuff.
        if (gridViews != null) {
            for (Shape[] row : gridViews) {
                if (row != null) {
                    for (Shape r : row) {
                        ((Group) r.getParent()).getChildren().remove(r);
                    }
                }
            }
        }

        this.simulation = simulation;
        history.clear();
        historyPos = 0;

        // render initial state
        if (simulation.getGrid() instanceof Square) {
            int numCells = simulation.getGrid().getMyGrid().length;
            Group containerToPass;
            if (simulation.getGrid().getManualCellSize() == -1) {
                currentCellSize = (400
                        - (simulation.getGrid().shouldShowOutlines() ? (numCells - 1) * 1 : 0)) // Account for padding
                        / (numCells * 1.0); // Number of cells
                containerToPass = myContainer;
            } else {
                currentCellSize = simulation.getGrid().getManualCellSize();
                ScrollPane s1 = new ScrollPane();
                s1.setPrefSize(400, 400);
                Group weirdGroup = new Group();
                s1.setContent(weirdGroup);
                s1.setLayoutX(100);
                s1.setLayoutY(87.5);
                myContainer.getChildren().add(s1);
                containerToPass = weirdGroup;
            }
            gridViews = SquareGridGenerator.createGrid(
                    simulation.getGrid().getNumRows(), simulation.getGrid().getNumCols(),
                    currentCellSize, simulation, containerToPass, 87, 100
            );
        } else if (simulation.getGrid() instanceof Triangular) {
            int numCells = simulation.getGrid().getMyGrid().length;
            Group containerToPass;
            if (simulation.getGrid().getManualCellSize() == -1) {
                currentCellSize = (400
                        - (simulation.getGrid().shouldShowOutlines() ? (numCells - 1) * 1 : 0))  // Account for padding
                        / (numCells * 1.0); // Number of cells
                containerToPass = myContainer;
            } else {
                currentCellSize = simulation.getGrid().getManualCellSize();
                ScrollPane s1 = new ScrollPane();
                s1.setPrefSize(400, 400);
                Group weirdGroup = new Group();
                s1.setContent(weirdGroup);
                s1.setLayoutX(100);
                s1.setLayoutY(87.5);
                myContainer.getChildren().add(s1);
                containerToPass = weirdGroup;
            }

            gridViews = TriangularGridGenerator.createGrid(
                    simulation.getGrid().getNumRows(), simulation.getGrid().getNumCols(),
                    currentCellSize, simulation, containerToPass, true
            );
        } else if (simulation.getGrid() instanceof Hexagonal) {
            int numCells = simulation.getGrid().getMyGrid().length;
            Group containerToPass;
            if (simulation.getGrid().getManualCellSize() == -1) {
                currentCellSize = (400
                        - (simulation.getGrid().shouldShowOutlines() ? ((numCells - 1) / 2d) * 2 : 0)) // Account for padding
                        / (numCells / 2.0); // Number of cells
                containerToPass = myContainer;
            } else {
                currentCellSize = simulation.getGrid().getManualCellSize();
                ScrollPane s1 = new ScrollPane();
                s1.setPrefSize(400, 400);
                Group weirdGroup = new Group();
                s1.setContent(weirdGroup);
                s1.setLayoutX(100);
                s1.setLayoutY(87.5);
                myContainer.getChildren().add(s1);
                containerToPass = weirdGroup;
            }


            gridViews = HexagonalGridGenerator.createGrid(
                    simulation.getGrid().getNumRows(), simulation.getGrid().getNumCols(),
                    currentCellSize, simulation, containerToPass, true
            );
        }

        renderGrid(simulation.getGrid().getMyGrid());
    }

    /**
     * Get all simulations being compared with
     * @return ArrayList of all simulations open right now that are being compared with
     */
    public ArrayList<SimulationShell> getPossessedShells() {
        return possessedShells;
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
                Simulation newSim = ConfigParser.parseConfigFile(chosenFile.getAbsolutePath());
                processSimulation(newSim);
                titleText.setText(newSim.getDisplayName());
                titleText.setX((myStage.getScene().getWidth() / 2) - titleText.getLayoutBounds().getWidth()/2);
                titleText.setY((myStage.getScene().getHeight() / 10) - titleText.getLayoutBounds().getHeight()/2);
                populationStats.clear();
            } catch (Exception e) {
                Dialogs.showAlert("Erroneous configuration file chosen.");
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

                for(SimulationShell ss : possessedShells) {
                    ss.stepForward();
                }
            } else {
                pausedFor += elapsedTime * 1000; // We continue to pause the animation
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
            Cell[][] oldGrid = makeDeepCopy(simulation.getGrid().getMyGrid());
            updatePopulationStats(oldGrid, historyPos);

            history.add(oldGrid); // Save current grid in history
            historyPos++;
            simulation.step(); // Compute next grid
            Cell[][] newGrid = simulation.getGrid().getMyGrid(); // Get next grid

            renderGrid(newGrid);
        }
    }

    private void updatePopulationStats(Cell[][] grid, int historyPos) {
        Map<Color, Integer> popThisIteration = new HashMap<>();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (!popThisIteration.containsKey(cell.getCurrColor())) {
                    popThisIteration.put(cell.getCurrColor(), 1);
                } else {
                    popThisIteration.put(cell.getCurrColor(), popThisIteration.get(cell.getCurrColor()) + 1);
                }
            }
        }
        for (Color color : popThisIteration.keySet()) {
            boolean needToAddToLine = false;
            if (!populationStats.containsKey(color)) {
                populationStats.put(color, new XYChart.Series<>());
                if (lineChart != null) {
                    needToAddToLine = true;
                }
            }

            populationStats.get(color).getData().add(new XYChart.Data<>(historyPos, popThisIteration.get(color)));
            if (needToAddToLine) {
                addToLine(color);
            }
            cleanUpGraph();
        }
    }

    private void addToLine(Color color) {
        lineChart.getData().add(populationStats.get(color));

        Node line = populationStats.get(color).getNode().lookup(".chart-series-line");

        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    }

    private void cleanUpGraph() {
        if (lineChart != null) {
            //in loop take all series
            for (XYChart.Series<Number, Number> series : lineChart.getData()) {
                //for all series, take date, each data has Node (symbol) for representing point
                for (XYChart.Data<Number, Number> data : series.getData()) {
                    // this node is StackPane
                    StackPane stackPane = (StackPane) data.getNode();
                    stackPane.setVisible(false);
                }
            }
        }
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
