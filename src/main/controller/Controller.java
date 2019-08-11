package main.controller;

import graph.GraphController;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

public class Controller {

    private String dotFileName;
    private int numOfProcessors;
    private int numOfCores;
    private boolean visualizeSearch;
    private String outputFileName;
    private Graph inputGraph;

    /**
     * Create and displays the dot file as a graph
     */
    public void initialise() {

        inputGraph = new DefaultGraph("inputGraph");

        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        if (visualizeSearch) {
            GraphController.viewGraph(inputGraph);
        }

    }

    /**
     * Takes the solution graph and generates the output file
     */
    private void writeOutputFile() {

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
