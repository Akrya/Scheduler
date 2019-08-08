package test;

import java.util.List;

import org.junit.Test;

import graph.GraphParser;
import solutionfinder.BasicSolutionFinder;
import solutiontreecreator.SolutionTreeCreator;
import solutiontreecreator.data.Solution;

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
		solutionTreeCreator.buildSolutionTree();

		// Print out the tree
		// solutionTreeCreator.getTreeRoot().printPretty("", true);
		
		// Find the solutions
		List<Solution> solutions = BasicSolutionFinder.findOptimalSolution(solutionTreeCreator.getTreeRoot());
		for(Solution s: solutions) {
			s.printData();
			System.out.println(s.getProcessors()[0].getEndTime());
			System.out.println(s.getProcessors()[1].getEndTime());
		}
	}
}
