package data.solutiontree;

import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.DefaultGraph;

/**
 * 
 * Represents a solution tree.
 * 
 * @author Teren
 *
 */
public class SolutionTree extends DefaultGraph{

	public SolutionTree(String id) {
		super(id);
	}
	
}