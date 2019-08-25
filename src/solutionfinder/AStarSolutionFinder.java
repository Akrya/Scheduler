package solutionfinder;

import com.jfoenix.utils.JFXUtilities;
import com.sun.jmx.remote.internal.ArrayQueue;
import graph.TaskGraph;
import jdk.nashorn.internal.ir.Block;
import main.GUI;
import main.Main;
import main.controller.GraphViewController;
import main.controller.ViewController;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import solutionfinder.data.Solution;
import sun.security.provider.NativePRNG;

import javax.swing.*;
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
    protected Solution optimalSolution;
    protected double lastExaminedHeuristic;
    protected Solution partialSolution;

    public AStarSolutionFinder(int numProcessors, TaskGraph taskGraph) {
        this.numProcessors = numProcessors;
        this.taskGraph = taskGraph;
        solutionsExplored = 0;
        optimalSolution = null;
        lastExaminedHeuristic = Double.POSITIVE_INFINITY;
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
        Solution emptySolution = new Solution(taskGraph, numProcessors);

        PriorityBlockingQueue<Solution> open = new PriorityBlockingQueue<Solution>(1000, new SolutionHeuristicComparator());
        PriorityBlockingQueue<Solution> closed = new PriorityBlockingQueue<Solution>(1000, new SolutionHeuristicComparator());

        open.add(emptySolution);

        partialSolution = emptySolution;

        // Main loop
        while (!open.isEmpty()) {
            Solution s;
            synchronized (this) {
                s = open.take();
            }
            solutionsExplored++;
            if (s.getHeuristic() != this.lastExaminedHeuristic) {
                // this.printDebugData(s, open, closed);
            }
            lastExaminedHeuristic = s.getHeuristic();

            // If complete solution is found, return it
            if (s.getTasksLeft().isEmpty() && (optimalSolution == null || s.getTotalTime() < optimalSolution.getTotalTime())) {
                optimalSolution = s;
            }

            // Expand the solution
            if (optimalSolution == null || s.getTotalTime() < optimalSolution.getTotalTime()) {
                expandSolution(s, open, closed);
            }
        }
        return optimalSolution;
    }

    /**
     * Helper function for findSolution(). Intended to be run on a seperate thread
     *
     * @param s
     * @param open
     */
    public void expandSolution(Solution s, PriorityBlockingQueue<Solution> open, PriorityBlockingQueue<Solution> closed) throws InterruptedException {
        // System.out.println("Stack size is now "+open.size());
        // Expand s's children
        for (Node n : s.getTasksLeft()) {
            for (int i = 0; i < numProcessors; i++) {
                Solution child = this.createCopy(s);
                boolean addSuccess = child.addTask(n, i);
                // Place the child in the proper location in the open array
                if (addSuccess) {
                    if (child != null) {
                        // Check if there are any duplicates in open or closed
                        for (Solution openS : open) {
                            if (openS.equals(s)) {
                                // System.out.println("Found duplicate in open.");
                                return;
                            }
                        }
                        for (Solution closedS : closed) {
                            if (closedS.equals(s)) {
                                // System.out.println("Found duplicate in closed.");
                                return;
                            }
                        }

                        // System.out.println("Adding element.");
                        // Add the child to open
                        open.add(child);
                    }
                }
            }
        }
        closed.add(s);
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
     * Prints the solution data, and amount of solutions on the stack to the console.
     */
    public void printDebugData(Solution s, PriorityBlockingQueue<Solution> open, PriorityBlockingQueue<Solution> closed) {
        System.out.println("Open size: " + open.size() + " || Closed size: " + closed.size());
        System.out.println("Current solution being examined: "
                + s.getHeuristic()
                + " IdleTime: " + s.calculateFIdleTime()
                + " BottomLevel: " + s.calculateFBottomLevel()
                + " DRT:" + s.calculateFDataReadyTime() +
                " NumOfTasks:" + s.getTaskList().size());
        System.out.println(s.stringData());
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

    public static class SolutionHeuristicComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Solution s1 = (Solution) o1;
            Solution s2 = (Solution) o2;
            if (s1.getHeuristic() > s2.getHeuristic()) {
                return 1;
            } else if (s1.getHeuristic() < s2.getHeuristic()) {
                return -1;
            }
            return 0;
        }
    }
}