package simulation;

import grid.Grid;
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
    private final Integer EMPTY = 0;
    private final Integer BLUE_AGENT = 1;
    private final Integer RED_AGENT = 2;
    private final double minPercentSatisfaction;

    private List<Cell> dissatisfiedAgents;
    private List<Cell> satisfiedAgents;
    private List<Cell> emptyCell;

    /**
     * Creates the simulation by calling super and using the constructor in simulation.Simulation
     * <p>
     * In order to create a new Simulation, we must first create a Grid object that has to be passed to the Simulation
     * constructor. If we want to create a Segregation simulation with a hexagonal grid where the EMPTY state is white,
     * the BLUE_AGENT state is blue, and the RED_AGENT state is red, with predetermined frequencies, and agents
     * needing to be surrounded by at least 30% similar neighbors, we would write:
     *
     * Simulation jim = new Segregation(new Hexagonal(parameters), new Integer[]{0, 1, 2},
     *                                  new Color[]{Color.WHITE, Color.BLUE, Color.RED}, "frequencies",
     *                                  new Double[]{0.2, 0.4, 0.4}, "0.3");
     * <p>
     * For the Segregation simulation, errors may occur if there are not enough empty spaces for a cell to go. There
     * should be at least a 0.1 chance that each Cell in the simulation is EMPTY. When creating a grid, it is
     * recommended that the side length be between 25 and 100.
     *
     * @param grid              the simulation grid
     * @param simStates         the possible states of the cells in the simulation grid
     * @param stateColors       the cell colors of each state in the simulation
     * @param populatingType    designates how the grid should be populated (with a list, randomly, with set numbers of
     *                          each state, based on frequencies)
     * @param populatingInfo    the data needed to populate the grid based on populatingType
     * @param percentSatisfied  percentage of neighbors of same state threshold
     */
    public Segregation(Grid grid, Integer[] simStates, Color[] stateColors, String populatingType,
                       Object populatingInfo, String percentSatisfied) {
        super(grid, simStates, stateColors, populatingType, populatingInfo);
        minPercentSatisfaction = Integer.valueOf(percentSatisfied);
    }

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

        for (int x = 0; x < gridNumCols; x++) {
            for (int y = 0; y < gridNumRows; y++) {
                Cell currCell = myGrid.getCellAt(x, y);
                if (currCell.getCurrState() == EMPTY) {
                    emptyCell.add(currCell);
                } else {
                    List<Cell> simNeighbors = myGrid.getNeighborsOfType(currCell, currCell.getCurrState(), false);
                    List<Cell> difNeighbors = myGrid.getNeighborsOfType(currCell, oppositeAgent(currCell.getCurrState()), false);
                    int numSimNeighbors = simNeighbors.size();
                    int numDifNeighbors = difNeighbors.size();
                    determineAgentBehavior(currCell, numSimNeighbors, numDifNeighbors);
                }
            }
        }
    }

    private int oppositeAgent(int currState) {
        if (currState == BLUE_AGENT) {
            return RED_AGENT;
        }
        if (currState == RED_AGENT) {
            return BLUE_AGENT;
        }
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
