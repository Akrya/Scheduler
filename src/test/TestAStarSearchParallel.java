package test;

import graph.GraphController;
import graph.TaskGraph;
import org.junit.Test;
import solutionfinder.AStarParallelSolutionFinder;
import solutionfinder.data.Solution;

import static org.junit.Assert.assertEquals;

public class TestAStarSearchParallel {

    @Test
    public void test7Nodes(){
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
    public void test8Nodes(){
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

    @Test
    public void test10Nodes(){
        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_10_Random.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);

        System.out.println("---------------------------------");

        AStarParallelSolutionFinder solutionTreeCreator = new AStarParallelSolutionFinder(4, taskGraph);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
        assertEquals(optimalSolution.getTotalTime(), 50.0f, 0.0f);
    }

    @Test
    public void test11Nodes(){
        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_11_OutTree.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);

        System.out.println("---------------------------------");

        AStarParallelSolutionFinder solutionTreeCreator = new AStarParallelSolutionFinder(4, taskGraph);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
        assertEquals(optimalSolution.getTotalTime(), 227.0f, 0.0f);
    }
}
