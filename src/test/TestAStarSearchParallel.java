package test;

import graph.GraphController;
import graph.TaskGraph;
import org.junit.Test;
import solutionfinder.AStarParallelSolutionFinder;
import solutionfinder.data.Solution;

import static org.junit.Assert.assertEquals;

public class TestAStarSearchParallel {

    @Test
    public void testSevenNodes(){
        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_7_OutTree.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);

        System.out.println("---------------------------------");

        AStarParallelSolutionFinder solutionTreeCreator = new AStarParallelSolutionFinder(4, taskGraph);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
        assertEquals(optimalSolution.getTotalTime(), 22.0f, 0.0f);
    }

    @Test
    public void testEightNodes(){
        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_8_Random.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);

        System.out.println("---------------------------------");

        AStarParallelSolutionFinder solutionTreeCreator = new AStarParallelSolutionFinder(4, taskGraph);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
        assertEquals(optimalSolution.getTotalTime(), 581.0f, 0.0f);
    }

}
