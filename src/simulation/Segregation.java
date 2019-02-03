package simulation;

import javafx.scene.paint.Color;
import utils.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents the simulation.Segregation simulation.
 * <p>
 * States: empty(0), occupied(1)
 *
 * @author Januario Carreiro
 */
public class Segregation extends Simulation {
    /**
     * Possible states of each cell in the Segregation simulation
     */
    private final int EMPTY = 0;
    private final int BLUE_AGENT = 1;
    private final int RED_AGENT = 2;

    /**
     * If a cell does not have minPercentSatisfaction percent of similar neighbors, it will move in the next step. Note that
     * empty cells do not count as neighbors.
     */
    private final double minPercentSatisfaction;

    /**
     * The list of agents that will move on a given step, represented by their current cells.
     */
    private List<Cell> dissatisfiedAgents;

    /**
     * The list of agents that will not move on a given step, represented by their current cells.
     */
    private List<Cell> satisfiedAgents;

    /**
     * The list of cells that agents can move to.
     * <p>
     * This is used to prevent conflicts where agents "overwrite" each other after moving into the same cell
     */
    private List<Cell> emptyCell;

    public Segregation(int sideSize, Integer[] states, Double[] populationFreqs, Color[] colors, String percentSatsfied) {
        super(sideSize, states, populationFreqs, colors, Integer.valueOf(percentSatsfied));
        minPercentSatisfaction = Integer.valueOf(percentSatsfied);
    }

    /**
     * We only have to move the dissatisfied cells, so we will iterate through the grid once, find all the cells that
     * do not meet or exceed the minPercentSatisfaction threshold, and then move those cells.
     */
    @Override
    protected void calculateNextStates() {
        determineCellBehavior();
        moveAgents();
        updateEmpty();
    }

    private void determineCellBehavior() {
        satisfiedAgents = new ArrayList<>();
        dissatisfiedAgents = new ArrayList<>();
        emptyCell = new ArrayList<>();

        for (Cell[] xCells : grid) {
            for (Cell cell : xCells) {
                if (cell.getCurrState() == EMPTY) emptyCell.add(cell);
                else {
                    List<Cell> simNeighbors = getNeighborsOfType(cell, cell.getCurrState(), false);
                    List<Cell> difNeighbors = getNeighborsOfType(cell, oppositeAgent(cell.getCurrState()), false);
                    int numSimNeighbors = simNeighbors.size();
                    int numDifNeighbors = difNeighbors.size();
                    determineAgentBehavior(cell, numSimNeighbors, numDifNeighbors);
                }
            }
        }
    }

    /**
     * Returns an int representing the agent of opposite color. If the input is an empty cell, returns an empty cell.
     *
     * e.g.
     * oppositeAgent(1) returns 2.
     *
     * e.g.
     * oppositeAgent(0) returns 0.
     *
     * @param currState the state of the Cell object.
     * @return an int corresponding to a different agent.
     */
    private int oppositeAgent(int currState) {
        if (currState == BLUE_AGENT) return RED_AGENT;
        if (currState == RED_AGENT) return BLUE_AGENT;
        return EMPTY;
    }

    private void determineAgentBehavior(Cell cell, int numSimNeighbors, int numDifNeighbors) {
        double totAgentNeighbors = numSimNeighbors + numDifNeighbors;
        if (totAgentNeighbors == 0) {
            satisfiedAgents.add(cell);
            return;
        }
        double satisfaction = (numSimNeighbors / totAgentNeighbors) * 100.0;
        if (satisfaction < minPercentSatisfaction) {
            dissatisfiedAgents.add(cell);
            return;
        }
        satisfiedAgents.add(cell);
    }

    /**
     * Shuffles emptyCell, then for each dissatisfied agent, sets their next state to empty; for each satisfied agent,
     * sets their next state to the same state.
     *
     * TODO: SHOULD I ADD DISSATISFIED AGENTS TO emptyCell?
     *
     * TODO: POSSIBLE BUG OCCURS IF THERE ARE NOT ENOUGH Cell OBJECTS FOR EACH Cell OBJECT IN dissatisfiedAgents. ABOVE.
     */
    private void moveAgents() {
        for (Cell agent : dissatisfiedAgents) {
            Collections.shuffle(emptyCell);
            Cell empty = emptyCell.get(0);
            emptyCell.remove(empty);
            empty.setNextState(agent.getCurrState(), colors[agent.getCurrState()]);
            agent.setNextState(EMPTY, colors[EMPTY]);
            emptyCell.add(agent);
        }
        for (Cell agent : satisfiedAgents) {
            agent.setNextState(agent.getCurrState(), colors[agent.getCurrState()]);
        }
    }

    private void updateEmpty() {
        for (Cell empty : emptyCell) {
            empty.setNextState(EMPTY, colors[EMPTY]);
        }
    }
}
