package solutiontreecreator.data;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Node;

/**
 * This class represents a node in a solution tree. It contains a solution and information on parent and children nodes. 
 * Because of the recursive nature of trees, an instance of this class can represent an entire tree.
 * 
 * @author Terence Qu
 * 
 */
public class SolutionNode {

	public Solution solution;
	public SolutionNode parent;
	public List<SolutionNode> children;
	
	public SolutionNode() {
		children = new ArrayList<SolutionNode>();
	}

	/**
	 * Adds a child node to this solution node.
	 * @param solutionNode
	 */
	public void addChild(SolutionNode solutionNode) {
		this.children.add(solutionNode);
		solutionNode.parent = this;
	}

	/**
	 * Returns a child at a specific index.
	 * @param i index
	 * @return The child at index i.
	 */
	public SolutionNode getChild(int i) {
		return children.get(i);
	}

	/**
	 * The string representation of this node.
	 * @return String representation of this node.
	 */
	public String stringRepresentation() {
		try {
			return solution.stringData();
		} catch (NullPointerException e) {
			return "root";
		}
	}
	
	/**
	 * Check if a task is already in the solution.
	 */
	public boolean isTaskInSolution(Node node) {
		for(Processor timeline: solution.getProcessors()) {
			if(timeline.mapOfTasksAndStartTimes.containsKey(timeline)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prints the tree in a console form, for debugging purposes.
	 * A Java conversion of the first StackOverflow answer at this link: https://stackoverflow.com/questions/1649027/how-do-i-print-out-a-tree-structure
	 * @param indent
	 * @param last
	 */
	public void printPretty(String indent, boolean last) {
		System.out.print(indent);
		if (last)
		{
			System.out.print("\\-");
			indent += "  ";
		}
		else
		{
			System.out.print("|-");
			indent += "| ";
		}

		System.out.println(this.stringRepresentation());

		for (int i = 0; i < children.size(); i++) {
			children.get(i).printPretty(indent, i == children.size() - 1);
		}
	}

	
	/**
	 * Returns true when this solution is complete.
	 */
	public boolean isSolutionComplete() {
		if (solution.getTasksLeft().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}
