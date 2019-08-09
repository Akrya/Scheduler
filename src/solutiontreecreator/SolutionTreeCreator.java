package solutiontreecreator;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Node;

import graph.TaskGraph;
import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionNode;

/**
 * Class that is used to create a solution tree from a task graph.
 * 
 * @author Terence Qu
 * 
 */
public class SolutionTreeCreator {

	public final int numProcessors;
	private TaskGraph taskGraph;
	
	private SolutionNode root;
	
	public List<Node> tasks;
	
	public SolutionTreeCreator(int numProcessors, TaskGraph taskGraph) {
		this.numProcessors = numProcessors;
		this.taskGraph = taskGraph;
		
		tasks = new ArrayList<Node>(taskGraph.getNodeSet());
	}

	/**
	 * Method to generate a solution tree from a task graph.
	 * 
	 * @return solution tree generated from input task graph
	 */
	public void buildSolutionTree() {
		root = new SolutionNode();
		root.solution = new Solution(taskGraph, numProcessors);
		
		branchOut(root);
	}
	
	/**
	 * Recursive function for creation of solution tree.
	 * 
	 * @param root
	 */
	public void branchOut(SolutionNode root) {
		for(Node n: root.solution.getTasksLeft()) {
			for(int i = 0; i < numProcessors; i++) {
				SolutionNode child = createCopy(root);
				if(child.solution.addTask(n, i)) {
					root.addChild(child);
				}
			}
		}
		
		for(SolutionNode s: root.children) {
			branchOut(s);
		}
	}
	
	/**
	 * Getter for tree.
	 * @return
	 */
	public SolutionNode getTreeRoot() {
		return this.root;
	}
	
	/**
	 * Helper function for creating a copy of a solution node.
	 */
	public SolutionNode createCopy(SolutionNode node) {
		SolutionNode solutionNode = new SolutionNode();
		solutionNode.solution = new Solution(taskGraph, numProcessors);
		
		solutionNode.solution.setTaskList(new ArrayList<Node>(node.solution.getTaskList()));
		solutionNode.solution.setTasksLeft(new ArrayList<Node>(node.solution.getTasksLeft()));
		solutionNode.solution.setCurrentProcessor(node.solution.getCurrentProcessor());
		
		// Copy all of solutionNode's processor data to the copy
		for(int i = 0; i < solutionNode.solution.getNumProcessors(); i++) {
			solutionNode.solution.getProcessor(i).mapOfTasksAndStartTimes
				.putAll(node.solution.getProcessor(i).mapOfTasksAndStartTimes);
			solutionNode.solution.getProcessor(i).setEndTime(node.solution.getProcessor(i).getEndTime());
		}
		
		return solutionNode;
	}
}