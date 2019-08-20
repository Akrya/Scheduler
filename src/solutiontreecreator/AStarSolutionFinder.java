package solutiontreecreator;

import graph.TaskGraph;
import org.graphstream.graph.Node;
import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionNode;

import java.util.*;

/**
 * Class that is used to create a solution tree from a task graph.
 *
 * @author Terence Qu
 */
public class AStarSolutionFinder {

    private final int numProcessors;
    private TaskGraph taskGraph;

    private SolutionNode root;

    private List<Node> tasks;

    public AStarSolutionFinder(int numProcessors, TaskGraph taskGraph) {
        this.numProcessors = numProcessors;
        this.taskGraph = taskGraph;

        tasks = new ArrayList<Node>(taskGraph.getNodeSet());
    }

    /**
     * Method to generate a solution tree from a task graph.
     *
     * @return solution tree generated from input task graph
     */
    public Solution startOptimalSearch() {


        return findOptimal();
    }

    /**
     * Finds the optimal solution
     */
    public Solution findOptimal() {
        Stack<Solution> optimalSolutions = new Stack<Solution>();

        Solution emptySolution = new Solution(taskGraph, numProcessors);
        Stack<Solution> open = new Stack<Solution>();
        open.push(emptySolution);
        while (!open.isEmpty()) {
            Solution s = open.pop();
            if (s.getTasksLeft().isEmpty()) {
                if (optimalSolutions.isEmpty() || s.getTotalTime() < optimalSolutions.peek().getTotalTime()) {
                    System.out.println("Found optimal solution with cost " + s.getTotalTime());
                    System.out.println("Stack size is now "+open.size());
                    optimalSolutions.push(s);
                }
            }
            // Check if s needs to be pruned
            if (optimalSolutions.isEmpty() || s.getTotalTime() <= optimalSolutions.peek().getTotalTime()) {
                // Expand s's children
                for (Node n : s.getTasksLeft()) {
                    for (int i = 0; i < numProcessors; i++) {
                        Solution child = createCopy(s);
                        boolean addSuccess = child.addTask(n, i);
                        // Place the child in the proper location in the open array
                        if (addSuccess) {
                            if (open.isEmpty()) {
                                open.push(child);
                            } else {
                                Stack<Solution> sortingStack = new Stack<Solution>();
                                boolean placeFound = false;
                                while (!placeFound) {
                                	if(!open.isEmpty()){
										Solution soln = open.pop();
										sortingStack.push(soln);
										if (child.getHeuristic() < soln.getHeuristic()) {
											open.push(child);
											placeFound = true;
										}
									} else {
                                		open.push(child);
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
            } else {
				System.out.println("Stack size is now "+open.size());
            }
        }
        return optimalSolutions.peek();
    }

    /**
     * Getter for tree.
     *
     * @return
     */
    public SolutionNode getTreeRoot() {
        return this.root;
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