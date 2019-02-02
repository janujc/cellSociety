package simulation;

import javafx.scene.paint.Color;

/**
 * Class that represents the simulation.Segregation simulation.
 * <p>
 * States: empty(0), occupied(1)
 *
 * @author Januario Carreiro
 */
public class Segregation extends Simulation {
    private final int PERCENT_SATISFIED;

    public Segregation(int sideSize, int[] states, double[] populationFreqs, Color[] colors, Integer percentSatsfied) {
        super(sideSize, states, populationFreqs, colors, percentSatsfied);
        PERCENT_SATISFIED = percentSatsfied;
    }

    @Override
    protected void calculateNextStates() {
        // TODO: Change a cell's state if it is not satisfied
    }
}
