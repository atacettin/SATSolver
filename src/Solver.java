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
        int[] choice = new int[variables.length];
        int length = 0;
        search(findAllSolutions, 0, choice, length, depth, variables);
    }

    /**
     * Solves the problem using search and inference.
     */
    void search(boolean findAllSolutions, int level, int[] choice, int length, int depth, Variable[] variables) {
        Variable[] vars = copy(variables);
        for(Constraint constraint : constraints){
            vars = constraint.infer(vars);
        }
        for(int i : vars[level].domain){
            choice[length] = i;
            length++;
            vars[level].domain =  new ArrayList<>(List.of(i));
            if(length==depth) {
                solutions.add(choice.clone());
                if (findAllSolutions) {
                    length--;
                    continue;
                } else {
                    return;
                }
            }
            search(findAllSolutions, level + 1, choice, length, depth, vars);
            length--;
        }
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
    static class UniquenessConstraint extends Constraint{

        public UniquenessConstraint() {}

        @Override
        Variable[] infer(Variable[] vars) {
            // for each variable, if it has a domain of size 1
            //    for each other variable, remove that value from the domain
            for(Variable var : vars){
                if(var.domain.size() != 1){ continue; } // only matters for variables that are picked
                int val = var.domain.get(0);
                if(val == -1){continue;}
                for(Variable other : vars) {
                    if(var != other){
                        List<Integer> oDomain = other.domain;
                        for(int i = 0; i < oDomain.size(); i++){
                            if(oDomain.get(i) > val){
                                break;
                            }
                            if(oDomain.get(i) == val){
                                oDomain.remove(i);
                                break;
                            }

                        }
//                        other.domain.removeIf(x -> x == val);
                    }
                }
            }
            return vars;
        }
    }

    static class OrderingConstraint extends Constraint{
        public OrderingConstraint(){}

        @Override
        Variable[] infer(Variable[] vars){
            for(int i = 0; i < vars.length-1; i++){
                if(!vars[i].domain.isEmpty()){
                    int val = vars[i].domain.get(0);
                    vars[i+1].domain.removeIf(x-> x<val);
                }
            }
            return vars;
        }
    }
}