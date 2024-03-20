package src;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sudoku {
    /**
     * Returns the filled in sudoku grid.
     *
     * @param grid the partially filled in grid. unfilled positions are -1.
     * @return the fully filled sudoku grid.
     */
    public static int[][] solve(int[][] grid) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        int n = grid.length;

        // TODO: add your variables
        List<Integer> domain = IntStream.range(1, n + 1).boxed().collect(Collectors.toList());
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == -1) {
                    variables.add(new Solver.Variable(new ArrayList<>(domain)));
                } else {
                    variables.add(new Solver.Variable(new ArrayList<>(List.of(grid[i][j]))));
                }
            }
        }

        // TODO: add your constraints

        constraints.add(new Solver.UniqueRowConstraint(n));
        constraints.add(new Solver.UniqueColConstraint(n));
        constraints.add(new Solver.UniqueBoxConstraint(n));

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        int[] result = solver.findOneSolution(n * n);
//        System.out.println();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = result[i*n+j];
//                System.out.print(grid[i][j] + ",");
            }
//            System.out.println();
        }
        // TODO use result to construct answer
        return grid;
    }
}
