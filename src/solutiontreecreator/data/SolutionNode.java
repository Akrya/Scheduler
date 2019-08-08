package solutiontreecreator.data;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Node;

public class SolutionNode {

	public Solution solution;
	public SolutionNode parent;
	public List<SolutionNode> children;
	public Node latestTask;

	public SolutionNode() {
		children = new ArrayList<SolutionNode>();
	}

	public void addChild(SolutionNode solutionNode) {
		this.children.add(solutionNode);
		solutionNode.parent = this;
	}

	public SolutionNode getChild(int i) {
		return children.get(i);
	}

	/**
	 * String representation of this node.
	 * @return
	 */
	public String stringRepresentation() {
		try {
			return this.solution.getLastTask().getId();
		} catch (NullPointerException e) {
			return "root";
		}

	}

	/**
	 * Prints the tree in a console form, for debugging purposes.
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
	 * Gets the total time spent for this solution.
	 * @return
	 */
	public double getTotalTime() {
		double longestTime = 0;
		for(Timeline t: this.solution.processors) {
			if(t.getEndTime() > longestTime) {
				t.getEndTime();
			}
		}
		
		return longestTime;
	}
}
