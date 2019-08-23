package solutionfinder;

import graph.TaskGraph;
import jdk.nashorn.internal.ir.Block;
import org.graphstream.graph.Node;
import solutionfinder.data.Solution;
import sun.security.provider.NativePRNG;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Class that is used to find the optimal schedule using a parallel A*.
 *
 * @author Terence Qu
 */
public class AStarSolutionFinder{

    protected final int numProcessors;
    protected TaskGraph taskGraph;
    protected int solutionsExplored;

    public AStarSolutionFinder(int numProcessors, TaskGraph taskGraph) {
        this.numProcessors = numProcessors;
        this.taskGraph = taskGraph;
        solutionsExplored = 0;
    }

    /**
     * Method to generate a solution tree from a task graph.
     *
     * @return solution tree generated from input task graph
     */
    public Solution startOptimalSearch() {
        try {
            return findOptimal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds the optimal solution.
     */
    public Solution findOptimal() throws InterruptedException {
        BlockingDeque<Solution> closed = new LinkedBlockingDeque<>();
        Solution emptySolution = new Solution(taskGraph, numProcessors);
        BlockingDeque<Solution> open = new LinkedBlockingDeque<Solution>();
        open.push(emptySolution);
        while (!open.isEmpty()) {
            Solution s = open.takeFirst();
            solutionsExplored++;
            if (s.getTasksLeft().isEmpty() && (closed.isEmpty() || s.getTotalTime() < closed.peek().getTotalTime())) {
                System.out.println("Found optimal candidate with cost " + s.getTotalTime());
                System.out.println("Stack size is " + open.size());
                closed.putFirst(s);
            }
            // Check if solution needs to be pruned
            boolean flag1 = true;
            if(!closed.isEmpty()){
                flag1 = s.getTotalTime() <= closed.peekFirst().getTotalTime();
            }

            if (flag1) {
                // Check if s is worth investigating
                expandSolution(s, open);
            }
        }

        return closed.peek();
    }

    /**
     * Helper function for findSolution(). Intended to be run on a seperate thread
     *
     * @param s
     * @param open
     */
    public void expandSolution(Solution s, BlockingDeque<Solution> open) throws InterruptedException {
        // System.out.println("Stack size is now "+open.size());
        // Expand s's children
        for (Node n : s.getTasksLeft()) {
            for (int i = 0; i < numProcessors; i++) {
                Solution child = createCopy(s);
                boolean addSuccess = child.addTask(n, i);
                // Place the child in the proper location in the open array
                if (addSuccess) {
                    insertSolutionIntoDeque(child, open);
                }
            }
        }
    }

    /**
     * Helper method to check if there is an equivalent solution to s in open.
     * @param s Solution to check for
     * @param open Deque containing a collection of solutions
     */
    public static void checkEquivalent(Solution s, BlockingDeque<Solution> open){
        BlockingDeque<Solution> tempQueue = new LinkedBlockingDeque<Solution>();
        tempQueue.addAll(open);

        double tmax = s.getTotalTime();

    }

    /**
     * Inserts a solution into a double ended queue in such a way that the heuristics are in order.
     */
    public synchronized void insertSolutionIntoDeque(Solution s, BlockingDeque<Solution> open) throws InterruptedException {
        if(open.isEmpty()){
            open.putFirst(s);
            return;
        }
        Stack<Solution> sortingStack = new Stack<Solution>();
        boolean placeFound = false;
        while (!placeFound) {
            if(open.isEmpty() || s.getHeuristic() >= open.peek().getHeuristic()){
                open.putFirst(s);
                placeFound = true;
            } else {
                sortingStack.push(open.takeFirst());
            }
        }

        // Restack the sorting stack to open stack
        while (!sortingStack.isEmpty()) {
            open.addFirst(sortingStack.pop());
        }
    }

    /**
     * Stack printing function for debugging purposes
     */
    public void printStack(BlockingDeque<Solution> stack){
        Stack<Solution> tempStack = new Stack<>();
        while(!stack.isEmpty()){
            Solution s = stack.removeFirst();
            tempStack.push(s);
            System.out.print(s.getHeuristic()+", ");
        }

        while(!tempStack.isEmpty()){
            Solution s = tempStack.pop();
            stack.push(s);
        }

        System.out.println("");
    }

    /**
     * Helper function for creating a copy of a solution.
     *
     * @param solution
     * @return
     */
    public Solution createCopy(Solution solution) {
        Solution solutionCopy = new Solution(taskGraph, numProcessors);

        solutionCopy.setTaskList(new ArrayList<Node>(solution.getTaskList()));
        solutionCopy.setTasksLeft(new ArrayList<Node>(solution.getTasksLeft()));
        solutionCopy.setCurrentProcessor(solution.getCurrentProcessor());

        // Copy all of solutionNode's processor data to the copy
        for (int i = 0; i < solution.getNumProcessors(); i++) {
            solutionCopy.getProcessor(i).mapOfTasksAndStartTimes
                    .putAll(solution.getProcessor(i).mapOfTasksAndStartTimes);
            solutionCopy.getProcessor(i).setEndTime(solution.getProcessor(i).getEndTime());
        }

        return solutionCopy;
    }
}