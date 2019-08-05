package solutiontreecreator;

import java.util.List;

import org.graphstream.graph.Node;

import data.solutiontree.SolutionTree;
import graph.TaskGraph;

public class SolutionTreeCreator {
	
	private int idCount; 
	private int numProcessors;
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
		SolutionTree solutionTree = new SolutionTree("Solution Tree");
		List<Node> rootNodes = taskGraph.getRootNodes();
		
		// Main loop
		for(Node n: rootNodes) {
			solutionTree.addNode(""+idCount++);
		}
		
		return solutionTree;
	}
}
