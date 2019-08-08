package test;

import java.util.List;

import org.graphstream.graph.Node;
import org.junit.Test;

import graph.GraphParser;
import solutiontreecreator.SolutionTreeCreator;
import solutiontreecreator.data.SolutionNode;

public class TestRandomStuff {
	
	@Test
	public void testNodes7OutTreeFile() {
		// Parse input file
		String path = getClass().getClassLoader().getResource("graphfiles/Nodes_7_OutTree.dot").getPath();
		System.out.println(path);
		GraphParser graphParser = new GraphParser(path);
		
		// Dump info of graph in the console
		graphParser.printGraphInConsole();
		
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		SolutionTreeCreator solutionTreeCreator = new SolutionTreeCreator(2, graphParser.g);
		solutionTreeCreator.convertTaskGraphToSolutionTree();
		
		// Print out the tree
		solutionTreeCreator.getTree().root.printPretty("", true);
		
		// Print out all leaves
		System.out.println("");
		for(SolutionNode s: solutionTreeCreator.leaves) {
			System.out.print(s.latestTask.getId()+", ");
		}
		System.out.println("");
		
		System.out.println("");
		List<SolutionNode> solutions = solutionTreeCreator.optimalSolutions();
		for(SolutionNode s: solutions) {
			System.out.print(s.getTotalTime()+", ");
		}
		System.out.println("");
	}
}
