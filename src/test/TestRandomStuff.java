package test;

import java.util.Collections;

import org.graphstream.graph.Node;
import org.junit.Test;

import graph.GraphParser;
import graph.TaskGraph;
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
		solutionTreeCreator.getTreeRoot().printPretty("", true);
	}
	
	/**
	@Test
	public void testSolutionMaking() {
		// Parse input file
		String path = getClass().getClassLoader().getResource("graphfiles/Nodes_7_OutTree.dot").getPath();
		System.out.println(path);
		GraphParser graphParser = new GraphParser(path);
		
		TaskGraph g = graphParser.g;
		
		Solution test = new Solution(g, 3);
		Node n1 = g.getNode("0");
		Node n2 = g.getNode("1");
		Node n3 = g.getNode("2");
		Node n4 = g.getNode("3");
		Node n5 = g.getNode("4");
		Node n6 = g.getNode("5");
		Node n7 = g.getNode("6");
		
		test.addTask(n1, 0);
		test.addTask(n2, 1);
		test.addTask(n3, 2);
		test.addTask(n4, 0);
		test.addTask(n5, 1);
		test.addTask(n6, 2);
		test.addTask(n7, 2);
		
		test.printData();
		
	}
	**/
}
