package solutiontreecreator.data;

import org.graphstream.graph.Graph;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AdjacencyListNode;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * 
 * Represents a solution tree.
 * 
 * @author Teren
 *
 */
public class SolutionTree extends SingleGraph {
	
	public final int numProcessors;
	
	public SolutionTree(String id, int numProcessors) {
		super(id);
		
		this.numProcessors = numProcessors;
	}
}