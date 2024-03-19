package src;

import java.util.*;


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
            for(int j = 0;j<n;j++){
                domain.add(j);
            }
            variables.add(new Solver.Variable(domain));
        }


        // TODO: add your constraints
        constraints.add(new RowConstraint(n));
        constraints.add(new DiagonalConstraint(n));
        //constraints.add(new SymmetryConstraint(n));

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions(n);

        System.out.println();
        System.out.println(result.size());
        // TODO: use result to construct answer
        return -1;
    }


    static class RowConstraint extends Solver.Constraint{
        int n;
        public RowConstraint(int n) {
            this.n = n;
        }

        @Override
        Solver.Variable[] infer(Solver.Variable[] vars) {
            for(Solver.Variable var : vars){
                if(var.picked){
                    int value = var.domain.get(0);
                    for(Solver.Variable var2 : vars){
                        if(!var2.picked){
                            var2.domain.remove(Integer.valueOf(value));
                        }
//                        for(int i=0; i<n;i++){
//                            var2.domain.remove(Integer.valueOf(a + n*i));
//                            var2.domain.remove(Integer.valueOf(i + n*b));
//                            int j1 = b - a + i;
//                            int j2 = b + a - i;
//                            var2.domain.remove(Integer.valueOf(i + n*j1));
//                            var2.domain.remove(Integer.valueOf(i + n*j2));
                        }
                    }
                }
            return vars;
        }
    }

    static class DiagonalConstraint extends Solver.Constraint{
        int n;
        public DiagonalConstraint(int n) {
            this.n = n;
        }

        @Override
        Solver.Variable[] infer(Solver.Variable[] vars) {
            for(int j=0;j<vars.length;j++){
                Solver.Variable var=vars[j];
                if(var.picked){
                    int a = j;
                    int b = var.domain.get(0);
                    for(int k=0;k<vars.length;k++){
                        if(!vars[k].picked){
                            vars[k].domain.remove(Integer.valueOf(b - a + k));
                            vars[k].domain.remove(Integer.valueOf(b + a - k));
                        }
//                        for(int i=0; i<n;i++){
//                            var2.domain.remove(Integer.valueOf(a + n*i));
//                            var2.domain.remove(Integer.valueOf(i + n*b));
//                            int j1 = b - a + i;
//                            int j2 = b + a - i;
//                            var2.domain.remove(Integer.valueOf(i + n*j1));
//                            var2.domain.remove(Integer.valueOf(i + n*j2));
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
                int width = n / 2;
                for (int a = 0; a < width; a++) {
                    for (int b = 0; b < n; b++) {
                        vars[0].domain.remove(Integer.valueOf(b + a * n));
                    }
                }
                return vars;
            }else {
                return vars;
            }
        }
    }
}
