package solutionfinder;

import graph.TaskGraph;
import solutionfinder.data.Solution;

import java.util.concurrent.*;

/**
 * Class that is used to find the optimal schedule using a parallel A*.
 *
 * @author Terence Qu
 */
public class AStarParallelSolutionFinder extends AStarSolutionFinder {

    public final int MAX_THREADS;
    public ConcurrentHashMap<Integer, Thread> threads;
    private int threadId;

    public AStarParallelSolutionFinder(int numProcessors, TaskGraph taskGraph, int maxThreads) {
        super(numProcessors, taskGraph);
        this.threadId = 0;
        this.threads = new ConcurrentHashMap<>();
        this.MAX_THREADS = maxThreads;
    }

    /**
     * Finds the optimal solution.
     */
    public Solution findOptimal() throws InterruptedException {
        Solution emptySolution = new Solution(taskGraph, numProcessors);

        PriorityBlockingQueue<Solution> open = new PriorityBlockingQueue<Solution>(1000, new SolutionHeuristicComparator());
        PriorityBlockingQueue<Solution> closed = new PriorityBlockingQueue<Solution>(1000, new SolutionHeuristicComparator());

        open.add(emptySolution);

        // Main loop
        while (!open.isEmpty()) {
            Solution s;
            synchronized (this) {
                s = open.take();
            }
            solutionsExplored++;
            partialSolution = s;
            lastExaminedHeuristic = s.getHeuristic();

            // If complete solution is found, return it
            if (s.getTasksLeft().isEmpty() && (optimalSolution == null || s.getTotalTime() < optimalSolution.getTotalTime())) {
                optimalSolution = s;
            }

            // Check if should spawn a new thread for solution expansion
            if(optimalSolution == null || s.getTotalTime() < optimalSolution.getTotalTime()){
                if (threads.size() < MAX_THREADS && open.size() > MAX_THREADS) {
                    BranchThread thread = new BranchThread();
                    thread.passParameters(this, threadId++, s, open, closed);
                    thread.start();
                } else {
                    this.expandSolution(s, open, closed);
                }
            }
        }
        for (Thread t : threads.values()) {
            t.join();
        }
        return optimalSolution;
    }

    /**
     * Class that extends thread which does the expansion logic for a specific solution.
     */
    private class BranchThread extends Thread {
        private AStarParallelSolutionFinder a;
        private int id;
        private Solution solution;
        private PriorityBlockingQueue<Solution> open;
        private PriorityBlockingQueue<Solution> closed;

        public void passParameters(AStarParallelSolutionFinder a, int id, Solution solution, PriorityBlockingQueue<Solution> open,  PriorityBlockingQueue<Solution> closed) {
            a.threads.put(id, this);
            this.a = a;
            this.id = id;
            this.solution = solution;
            this.open = open;
            this.closed = closed;
        }

        @Override
        public void run() {
            try {
                expandSolution(solution, open, closed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            a.threads.remove(id);
        }
    }
}