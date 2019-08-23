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
        LinkedBlockingDeque<Solution> closed = new LinkedBlockingDeque<Solution>();
        Solution emptySolution = new Solution(taskGraph, numProcessors);
        PriorityBlockingQueue<Solution> open = new PriorityBlockingQueue<Solution>(1000, new SolutionHeuristicComparator());
        open.add(emptySolution);
        while (!open.isEmpty() || !threads.isEmpty()) {
            Solution s = open.take();
            solutionsExplored++;
            if (s.getTasksLeft().isEmpty() && (closed.isEmpty() || s.getTotalTime() < closed.peekFirst().getTotalTime())) {
                System.out.println("Found complete solution with cost " + s.getTotalTime());
                System.out.println("Stack size is " + open.size());
                closed.putFirst(s);
            }
            // Check if s is worth investigating
            if (closed.isEmpty() || s.getTotalTime() <= closed.peekFirst().getTotalTime()) {
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
        return closed.peek();
    }

    /**
     * Class that extends thread which does the expansion logic for a specific solution.
     */
    private class BranchThread extends Thread {
        private AStarParallelSolutionFinder a;
        private int id;
        private Solution solution;
        private PriorityBlockingQueue<Solution> open;

        public BranchThread(AStarParallelSolutionFinder a, int id, Solution solution, PriorityBlockingQueue<Solution> open) {
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