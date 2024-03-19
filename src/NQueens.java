package src;

import java.util.*;

import static src.NQueens.visual;

public class NQueens {
    /**
     * Returns the number of N-Queen solutions
     */
    public static int getNQueenSolutions(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // TODO: add your variables

        for(int i = 0;i<n;i++){
            List<Integer> domain = new ArrayList<>();
            for(int j = 0;j<n*n;j++){
                domain.add(j);
            }
            variables.add(new Solver.Variable(domain));
        }


        // TODO: add your constraints
        constraints.add(new QueenConstraint(n));
        constraints.add(new SymmetryConstraint(n));

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions(0);
        for(int[] res : result){
            NQueens.visual(res,n);
        }
        // TODO: use result to construct answer
        return -1;
    }

    static void visual(int[] result, int n){
        for(int i = 0; i<n;i++){
            for (int j =0;j<n;j++){
                System.out.print(result[j+n*i]+" ");
            }
            System.out.println("");
        }
    }

    static class QueenConstraint extends Solver.Constraint{
        int n;
        public QueenConstraint(int n) {
            this.n = n;
        }

        @Override
        Solver.Variable[] infer(Solver.Variable[] vars) {
            for(Solver.Variable var : vars){
                if(var.picked){
                    int a = var.domain.get(0) % n;
                    int b = var.domain.get(0) / n;
                    for(Solver.Variable var2 : vars){
                        for(int i=0; i<n;i++){
                            var2.domain.remove(a + n*i);
                            var2.domain.remove(i + n*b);
                            int j1 = b - a + i;
                            int j2 = b + a - i;
                            var2.domain.remove(Integer.valueOf(i + n*j1));
                            var2.domain.remove(Integer.valueOf(i + n*j2));
                        }
                    }
                }
            }
            return vars;
        }
    }

    static class SymmetryConstraint extends Solver.Constraint{
        int n;
        boolean isFirst;
        public SymmetryConstraint(int n) {
            this.n = n;
            this.isFirst = true;
        }

        @Override
        Solver.Variable[] infer(Solver.Variable[] vars) {
            if(isFirst){
                isFirst = false;
                int width = n / 2 + 1;
                for (int a = 0; a < width; a++) {
                    for (int b = 0; b < n; b++) {
                        vars[0].domain.remove(b + a * n);
                    }
                }
                return vars;
            }else {
                return vars;
            }
        }
    }
}
