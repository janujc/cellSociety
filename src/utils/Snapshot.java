package utils;

/**
 * Represents a snapshot of the grid in history.
 * Used for step backwards and step forwards functions.
 */
public class Snapshot {
    private Cell[][] grid;
    public Snapshot(Cell[][] grid) {
        this.grid = grid.clone();
        for (int i = 0; i < this.grid.length; i++) {
            this.grid[i] = this.grid[i].clone();
        }
    }
    public Cell[][] getGrid() {
        return this.grid;
    }
}
