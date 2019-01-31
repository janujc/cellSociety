import java.util.Scanner;

public class BasicVisualizer {

    private Simulation sim;

    private BasicVisualizer(Simulation sim) {
        this.sim = sim;
    }

    private void display() {
        Cell[][] grid = sim.getGrid();
        int gridSideSize = grid.length;
        for (int y = 0; y < gridSideSize; y++) {
            for (int x = 0; x < gridSideSize; x++) {
                System.out.print(grid[x][y].getCurrState() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        //Simulation sim = new Fire(5, new double[]{.3, .4, .3}, 1);
        Simulation sim = new PredatorPrey(2, new double[]{.3, .4, .3}, 4);

        BasicVisualizer vis = new BasicVisualizer(sim);

        vis.display();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Would you like to continue? (y/n)");
            if (sc.next().equals("n")) {
                System.out.println("EXITING");
                break;
            }
            sim.step();
            vis.display();
        }
    }
}
