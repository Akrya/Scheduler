package solutiontreecreator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import graph.TaskGraph;
import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionNode;
import solutiontreecreator.data.SolutionTree;
import solutiontreecreator.data.Timeline;

public class SolutionTreeCreator {

	public static int nodeCount = 0;
	private int idCount; 
	public final int numProcessors;
	private TaskGraph taskGraph;
	private SolutionTree solutionTree;
	
	public SolutionTreeCreator(int numProcessors, TaskGraph taskGraph) {
		idCount = 0;

		this.numProcessors = numProcessors;
		this.taskGraph = taskGraph;
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
			branchOut(rootNode.getChild(i));
			System.out.println(nodeCount);
		}
	}
	
	/**
	 * Helper recursive function for solution tree generation.
	 * @param node
	 */
	public void branchOut(SolutionNode node) {
		System.out.println("Branching out from "+node.latestTask.getId());
		
		// Branch out from node
		for(Edge e: node.latestTask.getEachLeavingEdge()) {
			for(int i = 0; i < numProcessors; i++) {
				node.addChild(new SolutionNode());
				node.getChild(i).latestTask = e.getTargetNode();
				node.getChild(i).solution = copySolution(node.solution);
				node.getChild(i).solution.addTask(e.getTargetNode(), e.getAttribute("Weight"), i);
				
				nodeCount++;
				
				branchOut(node.getChild(i));
			}
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
}