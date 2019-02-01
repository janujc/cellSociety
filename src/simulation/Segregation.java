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

    public Segregation(int sideSize, double[] populationFreqs, int percentSatsfied, Color[] colors) {
        super(sideSize, new int[]{0, 1, 2}, populationFreqs, colors);
        PERCENT_SATISFIED = percentSatsfied;
    }

    @Override
    protected void calculateNextStates() {
        // TODO: Change a cell's state if it is not satisfied
    }
}
