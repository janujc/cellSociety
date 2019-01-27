import java.util.Random;

/**
 * Class that represents the Predator-Prey simulation
 */
public class PredatorPrey extends Simulation {

    // the possible states of each cell
    final int EMPTY = 0;
    final int FISH = 1;
    final int SHARK = 2;

    public PredatorPrey(int sideSize, double[] initialPopulationFreqs) {
        super(sideSize, new int[]{0, 1, 2}, initialPopulationFreqs); // hard-coded b/c states are pre-determined
    }

    
}
