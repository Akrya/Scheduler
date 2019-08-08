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

	public String stringRepresentation() {
		try {
			return this.solution.getLastTask().getId();
		} catch (NullPointerException e) {
			return null;
		}
		
	}
	
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
}
