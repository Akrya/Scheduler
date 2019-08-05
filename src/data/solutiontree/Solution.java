package data.solutiontree;

import org.graphstream.graph.implementations.AdjacencyListNode;

public class Solution extends AdjacencyListNode{
	
	private int numProcessors;
	
	/**
	 * Represents a solution, a series of tasks scheduled amongst processors
	 * @param numProcessors
	 */
	public Solution(SolutionTree tree, String id, int numProcessors) {
		super(tree, id);
		this.numProcessors = numProcessors;
	}
	
	/**
	 * Method which enables the contents of a solution to be copied to another.
	 * @param inSolution
	 * @return
	 */
	public Solution copySolution(Solution inSolution) {
		return null;
	}
}
