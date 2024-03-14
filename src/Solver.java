package src;

import java.util.*;

class Solver {
    static class Variable {
        List<Integer> domain;
        // you can add more attributes

        /**
         * Constructs a new variable.
         * @param domain A list of values that the variable can take
         */
        public Variable(List<Integer> domain) {
            this.domain = domain;
        }
    }

    static abstract class Constraint {
        /**
         * Tries to reduce the domain of the variables associated to this constraint, using inference
         */
        abstract Variable[] infer(/* you can add params */Variable[] vars);
    }

    // Example implementation of the Constraint interface.
    // It enforces that for given variable X, it holds that 5 < X < 10.
    //
    // This particular constraint will most likely not be very useful to you...
    // Remove it and design a few constraints that *can* help you!
//    static abstract class BetweenFiveAndTenConstraint {
//        Variable var;
//
//        public BetweenFiveAndTenConstraint(Variable var) {
//            this.var = var;
//        }
//
//        Variable[] infer() {
//            List<Integer> newDomain = new LinkedList<>();
//
//            for (Integer x : this.var.domain) {
//                if (5 < x && x < 10)
//                    newDomain.add(x);
//            }
//
//            this.var.domain = newDomain;
//        }
//    }

    Variable[] variables;
    LinkedList<Constraint> constraints;
    List<int[]> solutions;
    // you can add more attributes

    /**
     * Constructs a solver.
     * @param variables The variables in the problem
     * @param constraints The constraints applied to the variables
     */
    public Solver(Variable[] variables, Constraint[] constraints) {
        this.variables = variables;
        this.constraints = new LinkedList<>();
        for(Constraint constraint : constraints){
            this.constraints.add(constraint);
        }

        solutions = new LinkedList<>();
    }

    /**
     * Searches for one solution that satisfies the constraints.
     * @return The solution if it exists, else null
     */
    int[] findOneSolution(int depth) {
        solve(false, depth);

        return !solutions.isEmpty() ? solutions.get(0) : null;
    }

    /**
     * Searches for all solutions that satisfy the constraints.
     * @return The solution if it exists, else null
     */
    List<int[]> findAllSolutions(int depth) {
        solve(true, depth);
        return solutions;
    }

    /**
     * Main method for solving the problem.
     * @param findAllSolutions Whether the solver should return just one solution, or all solutions
     */
    void solve(boolean findAllSolutions, int depth) {
        // here you can do any preprocessing you might want to do before diving into the search
        List<Integer> choice = new ArrayList<>();
        search(findAllSolutions, 0, choice, depth);
    }

    /**
     * Solves the problem using search and inference.
     */
    void search(boolean findAllSolutions, int level, List<Integer> choice, int depth) {
        // TODO: implement search using search and inference
        Variable[] vars = copy(this.variables);
        for(Constraint constraint : constraints){
            vars = constraint.infer(vars);
        }
        for(int i : vars[level].domain){
            choice.add(i);
            if(choice.size()==depth) {
                solutions.add(choice.stream().mapToInt(x -> x).toArray());
                if (findAllSolutions) {
                    choice.remove(choice.size()-1);
                    continue;
                } else {
                    return;
                }
            }
            constraints.push(new ValueConstraint(i, level));
            search(findAllSolutions, level + 1, choice, depth);
            constraints.pop();
            choice.remove(choice.size()-1);
        }
        return;
    }

    Variable[] copy(Variable[] variables){
        Variable[] newCopy = new Variable[variables.length];
        int i = 0;
        for(Variable var : variables){
            List<Integer> domain = new ArrayList<>(var.domain);
            Variable newVar = new Variable(domain);
            newCopy[i] = newVar;
            i++;
        }
        return newCopy;
    }
    static class ValueConstraint extends Constraint{
        int value;
        int level;

        public ValueConstraint(int value, int level) {
            this.value = value;
            this.level = level;
        }

        @Override
        Variable[] infer(Variable[] vars) {
            List<Integer> domain = List.copyOf(vars[level].domain);

            for (int i : domain) {
                if (i != value) vars[level].domain.remove(i);
            }
            return vars;
        }
    }
}