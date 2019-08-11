package main.controller;

import graph.GraphController;

import graph.TaskGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import solutionfinder.BasicSolutionFinder;
import solutiontreecreator.SolutionTreeCreator;
import solutiontreecreator.data.Solution;

import java.util.List;

public class Controller {

    private String dotFileName;
    private int numOfProcessors;
    private int numOfCores;
    private boolean visualizeSearch;
    private String outputFileName;

    private TaskGraph inputGraph;

    /**
     * Create and displays the dot file as a graph
     */
    public void initialise() {

        inputGraph = new TaskGraph("inputGraph");

//        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        SolutionTreeCreator solutionTreeCreator = new SolutionTreeCreator(numOfProcessors, inputGraph);
        solutionTreeCreator.buildSolutionTree();

        findSolution(solutionTreeCreator);

        if (visualizeSearch) {
            GraphController.viewGraph(inputGraph);
        }

    }

    /**
     * Takes the solution graph and generates the output file
     */
    private void writeOutputFile() {
        GraphController.outputGraphDotFile(inputGraph, outputFileName);
    }

    private void findSolution(SolutionTreeCreator solutionTreeCreator) {
        List<Solution> solutions = BasicSolutionFinder.findOptimalSolution(solutionTreeCreator.getTreeRoot());

        System.out.println(solutions.size());

        for (Solution s: solutions) {
            System.out.println(s.getTotalTime());
            System.out.println(s.getTaskList().toString());
            s.stringData();
        }

    }

    /**
     * Setters and getters for fields
     */
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
