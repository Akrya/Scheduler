package test;

import graph.GraphController;
import graph.TaskGraph;
import org.junit.Test;
import solutiontreecreator.AstarSolutionTreeCreator;
import solutiontreecreator.data.Solution;

import java.util.Date;

public class TestAStarSearch {

    @Test
    public void testOne(){
        // Parse input file
        String path = getClass().getClassLoader().getResource("graphfiles/Nodes_7_OutTree.dot").getPath();
        System.out.println(path);
        TaskGraph taskGraph = GraphController.parseInputFile(new TaskGraph("1"), path);

        System.out.println("---------------------------------");

        AstarSolutionTreeCreator solutionTreeCreator = new AstarSolutionTreeCreator(4, taskGraph);
        Solution optimalSolution = solutionTreeCreator.startOptimalSearch();
        optimalSolution.printData();
    }

}
