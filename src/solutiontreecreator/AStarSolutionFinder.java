package solutiontreecreator;

import graph.TaskGraph;
import org.graphstream.graph.Node;
import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionNode;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Class that is used to find the optimal schedule using a parallel A*.
 *
 * @author Terence Qu
 */
public class AStarSolutionFinder {

    public static final int MAX_THREADS = 8;
    private final int numProcessors;
    private TaskGraph taskGraph;
    public ConcurrentHashMap<Integer, Thread> threads;
    private static int threadId = 0;

    public AStarSolutionFinder(int numProcessors, TaskGraph taskGraph) {
        this.numProcessors = numProcessors;
        this.taskGraph = taskGraph;
        threads = new ConcurrentHashMap<Integer, Thread>();
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
        BlockingDeque<Solution> optimalSolutions = new LinkedBlockingDeque<>();

        Solution emptySolution = new Solution(taskGraph, numProcessors);
        BlockingDeque<Solution> open = new LinkedBlockingDeque<Solution>();
        open.push(emptySolution);
        while (!open.isEmpty()) {
            Solution s = open.takeFirst();
            if (s.getTasksLeft().isEmpty()) {
                if (optimalSolutions.isEmpty() || s.getTotalTime() < optimalSolutions.peek().getTotalTime()) {
                    System.out.println("Found optimal solution with cost " + s.getTotalTime());
                    System.out.println("BlockingDeque size is now " + open.size());
                    optimalSolutions.putFirst(s);
                }
            }
            // Check if s is worth investigating
            if (optimalSolutions.isEmpty() || s.getTotalTime() <= optimalSolutions.peekFirst().getTotalTime()) {
                // Check if should spawn a new thread
                if (threads.size() < MAX_THREADS && open.size() > MAX_THREADS) {
                    BranchThread thread = new BranchThread(this, threadId++, s, open);
                    thread.start();
                } else {
                    expandSolution(s, open);
                }
            }
        }
        for (Thread t : threads.values()) {
            t.join();
        }
        return optimalSolutions.peek();
    }

    /**
     * Class that extends thread which does the expansion logic for a specific solution.
     */
    private class BranchThread extends Thread {
        private AStarSolutionFinder a;
        private int id;
        private Solution solution;
        private BlockingDeque<Solution> open;

        public BranchThread(AStarSolutionFinder a, int id, Solution solution, BlockingDeque<Solution> open){
            this.a = a;
            this.id = id;
            this.solution = solution;
            this.open = open;
        }

        @Override
        public void run() {
            a.threads.put(id, this);
            //System.out.println("Adding a thread to leave " + (threads.size()) + " amount of threads left.");
            try {
                expandSolution(solution, open);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("Thread finished to leave " + (threads.size()-1) + " amount of threads left.");
            a.threads.remove(id);
        }
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
                    if (open.isEmpty()) {
                        open.putFirst(child);
                    } else {
                        Stack<Solution> sortingStack = new Stack<Solution>();
                        boolean placeFound = false;
                        while (!placeFound) {
                            if (!open.isEmpty()) {
                                Solution soln = open.takeFirst();
                                sortingStack.push(soln);
                                if (child.getHeuristic() < soln.getHeuristic()) {
                                    open.putFirst(child);
                                    placeFound = true;
                                }
                            } else {
                                open.putFirst(child);
                                placeFound = true;
                            }
                        }
                        // Restack the sorting stack to open stack
                        while (!sortingStack.isEmpty()) {
                            open.push(sortingStack.pop());
                        }
                    }
                }
            }
        }
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