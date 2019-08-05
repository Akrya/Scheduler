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
		this.setNodeFactory(new SolutionFactory(numProcessors));
	}
	
	/**
	 * Generating a new instance of a Node will produce a Solution class instead, through this factory.
	 * @author Terence
	 *
	 */
	private class SolutionFactory implements NodeFactory<Solution>{
		public final int numProcessors;
		
		public SolutionFactory(int numProcessors) {
			this.numProcessors = numProcessors;
		}
		
		@Override
		public Solution newInstance(String id, Graph graph) {
			return new Solution((SolutionTree) graph, id, numProcessors);
		}
		
	}
}