package graph;

import java.util.*;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * 
 * A simple implementation of SingleGraph, which represents a Task graph.
 * 
 * @author Terence
 */
public class TaskGraph extends SingleGraph{

	public HashMap<Node, Double> nodesAndBottomLevels;

	public TaskGraph(String id) {
		super(id);
		nodesAndBottomLevels = new HashMap<>();
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

	public void setUpBottomLevels(){
		for(Node n: this.getNodeSet()){
			nodesAndBottomLevels.put(n, getBottomLevelOfNode(n));
		}

		for(Map.Entry<Node, Double> entry: nodesAndBottomLevels.entrySet()){
			System.out.println("Bottom level of node "+entry.getKey()+": "+entry.getValue());
		}
	}

	/**
	 * Calculates the bottom level of a node n. This is the length of the longest path.
	 * Bottom level is recursively defined in the 2003 Oliver Sinnen paper.
	 * @param n
	 * @return
	 */
	public static double getBottomLevelOfNode(Node n){
		List<Node> independentNodes = new ArrayList<>(n.getGraph().getNodeSet());
		independentNodes.remove(n);

		List<Double> bottomLevelCandidates = new ArrayList<Double>();
		bottomLevelCandidates.add(0.0);
		for(Edge e: n.getLeavingEdgeSet()){
			bottomLevelCandidates.add((double)n.getAttribute("Weight")+getBottomLevelOfNode(e.getTargetNode()));
			independentNodes.remove(e.getTargetNode());
		}

		for(Double d: bottomLevelCandidates){
			for(Node inode: independentNodes){
				d = d+(double)inode.getAttribute("Weight");
			}
		}

		return Collections.max(bottomLevelCandidates);
	}
}
