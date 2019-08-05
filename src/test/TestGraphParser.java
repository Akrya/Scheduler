package test;

import org.junit.Test;

import graph.GraphParser;

public class TestGraphParser {
	
	@Test
	public void testNodes7OutTreeFile() {
		String path = getClass().getClassLoader().getResource("graphfiles/Nodes_7_OutTree.dot").getPath();
		System.out.println(path);
		GraphParser graphParser = new GraphParser(path);
		
		graphParser.printGraphInConsole();
	}
}
