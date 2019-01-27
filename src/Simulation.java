import java.util.ArrayList;
import java.util.List;

public abstract class Simulation {

    private Cell[][] grid;

    public Simulation(int sideSize) {
        grid = new Cell[sideSize][sideSize];
        populateGrid();
    }

    protected abstract void populateGrid();

    public abstract Cell[][] step();

    protected List<int[]> getNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();

        // N
        neighbors.add(new int[]{x, y - 1});
        // NE
        neighbors.add(new int[]{x + 1, y - 1});
        // E
        neighbors.add(new int[]{x + 1, y});
        // SE
        neighbors.add(new int[]{x + 1, y + 1});
        // S
        neighbors.add(new int[]{x, y + 1});
        // SW
        neighbors.add(new int[]{x - 1, y + 1});
        // W
        neighbors.add(new int[]{x - 1, y});
        //NW
        neighbors.add(new int[]{x -1, y - 1});

        return neighbors;
    }

    public Cell[][] getGrid() {
        return grid;
    }

}
