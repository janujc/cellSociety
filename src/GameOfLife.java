
/**
 * Class that represents the GameOfLife simulation
 * <p>
 * States: empty(0), full(1)
 *
 * @author Januario Carreiro
 *
 */
public class GameOfLife extends Simulation {

    public GameOfLife(int sideSize, int[] states, double[] populationFreqs) {
        super(sideSize, new int[]{0, 1, 2}, populationFreqs);
    }

    @Override
    protected void calculateNextStates() {

    }
}
