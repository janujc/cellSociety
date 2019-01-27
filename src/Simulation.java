public abstract class Simulation {

    private Cell[][] grid;

    public Simulation(int sideSize) {
        grid = new Cell[sideSize][sideSize];
        populateGrid();
    }

    protected abstract void populateGrid();

    public abstract Cell[][] step();

}
