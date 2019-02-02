package utils;

/**
 * Represents a snapshot of the grid in history.
 * Used for step backwards and step forwards functions.
 */
public class Snapshot {
    private Cell[][] grid;
    public Snapshot(Cell[][] grid) {
        this.grid = grid;
    }
    public Cell[][] getGrid() {
        return this.grid;
    }
}
