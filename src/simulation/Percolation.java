package simulation;

import javafx.scene.paint.Color;

/**
 * Class that represents the simulation.Percolation simulation.
 * <p>
 * States: blocked(0), open(1), percolated(1)
 *
 * @author Januario Carreiro
 *
 */
public class Percolation extends Simulation {

    public Percolation(int sideSize, Integer[] states, Double[] populationFreqs, Color[] colors, Object metadata) {
        super(sideSize, states, populationFreqs, colors, null);
    }

    @Override
    protected void calculateNextStates() {
        // TODO: Change a cell's state if one of its neighbors is percolated and it is open
    }
}
