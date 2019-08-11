package graph;

import java.util.ArrayList;
import java.util.List;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * 
 * A simple implementation of SingleGraph, which represents a Task graph.
 * 
 * @author Terence
 */
public class TaskGraph extends SingleGraph{

	public TaskGraph(String id) {
		super(id);
	}

	/**
	 * Finds a list of all the roots in the graph. 
	 * Returns empty list if there aren't any roots.
	 * This algorithm does not have to be complex, since task graps are small in size.
	 * 
	 * @return root nodes
	 */
	public List<Node> getRootNodes() {
		List<Node> rootNodes = new ArrayList<Node>();
		
		// Testing for nodes with 0 inDegree. These will be the roots.
		for(Node n : this.getEachNode()) {
			System.out.println("Testing node "+n.getId()+" with in degree of "+n.getInDegree());
			if(n.getInDegree()==0) {
				rootNodes.add(n);
			}
		}
		return rootNodes;
	}
}
