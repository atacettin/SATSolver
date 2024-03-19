package src;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StandardCombinatorics {
    /**
     * Returns a list of all binary strings of length n
     */
    public static List<String> getBinaryStrings(int n) {
        // Initialize lists for variables and constraints
        Solver.Constraint[] constraints = new Solver.Constraint[0];

        Solver.Variable[] variables = new Solver.Variable[n];
        for(int i = 0; i < n; i++){
            List<Integer> domain = new ArrayList<>();
            domain.add(0);
            domain.add(1);
            variables[i] = new Solver.Variable(domain);
        }

        // Use solver
        Solver solver = new Solver(variables, constraints);
        List<int[]> result = solver.findAllSolutions(n);

        List<String> theActualResult = new ArrayList<>();
        for(int[] entry : result){
            StringBuilder sb = new StringBuilder();
            for (int num : entry) {
                sb.append(num);
            }
            theActualResult.add(sb.toString());
        }

        return theActualResult;
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} without repetitions
     */
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // variables
        List<Integer> domain = IntStream.range(1,n+1).boxed().collect(Collectors.toList());
        System.out.println(domain);
        for(int i = 0; i < k; i++){
            variables.add(new Solver.Variable(new ArrayList<>(domain)));
        }

        // constraints
        constraints.add(new Solver.OrderingConstraint());
        constraints.add(new Solver.UniquenessConstraint());

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions(k);

        return result;
    }

    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} with repetitions
     */
    public static List<int[]> getCombinationsWithRepetition(int n, int k) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // variables
        List<Integer> domain = IntStream.range(1,n+1).boxed().collect(Collectors.toList());
        System.out.println(domain);
        for(int i = 0; i < k; i++){
            variables.add(new Solver.Variable(new ArrayList<>(domain)));
        }

        // constraints
        constraints.add(new Solver.OrderingConstraint());

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions(k);

        return result;
    }

    /**
     * Returns a list of all subsets in the set {1,...,n}
     */
    public static List<int[]> getSubsets(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // variables
        List<Integer> domain = new ArrayList<>();
        domain.add(0);
        domain.add(1);
        for(int i = 0; i < n; i++){
            variables.add(new Solver.Variable(new ArrayList<>(domain)));
        }

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions(n);
        List<int[]> newResult = new ArrayList<>();
        for(int[] res : result){
            int[] idx = IntStream.range(1, res.length + 1)
                    .filter(x -> res[x-1] == 1)
                    .toArray();
            newResult.add(idx);
//            for(int i : idx){
//                System.out.print(i);
//            }
//            System.out.print(" ");
//            for(int i : res){
//                System.out.print(i);
//            }
//            System.out.println();
        }

        return newResult;
    }

    /**
     * Returns a list of all permutations in the set {1,...,n}
     */
    public static List<int[]> getSetPermutations(int n) {
        // Initialize lists for variables and constraints
        List<Solver.Variable> variables = new ArrayList<>();
        List<Solver.Constraint> constraints = new ArrayList<>();

        // variables
        List<Integer> domain = IntStream.range(1,n+1).boxed().collect(Collectors.toList());
        for(int i = 0; i < n; i++){
            variables.add(new Solver.Variable(new ArrayList<>(domain)));
        }

        // constraints
        constraints.add(new Solver.UniquenessConstraint());

        // Convert to arrays
        Solver.Variable[] variablesArray = new Solver.Variable[variables.size()];
        variablesArray = variables.toArray(variablesArray);
        Solver.Constraint[] constraintsArray = new Solver.Constraint[constraints.size()];
        constraintsArray = constraints.toArray(constraintsArray);

        // Use solver
        Solver solver = new Solver(variablesArray, constraintsArray);
        List<int[]> result = solver.findAllSolutions(n);

        return result;
    }
}
