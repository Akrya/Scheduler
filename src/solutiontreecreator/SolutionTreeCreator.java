package solutiontreecreator;

import org.graphstream.graph.implementations.DefaultGraph;

import data.solutiontree.SolutionTree;

public class SolutionTreeCreator {
	
	/**
	 * Method to generate a solution tree from a task graph.
	 * 
	 * UNFINISHED
	 * @return solution tree generated from input task graph
	 */
	public static DefaultGraph convertTaskGraphToSolutionTree(DefaultGraph taskGraph) {
		
		// Initialization
		DefaultGraph solutionTree = new DefaultGraph("Solution Tree");
		
		// Find the first node in the 
		taskGraph.getNode(0);
		
		return solutionTree;
		
	}
}
