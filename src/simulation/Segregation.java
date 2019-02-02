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

    private int oppositeAgent(int currState) {
        if (currState == BLUE_AGENT) return RED_AGENT;
        return BLUE_AGENT;
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

    // Remove a cell from dissatisfiedAgents, remove a cell from emptyCell, make
    // emptyCell.setNextState(state of dissatisfied agent), add cell of dissatisfied agent to emptyCell.
    private void moveAgents() {
        Collections.shuffle(emptyCell);
        for (Cell agent : dissatisfiedAgents) {
            dissatisfiedAgents.remove(agent);
            Cell empty = emptyCell.get(0);
            emptyCell.remove(empty);
            empty.setNextState(agent.getCurrState(), colors[agent.getCurrState()]);
            agent.setNextState(EMPTY, colors[EMPTY]);
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
