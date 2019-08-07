package solutiontreecreator;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import graph.TaskGraph;
import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionNode;
import solutiontreecreator.data.SolutionTree;
import solutiontreecreator.data.Timeline;

public class SolutionTreeCreator {

	private int idCount; 
	public final int numProcessors;
	private TaskGraph taskGraph;
	private SolutionTree solutionTree;
	
	public List<Node> tasks;
	
	public List<SolutionNode> leaves;
	
	public SolutionTreeCreator(int numProcessors, TaskGraph taskGraph) {
		idCount = 0;

		this.numProcessors = numProcessors;
		this.taskGraph = taskGraph;
		
		tasks = new ArrayList<Node>(taskGraph.getNodeSet());
		
		this.leaves = new ArrayList<SolutionNode>();
	}

	/**
	 * Method to generate a solution tree from a task graph.
	 * 
	 * UNFINISHED
	 * @return solution tree generated from input task graph
	 */
	public void convertTaskGraphToSolutionTree() {
		// Initialization
		solutionTree = new SolutionTree(numProcessors);
		Node rootTask = taskGraph.getRootNodes().get(0);
		System.out.println("The root task of the tree is "+rootTask.getId());
		
		// Create the root node of the tree.
		SolutionNode rootNode = new SolutionNode();
		rootNode.solution = new Solution(numProcessors);
		solutionTree.root = rootNode;
		
		// Branch out from the root node
		for(int i = 0; i < numProcessors; i++) {
			rootNode.addChild(new SolutionNode());
			rootNode.getChild(i).latestTask = rootTask;
			rootNode.getChild(i).solution = new Solution(numProcessors);
			rootNode.getChild(i).solution.addTask(rootTask, 0, i);
		}
		
		for(SolutionNode s: rootNode.children) {
			branchOut(s, null);
		}
	}
	
	/**
	 * Helper recursive function for solution tree generation.
	 * 
	 * INCORRECT, THIS NEEDS TO BE ALTERED
	 * @param node
	 */
	public void branchOut(SolutionNode node, List<Node> availableTasks) {
		System.out.println("Branching out from "+node.latestTask.getId());
		
		// Branch out from node - FOR LOOP NEEDS TO LOOP THROUGH ALL AVAILABLE TASKS
		for(Edge e: node.latestTask.getEachLeavingEdge()) {
			for(int i = 0; i < numProcessors; i++) {
				SolutionNode childSolution = new SolutionNode();
				childSolution.latestTask = e.getTargetNode();
				childSolution.solution = copySolution(node.solution);
				childSolution.solution.addTask(e.getTargetNode(), e.getAttribute("Weight"), i);
				node.addChild(childSolution);
			}
		}
		
		for(SolutionNode s: node.children) {
			branchOut(s, null);
		}
		
		if(node.children.isEmpty()) {
			leaves.add(node);
		}
		
		System.out.println("Branch end.");
	}

	/**
	 * Helper method to copy one solution to another
	 * @param inSolution
	 * @return
	 */
	public static Solution copySolution(Solution solution) {
		Solution copyOfSolution = new Solution(solution.numProcessors);

		// Copy over all of the data from input Solution object to new Solution object
		for(int i = 0; i < solution.numProcessors; i++) {
			Timeline timeline = solution.processors[i];
			Timeline copyTimeline = copyOfSolution.processors[i];

			copyTimeline.mapOfTasksAndStartTimes.putAll(timeline.mapOfTasksAndStartTimes);
		}

		return copyOfSolution;
	}
	
	public SolutionTree getTree() {
		return this.solutionTree;
	}
	
	public List<SolutionNode> optimalSolutions(){
		List<SolutionNode> optimalSolutions = new ArrayList<SolutionNode>();
		double bestTime = 10000000;
		
		// Simple algorithm to find the least cost solutions.
		for(SolutionNode s: this.leaves) {
			if(s.getTotalTime() == bestTime) {
				optimalSolutions.add(s);
			} else if(s.getTotalTime() < bestTime) {
				optimalSolutions.clear();
				optimalSolutions.add(s);
			}
		}
		
		return optimalSolutions;
	}
}