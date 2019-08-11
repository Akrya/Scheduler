package test;



/*
	Four PROCESSORS
	----------------------------------
	Nodes_7_OutTree.dot    22
	Nodes_8_Random.dot     581
	Nodes_9_SeriesParallel.dot   55
	Nodes_10_Random.dot    50
	Nodes_11_OutTree.dot   227

	Two PROCESSORS
	----------------------------------
	Nodes_7_OutTree.dot    28
	Nodes_8_Random.dot     581
	Nodes_9_SeriesParallel.dot   55
	Nodes_10_Random.dot    50
	Nodes_11_OutTree.dot   350


	One PROCESSOR
	----------------------------------
	Nodes_7_OutTree.dot    40
	Nodes_8_Random.dot     969
	Nodes_9_SeriesParallel.dot   55
	Nodes_10_Random.dot    63
	Nodes_11_OutTree.dot   640

 */






public class TestRandomStuff {

	/**
	 * Test the Nodes_7_OutTree.dot file.
	 */
//	@Test
//	public void testNodes7OutTreeFile() {
//		// Parse input file
//		String path = getClass().getClassLoader().getResource("graphfiles/Nodes_7_OutTree.dot").getPath();
//		System.out.println(path);
////		GraphParser graphParser = new GraphParser(path);
//
//		// Dump info of graph in the console
////		graphParser.printGraphInConsole();
//
//		System.out.println("---------------------------------");
//		System.out.println("---------------------------------");
//		System.out.println("---------------------------------");
//
//
//		//Finding the time it takes to create the tree
//		long startime = new Date().getTime();
//
//		SolutionTreeCreator solutionTreeCreator = new SolutionTreeCreator(2, graphParser.g);
//		solutionTreeCreator.buildSolutionTree();
//
//		long endtime = new Date().getTime();
//		long timeTreeCreator = endtime - startime;
//
//		// Print out the tree
//		// solutionTreeCreator.getTreeRoot().printPretty("", true);
//
//		//Finding the time it takes to find the solution from the tree
//		long startimeSolution = new Date().getTime();
//		// Find the solutions
//		List<Solution> solutions = BasicSolutionFinder.findOptimalSolution(solutionTreeCreator.getTreeRoot());
//		for(Solution s: solutions) {
//			System.out.println("---------------------------------");
//			s.printData();
//			System.out.println("--SOLUTION TIME: "+s.getTotalTime()+"--");
//
//			assert(s.getTotalTime() == 28);
//		}
//
//		long endtimeSolution = new Date().getTime();
//		long timeSolutionfinder = endtimeSolution -startimeSolution ;
//
//		System.out.println("TREECREATION TIME: "+timeTreeCreator + " ms");
//		System.out.println("SOLUTIONFINDING TIME: "+timeSolutionfinder + " ms");
//
//	}
//
//	/**
//	 * Test the Nodes_8_Random.dot file.
//	 */
//	@Test
//	public void testNodes8RandomFile() {
//		// Parse input file
//		String path = getClass().getClassLoader().getResource("graphfiles/Nodes_8_Random.dot").getPath();
//		System.out.println(path);
////		GraphParser graphParser = new GraphParser(path);
//
//		// Dump info of graph in the console
//		graphParser.printGraphInConsole();
//
//		System.out.println("---------------------------------");
//		System.out.println("---------------------------------");
//		System.out.println("---------------------------------");
//
//
//		//Finding the time it takes to create the tree
//		long startime = new Date().getTime();
//
//		SolutionTreeCreator solutionTreeCreator = new SolutionTreeCreator(2, graphParser.g);
//		solutionTreeCreator.buildSolutionTree();
//
//		long endtime = new Date().getTime();
//		long timeTreeCreator = endtime - startime;
//
//
//		// Print out the tree
//		// solutionTreeCreator.getTreeRoot().printPretty("", true);
//
//		//Finding the time it takes to find the solution from the tree
//		long startimeSolution = new Date().getTime();
//		// Find the solutions
//		List<Solution> solutions = BasicSolutionFinder.findOptimalSolution(solutionTreeCreator.getTreeRoot());
//		for(Solution s: solutions) {
//			System.out.println("---------------------------------");
//			s.printData();
//			System.out.println("--SOLUTION TIME: "+s.getTotalTime()+"--");
//
//			assert(s.getTotalTime() == 581);
//		}
//
//		long endtimeSolution = new Date().getTime();
//		long timeSolutionfinder = endtimeSolution -startimeSolution ;
//
//		System.out.println("TREECREATION TIME: "+timeTreeCreator + " ms");
//		System.out.println("SOLUTIONFINDING TIME: "+timeSolutionfinder + " ms");
//
//	}
}
