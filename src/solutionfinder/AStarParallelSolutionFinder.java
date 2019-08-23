package solutionfinder;

import graph.TaskGraph;
import org.graphstream.graph.Node;
import solutionfinder.data.Solution;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Class that is used to find the optimal schedule using a parallel A*.
 *
 * @author Terence Qu
 */
public class AStarParallelSolutionFinder extends AStarSolutionFinder {

    public static final int MAX_THREADS = 8;
    public ConcurrentHashMap<Integer, Thread> threads;
    private int threadId;

    public AStarParallelSolutionFinder(int numProcessors, TaskGraph taskGraph) {
        super(numProcessors, taskGraph);
        this.threadId = 0;
        this.threads = new ConcurrentHashMap<>();
    }

    /**
     * Finds the optimal solution.
     */
    public Solution findOptimal() throws InterruptedException {
        BlockingDeque<Solution> optimalSolutions = new LinkedBlockingDeque<>();
        Solution emptySolution = new Solution(taskGraph, numProcessors);
        BlockingDeque<Solution> open = new LinkedBlockingDeque<Solution>();
        open.push(emptySolution);
        while (!open.isEmpty() || !threads.isEmpty()) {
            Solution s = open.takeFirst();
            solutionsExplored++;
            if (s.getTasksLeft().isEmpty() && (optimalSolutions.isEmpty() || s.getTotalTime() < optimalSolutions.peek().getTotalTime())) {
                System.out.println("Found optimal candidate with cost " + s.getTotalTime());
                System.out.println("Stack size is " + open.size());
                optimalSolutions.putFirst(s);
            }
            // Check if s is worth investigating
            if (optimalSolutions.isEmpty() || s.getTotalTime() <= optimalSolutions.peekFirst().getTotalTime()) {
                // Check if should spawn a new thread
                if (open.size() > MAX_THREADS && threads.size() < MAX_THREADS) {
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
        private AStarParallelSolutionFinder a;
        private int id;
        private Solution solution;
        private BlockingDeque<Solution> open;

        public BranchThread(AStarParallelSolutionFinder a, int id, Solution solution, BlockingDeque<Solution> open) {
            a.threads.put(id, this);
            this.a = a;
            this.id = id;
            this.solution = solution;
            this.open = open;
        }

        @Override
        public void run() {
            // System.out.println("Adding a thread to leave " + (threads.size()) + " amount of threads left.");
            try {
                expandSolution(solution, open);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            a.threads.remove(id);
        }
    }
}