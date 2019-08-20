package test;

import graph.GraphController;
import graph.TaskGraph;
import org.junit.Test;
import solutiontreecreator.AStarSolutionFinder;
import solutiontreecreator.data.Solution;

public class TestAStarSearch {

    @Test
    public void testOne(){
        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_11_OutTree.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);

        System.out.println("---------------------------------");

        AStarSolutionFinder solutionTreeCreator = new AStarSolutionFinder(4, taskGraph);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
    }

}
