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

    /**
     * The initial grid will contain only blocked and open cells. In the first step(), we will calculate where the
     * percolated cell will be.
     *
     * The simulation will end when a cell on the opposite side of the initial cell is percolated.
     *
     * If a cell is open and one of its neighbors is percolated, it will too become percolated.
     */
    @Override
    protected void calculateNextStates() {
        // TODO: Change a cell's state if one of its neighbors is percolated and it is open
    }
}
