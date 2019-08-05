package test;

import java.util.List;

import org.graphstream.graph.Node;
import org.junit.Test;

import graph.GraphParser;

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
		
		
		
		// Get the root nodes of the graph
		List<Node> rootNodes = graphParser.g.getRootNodes();
		
		// Dump info of root nodes in the console
		System.out.println("---------------------------------");
		for(Node n: rootNodes) {
			System.out.println(n.getId()+": "+n.getInDegree());
		}
		
	}
}
