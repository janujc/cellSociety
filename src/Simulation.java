public abstract class Simulation {

    private int[][] grid;

    public Simulation(int sideSize) {
        grid = new Cell[sideSize][sideSize];
        populateGrid();
    }

    protected abstract void populateGrid();

}
