package solutiontreecreator;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import graph.TaskGraph;
import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionTree;
import solutiontreecreator.data.Timeline;

public class SolutionTreeCreator {

	private int idCount; 
	public final int numProcessors;
	private TaskGraph taskGraph;

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
	public SolutionTree convertTaskGraphToSolutionTree() {

		// Initialization
		SolutionTree solutionTree = new SolutionTree("Solution Tree", numProcessors);
		List<Node> rootNodes = taskGraph.getRootNodes();

		// For the root nodes, only add them to the first node
		for(Node rootNode: rootNodes) {
			Solution solution = new Solution(numProcessors);
			solution.addTaskNoDelay(rootNode, 0);
			solutionTree.addNode(""+idCount++).addAttribute("Solution", solution);
			
		}



		return solutionTree;
	}

	public void branchOut(Node n) {
		// Get a list of all child nodes
		List<Node> childNodes = new ArrayList<Node>();

		for(Edge edge: n.getLeavingEdgeSet()) {
			childNodes.add(edge.getTargetNode());
			
		}
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
}
