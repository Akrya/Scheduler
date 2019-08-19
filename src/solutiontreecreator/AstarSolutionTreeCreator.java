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
 * 
 */
public class AstarSolutionTreeCreator {

	private final int numProcessors;
	private TaskGraph taskGraph;

	private SolutionNode root;

	private List<Node> tasks;

	public AstarSolutionTreeCreator(int numProcessors, TaskGraph taskGraph) {
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
		Deque<Solution> optimalSolutions = new ArrayDeque<Solution>();

		Solution emptySolution = new Solution(taskGraph, numProcessors);
		Deque<Solution> open = new ArrayDeque<Solution>();
		open.addFirst(emptySolution);
		while(!open.isEmpty()){
			Solution s = open.removeFirst();
			if(s.getTasksLeft().isEmpty()){
				if(optimalSolutions.isEmpty() || s.getTotalTime() < optimalSolutions.peekFirst().getTotalTime()) {
					System.out.println("Found optimal solution with cost "+s.getTotalTime());
					optimalSolutions.addFirst(s);
				}
			}
			// Check if s needs to be pruned
			if(optimalSolutions.isEmpty() || s.getTotalTime() <= optimalSolutions.peekFirst().getTotalTime()) {
				// Expand s's children
				for (Node n : s.getTasksLeft()) {
					for (int i = 0; i < numProcessors; i++) {
						Solution child = createCopy(s);
						boolean addSuccess = child.addTask(n, i);
						if (addSuccess) {
							if (open.isEmpty()) {
								open.addFirst(child);
							} else if (child.getHeuristic() < open.peekFirst().getHeuristic()) {
								open.addFirst(child);
							} else {
								open.addLast(child);
							}
						}
					}
				}
			} else {
				System.out.println("Solution pruned with time: "+ s.getTotalTime());
			}
		}
		return optimalSolutions.peekFirst();
	}
	
	/**
	 * Getter for tree.
	 * @return
	 */
	public SolutionNode getTreeRoot() {
		return this.root;
	}

	/**
	 * Helper function for creating a copy of a solution.
	 * @param solution
	 * @return
	 */
	public Solution createCopy(Solution solution){
		Solution solutionCopy = new Solution(taskGraph, numProcessors);;

		solutionCopy.setTaskList(new ArrayList<Node>(solution.getTaskList()));
		solutionCopy.setTasksLeft(new ArrayList<Node>(solution.getTasksLeft()));
		solutionCopy.setCurrentProcessor(solution.getCurrentProcessor());

		// Copy all of solutionNode's processor data to the copy
		for(int i = 0; i < solution.getNumProcessors(); i++) {
			solutionCopy.getProcessor(i).mapOfTasksAndStartTimes
					.putAll(solution.getProcessor(i).mapOfTasksAndStartTimes);
			solutionCopy.getProcessor(i).setEndTime(solution.getProcessor(i).getEndTime());
		}

		return solutionCopy;
	}
}