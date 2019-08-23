package solutionfinder;

import com.sun.jmx.remote.internal.ArrayQueue;
import graph.TaskGraph;
import jdk.nashorn.internal.ir.Block;
import org.graphstream.graph.Node;
import solutionfinder.data.Solution;
import sun.security.provider.NativePRNG;

import java.util.*;
import java.util.concurrent.*;

/**
 * Class that is used to find the optimal schedule using a parallel A*.
 *
 * @author Terence Qu
 */
public class AStarSolutionFinder {

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
        BlockingDeque<Solution> closed = new LinkedBlockingDeque<Solution>();
        Solution emptySolution = new Solution(taskGraph, numProcessors);
        PriorityBlockingQueue<Solution> open = new PriorityBlockingQueue<Solution>(1000, new SolutionHeuristicComparator());
        open.add(emptySolution);
        while (!open.isEmpty()) {
            Solution s;
            synchronized (this) {
                s = open.take();
            }
            solutionsExplored++;
            if (s.getTasksLeft().isEmpty() && (closed.isEmpty() || s.getTotalTime() < closed.peekFirst().getTotalTime())) {
                if(closed.peek() != null){
                    System.out.println("Found complete solution with cost " + s.getTotalTime());
                }
                System.out.println("Stack size is " + open.size());
                closed.putFirst(s);
            }
            // Check if solution needs to be pruned
            boolean flag1 = true;
            if (!closed.isEmpty()) {
                flag1 = s.getTotalTime() <= closed.peek().getTotalTime();
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
    public void expandSolution(Solution s, PriorityBlockingQueue<Solution> open) throws InterruptedException {
        // System.out.println("Stack size is now "+open.size());
        // Expand s's children
        for (Node n : s.getTasksLeft()) {
            for (int i = 0; i < numProcessors; i++) {
                Solution child = this.createCopy(s);
                boolean addSuccess = child.addTask(n, i);
                // Place the child in the proper location in the open array
                if (addSuccess) {
                    if (child != null) {
                        insertSolutionIntoDeque(child, open);
                    }
                }
            }
        }
    }

    /**
     * Helper method to check if there is an equivalent solution to s in open.
     *
     * @param s    Solution to check for
     * @param open Deque containing a collection of solutions
     */
    public static void checkEquivalent(Solution s, PriorityBlockingQueue<Solution> open) {
        BlockingDeque<Solution> tempQueue = new LinkedBlockingDeque<Solution>();
        tempQueue.addAll(open);

        double tmax = s.getTotalTime();

    }

    /**
     * Inserts a solution into a double ended queue in such a way that the heuristics are in order.
     */
    public void insertSolutionIntoDeque(Solution s, PriorityBlockingQueue<Solution> open) throws InterruptedException {
        // Check if the queue has any duplicates.
        for (Solution openS : open) {
            if (openS.equals(s)) {
                return;
            }
        }

        open.add(s);
    }

    /**
     * Stack printing function for debugging purposes
     */
    public void printStack(PriorityBlockingQueue<Solution> stack) {
        Stack<Solution> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            Solution s = stack.remove();
            tempStack.push(s);
            System.out.print(s.getHeuristic() + ", ");
        }

        while (!tempStack.isEmpty()) {
            Solution s = tempStack.pop();
            stack.add(s);
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

    protected class SolutionHeuristicComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Solution s1 = (Solution)o1;
            Solution s2 = (Solution)o2;
            if(s1.getHeuristic() < s2.getHeuristic()){
                return 1;
            } else if (s1.getHeuristic() > s2.getHeuristic()){
                return -1;
            }
            return 0;
        }
    }
}