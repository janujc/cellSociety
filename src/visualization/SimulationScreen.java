package visualization;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import simulation.PredatorPrey;
import simulation.Simulation;
import uitools.*;
import utils.Cell;
import utils.Snapshot;

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

    private Stack<Snapshot> history;

    public SimulationScreen(Scene scene, Controller context, Simulation simulation, String label) {
        this.simulation = simulation;
        this.history = new Stack<>();

        var container = new Group();
        Text titleText = makeText(label, sofiaPro, Color.SLATEGREY,
                scene.getWidth()/2,
                scene.getHeight()/10);

        myContainer = container;

        Text pressEscape = makeText("Press Escape To Exit", bebasKai, Color.SLATEGREY,
                scene.getWidth()/2, scene.getHeight()-5);

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

        container.getChildren().addAll(titleText, pressEscape, speedUpControl.getView(),
                rateText, speedDownControl.getView(), nextStateControl.getView(), prevStateControl.getView(),
                playPauseToggle.getView());
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
        // TODO
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
        renderGrid(history.pop().getGrid());
    }

    public void stepForward() {
        history.push(new Snapshot(simulation.getGrid())); // Save current grid in history
        simulation.step(); // Compute next grid
        Cell[][] newGrid = simulation.getGrid(); // Get next grid

        renderGrid(newGrid);
    }

    public boolean getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
}
