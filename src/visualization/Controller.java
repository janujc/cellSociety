package visualization;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import uitools.Animator;
import utils.ScreenType;

import static uitools.TextGenerator.makeText;

/**
 * Author: Anshu Dwibhashi
 * Main controller for this program.
 * Controls screens, animations and event handling, as well as setup
 */
public class Controller extends Application {

    // some things we need to remember during our game
    private Scene myScene;
    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 600;
    private final String WINDOW_TITLE = "SimuCell";
    private static final Paint BACKGROUND = Color.GHOSTWHITE;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    public static Font sofiaPro, sofiaProSmall, bebasKai, bebasKaiMedium;
    private Stage mainStage;

    private ScreenType currentScreen = ScreenType.HOME_SCREEN;
    private boolean inTransition = false;
    private SimulationScreen simulationScreen;

    /**
     * Method to set pointer to simulation screen that's currently active
     * @param ss Currently active simulation screen
     */
    public void setSimulationScreen(SimulationScreen ss) {
        this.simulationScreen = ss;
    }

    /**
     * Function where we build the stage with the first scene
     * @param stage provided to us by the library
     * @throws Exception
     */
    public Stage getStage() {
        return mainStage;
    }

    /**
     * Method that initialises it all. Takes in a stage obtained from the main method and attaches
     * an animation loop
     * @param stage
     */
    @Override
    public void start (Stage stage) {
        // attach scene to the stage and display it
        myScene = setupProg(WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(WINDOW_TITLE);
        stage.show();
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        stage.setResizable(false);
        mainStage = stage;
    }

    private Scene setupProg (int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        var root = new Group();

        // create a place to see the shapes
        var scene = new Scene(root, width, height, Color.GHOSTWHITE);
        HomeScreen homeScreen = new HomeScreen(scene, this);
        ((Group) scene.getRoot()).getChildren().add(homeScreen.getContainer());

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }


    private void step(double elapsedTime){
        if (currentScreen == ScreenType.SIMULATION_SCREEN) {
            simulationScreen.step(elapsedTime);
        }
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.ESCAPE && currentScreen == ScreenType.SIMULATION_SCREEN) {
            Animator.animate(myScene, (Group) ((Group)myScene.getRoot()).getChildren().get(1), ScreenType.HOME_SCREEN,
                    0, -WINDOW_WIDTH, WINDOW_WIDTH, 0, true, this);
        }
    }

    /**
     * Sets variable indicating whether or not the program is transitioning between screens
     * @param transition
     */
    public void setInTransition(boolean transition) {
        this.inTransition = transition;
    }

    /**
     * Sets variable indicating what type of a screen is currently active
     * @param currentScreen
     */
    public void setCurrentScreen(ScreenType currentScreen) {
        this.currentScreen = currentScreen;
    }


    /**
     * Main function to serve as entry point
     * @param args: cmd line args
     */
    public static void main(String args[]) {
        // load custom font
        try {
            sofiaPro = Font.loadFont(Controller.class.getResource("/fonts/sofiapro-light.otf").openStream(),30);
            sofiaProSmall = Font.loadFont(Controller.class.getResource("/fonts/sofiapro-light.otf").openStream(),15);
            bebasKai = Font.loadFont(Controller.class.getResource("/fonts/bebaskai.otf").openStream(),15);
            bebasKaiMedium = Font.loadFont(Controller.class.getResource("/fonts/bebaskai.otf").openStream(),25);
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }
}