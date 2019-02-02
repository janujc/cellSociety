package visualization;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import simulation.PredatorPrey;
import simulation.Simulation;
import uitools.Control;
import uitools.SpeedDownControl;
import uitools.SpeedUpControl;

import static uitools.TextGenerator.makeText;
import static visualization.Controller.*;

public class SimulationScreen {
    private Group myContainer;
    private Controller context;
    private Simulation simulation;
    private int rate = 1; // Frequency in Hertz
    private Text rateText;
    private Control speedUpControl, speedDownControl;
    public SimulationScreen(Scene scene, Controller context, Simulation simulation, String label) {
        this.simulation = simulation;

        var container = new Group();
        Text titleText = makeText(label, sofiaPro, Color.SLATEGREY,
                scene.getWidth()/2,
                scene.getHeight()/10);

        myContainer = container;

        Text pressEscape = makeText("Press Escape To Exit", bebasKai, Color.SLATEGREY, scene.getWidth()/2, scene.getHeight()-15);
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

        container.getChildren().addAll(titleText, pressEscape, speedUpControl.getView(),
                rateText, speedDownControl.getView());
    }
    public Group getContainer() {
        return myContainer;
    }
    public void setRate(int rate) {
        if (rate > 0) { // rate can't go below 1 (that's just pause)
            this.rate = rate;
            this.rateText.setText(String.valueOf(rate));
            speedDownControl.getView().setLayoutX(rateText.getX() + rateText.getLayoutBounds().getWidth()); // recalc pos
        }
    }
    public int getRate() {
        return rate;
    }
}
