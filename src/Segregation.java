
/**
 * Class that represents the Segregation simulation.
 * <p>
 * States: empty(0), occupied(1)
 *
 * @author Januario Carreiro
 */
public class Segregation extends Simulation {

    public Segregation(int sideSize, int[] states, double[] populationFreqs) {
        super(sideSize, new int[]{0, 1, 2}, populationFreqs);
    }

    @Override
    protected void calculateNextStates() {

    }
}
