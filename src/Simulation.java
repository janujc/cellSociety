import java.util.ArrayList;
import java.util.List;

public abstract class Simulation {

    private Cell[][] grid;
    private int gridSideSize;

    public Simulation(int sideSize) {
        gridSideSize = sideSize;
        grid = new Cell[gridSideSize][gridSideSize];
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

        // mark invalid neighbors (not in grid) null
        for (int i = 0; i < neighbors.size(); i ++) {
            int[] neighbor = neighbors.get(i);
            if (!isCellValid(neighbor[0], neighbor[1])) {
                neighbors.set(i, null);
            }
        }

        return neighbors;
    }

    protected boolean isCellValid(int x, int y) {
        if (x < 0 || x >= gridSideSize || y < 0 || y >= gridSideSize) {
            return false;
        }

        return true;
    }

    public Cell[][] getGrid() {
        return grid;
    }

}
