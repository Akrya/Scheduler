package solutiontreecreator.data;

import org.graphstream.graph.implementations.AdjacencyListNode;

public class Solution extends AdjacencyListNode{
	
	public final int numProcessors;
	public final Timeline[] processors;
	
	/**
	 * Represents a solution, a series of tasks scheduled amongst processors
	 * @param numProcessors
	 */
	public Solution(SolutionTree tree, String id, int numProcessors) {
		super(tree, id);
		this.numProcessors = numProcessors;
		processors = new Timeline[numProcessors];
	}
	
	
}
