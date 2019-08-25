package test;

import graph.GraphController;
import graph.TaskGraph;
import org.junit.Test;
import solutionfinder.AStarParallelSolutionFinder;
import solutionfinder.AStarSolutionFinder;
import solutionfinder.data.Solution;

import static org.junit.Assert.assertEquals;

public class TestAStarSearchSpeed {
    @Test
    public void test11NodesSequential(){
        long startTime = System.nanoTime();

        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_11_OutTree.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);
        taskGraph.setUpBottomLevels();

        System.out.println("---------------------------------");

        AStarSolutionFinder solutionTreeCreator = new AStarSolutionFinder(4, taskGraph);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
        assertEquals(optimalSolution.getTotalTime(), 227.0f, 0.0f);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds : " +
                timeElapsed / 1000000);
    }

    @Test
    public void test11NodesParallel(){
        long startTime = System.nanoTime();

        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_11_OutTree.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);
        taskGraph.setUpBottomLevels();

        System.out.println("---------------------------------");

        AStarParallelSolutionFinder solutionTreeCreator = new AStarParallelSolutionFinder(4, taskGraph,4);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
        assertEquals(optimalSolution.getTotalTime(), 227.0f, 0.0f);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds : " +
                timeElapsed / 1000000);
    }
}
