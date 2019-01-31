package simulation;

/**
 * Class that represents the GameOfLife simulation
 * <p>
 * States: dead (0), alive (1)
 *
 * @author Januario Carreiro
 *
 */
public class GameOfLife extends Simulation {

    /**
     * The possible states of each cell in the GameOfLife simulation
     */
    private final int DEAD = 0;
    private final int ALIVE = 1;

    public GameOfLife(int sideSize, int[] states, double[] populationFreqs) {
        super(sideSize, new int[]{0, 1}, populationFreqs);
    }

    @Override
    protected void calculateNextStates() {
        /*
         * TODO: Change a cell's state according to a few rules:
         * Any LIVE cell with fewer than 2 neighbors DIES
         * Any LIVE cell with two or three neighbors LIVES
         * Any LIVE cell with more than 3 neighbors DIES
         * Any DEAD cell with more than 3 LIVE neighbors, becomes ALIVE.
         */
    }
}
