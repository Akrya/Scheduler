package main.controller;

import graph.GraphController;

import graph.TaskGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import solutionfinder.BasicSolutionFinder;
import solutiontreecreator.SolutionTreeCreator;
import solutiontreecreator.data.Processor;
import solutiontreecreator.data.Solution;
import sun.java2d.pipe.SolidTextRenderer;

import java.util.List;
import java.util.Random;

public class Controller {

    private String dotFileName;
    private int numOfProcessors;
    private int numOfCores;
    private boolean visualizeSearch;
    private String outputFileName;

    private TaskGraph inputGraph;

    public void initialise() {

        inputGraph = new TaskGraph("inputGraph");

//        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        SolutionTreeCreator solutionTreeCreator = new SolutionTreeCreator(numOfProcessors, inputGraph);
        solutionTreeCreator.buildSolutionTree();

        findSolution(solutionTreeCreator);

        GraphController.outputGraphDotFile(inputGraph, outputFileName);



        if (visualizeSearch) {
            GraphController.viewGraph(inputGraph);
        }

    }

    private void writeOutputFile() {
        GraphController.outputGraphDotFile(inputGraph, outputFileName);
    }

    private void findSolution(SolutionTreeCreator solutionTreeCreator) {
        List<Solution> solutions = BasicSolutionFinder.findOptimalSolution(solutionTreeCreator.getTreeRoot());

        System.out.println(solutions.size());

        Random rand = new Random();
        int randomOptimalSolution = rand.nextInt((solutions.size() - 1));

        Solution optimalSolution = solutions.get(randomOptimalSolution);

        for (Solution s: solutions) {
            s.printData();
            System.out.println(s.getTotalTime());
            System.out.println(s.getTaskList().toString());
            s.stringData();
        }

        Processor[] processors = optimalSolution.getProcessors();
        for (Node n: inputGraph.getNodeSet()) {
            System.out.println("Running through the nodes ///////////////////////////////////////");
            for (int i = 0; i < processors.length; i++) {
                Object[] tasks = processors[i].mapOfTasksAndStartTimes.keySet().toArray();
                System.out.println(tasks[0].toString());
//                System.out.println();
                System.out.println("Running through the processors *******************************");
                if (processors[i].mapOfTasksAndStartTimes.keySet().contains(n.getId())) {
                    System.out.println("Found node --------------------------------------");
                    GraphController.changeAttribute(n.getId(), "Start", (int) Math.round(processors[i].mapOfTasksAndStartTimes.get(n)), inputGraph);
                    GraphController.changeAttribute(n.getId(), "Processor", i, inputGraph);
                }
            }
//            for (Processor p: processors) {
//                if (p.mapOfTasksAndStartTimes.keySet().contains(n.getId())) {
//                    GraphController.changeAttribute(n.getId(), "Start", (int) Math.round(p.mapOfTasksAndStartTimes.get(n)), inputGraph);
//                    GraphController.changeAttribute(n.getId(), "Processor", );
//                }
//            }
        }

    }


    public String getGraphFilename() {
        return dotFileName;
    }

    public void setGraphFilename(String graphFilename) {
        this.dotFileName = graphFilename;
    }

    public int getNumOfProcessors() {
        return numOfProcessors;
    }

    public void setNumOfProcessors(int numOfProcessors) {
        this.numOfProcessors = numOfProcessors;
    }

    public int getNumOfCores() {
        return numOfCores;
    }

    public void setNumOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    public boolean isVisualizeSearch() {
        return visualizeSearch;
    }

    public void setVisualizeSearch(boolean visualizeSearch) {
        this.visualizeSearch = visualizeSearch;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

}
